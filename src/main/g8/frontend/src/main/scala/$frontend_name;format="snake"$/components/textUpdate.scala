package $frontend_name;format="snake"$.components
import com.raquo.laminar.api.L.{given, *}

import $frontend_name;format="snake"$.events.{UpdateText, onEnterPress}
import $frontend_name;format="snake"$.state.TodoItem

// Note that we pass reactive variables: `itemSignal` for reading, `updateTextObserver` for writing
def textUpdateInput(
    itemId: Int,
    itemSignal: Signal[TodoItem],
    updateTextObserver: Observer[UpdateText]
) =
  input(
    cls("edit"),
    defaultValue <-- itemSignal.map(_.title),
    onMountFocus,
    onEnterPress.mapToValue.map(UpdateText(itemId, _)) --> updateTextObserver,
    onBlur.mapToValue.map(UpdateText(itemId, _)) --> updateTextObserver
  )
