package livechart.state

import com.raquo.airstream.state.Var

case class TodoItem(id: Int, text: String, completed: Boolean)
val itemsVar = Var(List[TodoItem]())
