package $frontend_name;format="snake"$.state

import com.raquo.airstream.state.Var
import $frontend_name;format="snake"$.state.TodoItem

sealed abstract class Filter(
    val name: String,
    val passes: TodoItem => Boolean
)

object ShowAll extends Filter("All", _ => true)
object ShowActive extends Filter("Active", !_.completed)
object ShowCompleted extends Filter("Completed", _.completed)

val filters: List[Filter] = ShowAll :: ShowActive :: ShowCompleted :: Nil

val filterVar = Var[Filter](ShowAll)
