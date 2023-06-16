package $frontend_name;format="snake"$.components
import com.raquo.laminar.api.L.{given, *}

import $frontend_name;format="snake"$.events.{Create, onEnterPress, commandObserver}

def newTodoInput =
  input(
    cls("new-todo"),
    placeholder("What needs to be done?"),
    autoFocus(true),
    onEnterPress.mapToValue
      .filter(_.nonEmpty)
      .map(Create(_))
      .setValue("") --> commandObserver
  )
