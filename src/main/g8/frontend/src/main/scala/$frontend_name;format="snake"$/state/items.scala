package $frontend_name;format="snake"$.state

import com.raquo.airstream.state.Var

case class TodoItem(id: Int, title: String, completed: Boolean)
val itemsVar = Var(List[TodoItem]())
