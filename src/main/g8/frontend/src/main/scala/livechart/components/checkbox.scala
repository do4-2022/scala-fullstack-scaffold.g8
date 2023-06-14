package livechart.components

import com.raquo.laminar.api.L.{given, *}

import livechart.state.TodoItem
import livechart.events.{UpdateCompleted, commandObserver}

def checkboxInput(itemId: Int, itemSignal: Signal[TodoItem]) =
  input(
    cls("toggle"),
    typ("checkbox"),
    checked <-- itemSignal.map(_.completed),
    onInput.mapToChecked.map { isChecked =>
      UpdateCompleted(itemId, completed = isChecked)
    } --> commandObserver
  )
