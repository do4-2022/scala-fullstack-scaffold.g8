package livechart
import com.raquo.laminar.api.L.{given, *}
import org.scalajs.dom
import org.scalajs.dom._
import scala.scalajs.js.Thenable.Implicits._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.{Failure, Success}

import org.scalajs.dom._
import scala.scalajs.js.JSON
import scala.scalajs.js.Thenable.Implicits._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.{Failure, Success}
import scala.scalajs.js
import livechart.TodoMvcApp.Reload
import livechart.TodoMvcApp.loadTodos

case class TodoItem(id: Int, text: String, completed: Boolean)

// from https://laminar.dev/examples/todomvc

object TodoMvcApp {

  sealed abstract class Filter(
      val name: String,
      val passes: TodoItem => Boolean
  )

  object ShowAll extends Filter("All", _ => true)

  object ShowActive extends Filter("Active", !_.completed)

  object ShowCompleted extends Filter("Completed", _.completed)

  val filters: List[Filter] = ShowAll :: ShowActive :: ShowCompleted :: Nil

  sealed trait Command

  case class Create(itemText: String) extends Command

  case class UpdateText(itemId: Int, text: String) extends Command

  case class UpdateCompleted(itemId: Int, completed: Boolean) extends Command

  case class Delete(itemId: Int) extends Command

  case object DeleteCompleted extends Command

  case object Reload extends Command

  // state

  private val itemsVar = Var(List[TodoItem]())

  private val filterVar = Var[Filter](ShowAll)

  private var lastId = 1 // just for auto-incrementing IDs

  private val commandObserver = Observer[Command] {
    case Reload =>
      getTodos().onComplete {
        case Success(items) =>
          itemsVar.set(items.toList)
          lastId = items.map(_.id).max
        case Failure(exception) =>
          println(s"Failed to load todos: $exception")
      }

    case Create(itemText) =>
      // currently incrementing lastId for testing purposes
      lastId += 1

      if (filterVar.now() == ShowCompleted)
        filterVar.set(ShowAll)

      createTodo(
        TodoItem(id = lastId, text = itemText, completed = false)
      ).onComplete {
        case Success(item) =>
          itemsVar.update(_ :+ item)
        case Failure(exception) =>
          println(s"Failed to create todo: $exception")
      }
    case UpdateText(itemId, text) =>
      val item = itemsVar.now().find(_.id == itemId).get

      setTodo(
        TodoItem(id = itemId, text = text, item.completed)
      ).onComplete {
        case Success(returnedItem) =>
          itemsVar.update(
            _.map(item =>
              if (item.id == returnedItem.id) returnedItem else item
            )
          )
        case Failure(exception) =>
          println(s"Failed to update todo: $exception")
      }

    case UpdateCompleted(itemId, completed) =>
      val item = itemsVar.now().find(_.id == itemId).get

      setTodo(
        item.copy(completed = completed)
      ).onComplete {
        case Success(returnedItem) =>
          itemsVar.update(
            _.map(item =>
              if (item.id == returnedItem.id) returnedItem else item
            )
          )
        case Failure(exception) =>
          println(s"Failed to update todo: $exception")
      }

    case Delete(itemId) =>
      deleteTodo(
        itemId
      ).onComplete {
        case Success(_) =>
          itemsVar.update(_.filterNot(_.id == itemId))
        case Failure(exception) =>
          println(s"Failed to delete todo: $exception")
      }
    case DeleteCompleted =>
      deleteCompletedTodos().onComplete {
        case Success(_) =>
          itemsVar.update(_.filterNot(_.completed))
        case Failure(exception) =>
          println(s"Failed to delete completed todos: $exception")
      }
  }

  // --- Views ---

  lazy val node: HtmlElement = {
    val todoItemsSignal = itemsVar.signal
      .combineWith(filterVar.signal)
      .mapN(_ filter _.passes)
    div(
      cls("todoapp"),
      div(
        cls("header"),
        h1("todos"),
        div(
          cls("top-bar"),
          button(
            "reload",
            cls("reload"),
            onClick.mapTo(Reload) --> commandObserver
          )
        ),
        renderNewTodoInput
      ),
      div(
        hideIfNoItems,
        cls("main"),
        ul(
          cls("todo-list"),
          children <-- todoItemsSignal.split(_.id)(renderTodoItem)
        )
      ),
      renderStatusBar
    )
  }

