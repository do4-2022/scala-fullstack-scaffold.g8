package livechart.components
import com.raquo.laminar.api.L.{given, *}
import livechart.events.commandObserver
import livechart.events.Reload

// header of the app
def header() = {
  div(
    cls("header"),
    h1("Todo"),
    errorMessage(),
    div(
      cls("top-bar"),
      button(
        "reload",
        cls("reload"),
        onClick.mapTo(Reload) --> commandObserver
      )
    ),
    newTodoInput
  )
}
