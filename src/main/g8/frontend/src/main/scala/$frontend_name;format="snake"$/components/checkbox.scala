package $frontend_name;format="snake"$.components

import com.raquo.laminar.api.L.{given, *}

import $frontend_name;format="snake"$.state.TodoItem
import $frontend_name;format="snake"$.events.{UpdateCompleted, commandObserver}

def checkboxInput(itemId: Int, itemSignal: Signal[TodoItem]) =
  input(
    cls("toggle"),
    typ("checkbox"),
    checked <-- itemSignal.map(_.completed),
    onInput.mapToChecked.map { isChecked =>
      UpdateCompleted(itemId, completed = isChecked)
    } --> commandObserver
  )
