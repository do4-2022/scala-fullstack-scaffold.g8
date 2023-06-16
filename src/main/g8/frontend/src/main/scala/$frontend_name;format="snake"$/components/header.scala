package $frontend_name;format="snake"$.components
import com.raquo.laminar.api.L.{given, *}
import $frontend_name;format="snake"$.events.commandObserver
import $frontend_name;format="snake"$.events.Reload

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