  private def renderNewTodoInput =
    input(
      cls("new-todo"),
      placeholder("What needs to be done?"),
      autoFocus(true),
      onEnterPress.mapToValue
        .filter(_.nonEmpty)
        .map(Create(_))
        .setValue("") --> commandObserver
    )

  // Render a single item. Note that the result is a single element: not a stream, not some virtual DOM representation.
  private def renderTodoItem(
      itemId: Int,
      initialTodo: TodoItem,
      itemSignal: Signal[TodoItem]
  ): HtmlElement = {
    val isEditingVar = Var(false) // Example of local state
    val updateTextObserver = commandObserver.contramap[UpdateText] {
      updateCommand =>
        isEditingVar.set(false)
        updateCommand
    }
    li(
      cls <-- itemSignal.map(item => Map("completed" -> item.completed)),
      onDblClick
        .filter(_ => !isEditingVar.now())
        .mapTo(true) --> isEditingVar.writer,
      children <-- isEditingVar.signal.map[List[HtmlElement]] {
        case true =>
          renderTextUpdateInput(itemId, itemSignal, updateTextObserver) :: Nil
        case false =>
          List(
            renderCheckboxInput(itemId, itemSignal),
            label(child.text <-- itemSignal.map(_.text)),
            button(
              cls("destroy"),
              onClick.mapTo(Delete(itemId)) --> commandObserver
            )
          )
      }
    )
  }

  // Note that we pass reactive variables: `itemSignal` for reading, `updateTextObserver` for writing
  private def renderTextUpdateInput(
      itemId: Int,
      itemSignal: Signal[TodoItem],
      updateTextObserver: Observer[UpdateText]
  ) =
    input(
      cls("edit"),
      defaultValue <-- itemSignal.map(_.text),
      onMountFocus,
      onEnterPress.mapToValue.map(UpdateText(itemId, _)) --> updateTextObserver,
      onBlur.mapToValue.map(UpdateText(itemId, _)) --> updateTextObserver
    )

  private def renderCheckboxInput(itemId: Int, itemSignal: Signal[TodoItem]) =
    input(
      cls("toggle"),
      typ("checkbox"),
      checked <-- itemSignal.map(_.completed),
      onInput.mapToChecked.map { isChecked =>
        UpdateCompleted(itemId, completed = isChecked)
      } --> commandObserver
    )

  private def renderStatusBar =
    footerTag(
      hideIfNoItems,
      cls("footer"),
      span(
        cls("todo-count"),
        child.text <-- itemsVar.signal
          .map(_.count(!_.completed))
          .map(pluralize(_, "item left", "items left"))
      ),
      ul(
        cls("filters"),
        filters.map(filter => li(renderFilterButton(filter)))
      ),
      child.maybe <-- itemsVar.signal.map { items =>
        if (items.exists(ShowCompleted.passes))
          Some(
            button(
              cls("clear-completed"),
              "Clear completed",
              onClick.map(_ => DeleteCompleted) --> commandObserver
            )
          )
        else None
      }
    )

  private def renderFilterButton(filter: Filter) =
    a(
      cls.toggle("selected") <-- filterVar.signal.map(_ == filter),
      onClick.preventDefault.mapTo(filter) --> filterVar.writer,
      filter.name
    )

  // Every little thing in Laminar can be abstracted away
  private def hideIfNoItems: Mod[HtmlElement] =
    display <-- itemsVar.signal.map { items =>
      if (items.nonEmpty) "" else "none"
    }

  // --- Generic helpers ---

  private def pluralize(num: Int, singular: String, plural: String): String =
    s"$num ${if (num == 1) singular else plural}"

  private val onEnterPress =
    onKeyPress.filter(_.keyCode == dom.ext.KeyCode.Enter)

  def loadTodos: Unit = {
    commandObserver.onNext(Reload)
  }
}

@main def main(): Unit = {
  val containerNode = dom.document.querySelector("#app")
  render(containerNode, TodoMvcApp.node)
  loadTodos
}
