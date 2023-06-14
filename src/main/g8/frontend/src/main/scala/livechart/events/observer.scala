package livechart.events
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import com.raquo.airstream.core.Observer
import scala.util.{Success, Failure}

import livechart.api.todos.{
  getTodos,
  createTodo,
  setTodo,
  deleteTodo,
  deleteCompletedTodos
}
import livechart.state.{itemsVar, filterVar}
import livechart.state.{TodoItem, Filter, ShowCompleted, ShowAll}
import livechart.state.errorVar

private var lastId = 1 // just for auto-incrementing without a backend

val commandObserver = Observer[Command](onNext = handleCommand)

private def handleCommand(command: Command): Unit = {
  errorVar.set("")
  command match {
    case Reload =>
      getTodos().onComplete {
        case Success(items) =>
          itemsVar.set(items.toList)
          lastId = items.map(_.id).max
        case Failure(exception) =>
          errorVar.set(s"Failed to load todos : $exception")
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
          errorVar.set(s"Failed to create todo : $exception")
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
          errorVar.set(s"Failed to update todo : $exception")
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
          errorVar.set(s"Failed to update todo: $exception")
      }

    case Delete(itemId) =>
      deleteTodo(
        itemId
      ).onComplete {
        case Success(_) =>
          itemsVar.update(_.filterNot(_.id == itemId))
        case Failure(exception) =>
          errorVar.set(s"Failed to delete todo: $exception")
      }

    case DeleteCompleted =>
      deleteCompletedTodos().onComplete {
        case Success(_) =>
          itemsVar.update(_.filterNot(_.completed))
        case Failure(exception) =>
          errorVar.set(s"Failed to delete completed todos: $exception")
      }
  }
}
