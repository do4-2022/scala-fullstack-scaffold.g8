package livechart

import com.raquo.laminar.api.L.{given, *}
import org.scalajs.dom
import org.scalajs.dom._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js

import components.{newTodoInput, statusBar, hideIfNoItems, todoItem}
import livechart.state.{itemsVar, filterVar}
import livechart.events.{Reload, loadTodos, commandObserver}

// from https://laminar.dev/examples/todomvc

// --- Views ---

lazy val appNode: HtmlElement = {
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
      newTodoInput
    ),
    div(
      hideIfNoItems,
      cls("main"),
      ul(
        cls("todo-list"),
        children <-- todoItemsSignal.split(_.id)(todoItem)
      )
    ),
    statusBar
  )
}

@main def main(): Unit = {
  val containerNode = dom.document.querySelector("#app")
  render(containerNode, appNode)
  loadTodos
}
