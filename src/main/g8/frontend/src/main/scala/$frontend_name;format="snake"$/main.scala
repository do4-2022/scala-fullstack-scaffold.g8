package $frontend_name;format="snake"$

import com.raquo.laminar.api.L.{given, *}
import org.scalajs.dom
import org.scalajs.dom._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js

import components.{newTodoInput, statusBar, hideIfNoItems, todoItem}
import $frontend_name;format="snake"$.state.{itemsVar, filterVar}
import $frontend_name;format="snake"$.events.{Reload, loadTodos, commandObserver}
import $frontend_name;format="snake"$.components.errorMessage
import $frontend_name;format="snake"$.components.header

// from https://laminar.dev/examples/todomvc

lazy val appNode: HtmlElement = {
  val todoItemsSignal = itemsVar.signal
    .combineWith(filterVar.signal)
    .mapN(_ filter _.passes)
  div(
    cls("todoapp"),
    header(),
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
