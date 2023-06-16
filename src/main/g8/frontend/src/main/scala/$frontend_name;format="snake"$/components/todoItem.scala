package $frontend_name;format="snake"$.components

import com.raquo.airstream.core.Signal
import com.raquo.airstream.state.Var
import com.raquo.laminar.api.L.{given, *}

import $frontend_name;format="snake"$.state.TodoItem
import $frontend_name;format="snake"$.events.{onEnterPress, Delete, UpdateText, commandObserver}

// Render a single item. Note that the result is a single element: not a stream, not some virtual DOM representation.
def todoItem(
    itemId: Int,
    initialTodo: TodoItem,
    itemSignal: Signal[TodoItem]
): HtmlElement = {
  val isEditingVar = Var(false) // Example of local state
  val updateTextObserver = commandObserver.contramap[UpdateText] {
    updateCommand =>
      isEditingVar.set(false)
      updateCommand
  }
  li(
    cls <-- itemSignal.map(item => Map("completed" -> item.completed)),
    onDblClick
      .filter(_ => !isEditingVar.now())
      .mapTo(true) --> isEditingVar.writer,
    children <-- isEditingVar.signal.map[List[HtmlElement]] {
      case true =>
        textUpdateInput(itemId, itemSignal, updateTextObserver) :: Nil
      case false =>
        List(
          checkboxInput(itemId, itemSignal),
          label(child.text <-- itemSignal.map(_.title)),
          button(
            cls("destroy"),
            onClick.mapTo(Delete(itemId)) --> commandObserver
          )
        )
    }
  )
}
