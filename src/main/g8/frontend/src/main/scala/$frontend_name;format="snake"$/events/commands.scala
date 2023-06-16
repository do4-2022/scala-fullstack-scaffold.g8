package $frontend_name;format="snake"$.events

sealed trait Command

case class Create(itemText: String) extends Command
case class UpdateText(itemId: Int, title: String) extends Command
case class UpdateCompleted(itemId: Int, completed: Boolean) extends Command
case class Delete(itemId: Int) extends Command
case object DeleteCompleted extends Command
case object Reload extends Command
