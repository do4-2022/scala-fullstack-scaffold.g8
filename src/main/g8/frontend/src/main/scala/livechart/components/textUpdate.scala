package livechart.components
import com.raquo.laminar.api.L.{given, *}

import livechart.events.{UpdateText, onEnterPress}
import livechart.state.TodoItem

// Note that we pass reactive variables: `itemSignal` for reading, `updateTextObserver` for writing
def textUpdateInput(
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
