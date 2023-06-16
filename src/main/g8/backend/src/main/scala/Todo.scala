package todo
import zio.json._

final case class Todo(
    id: Int,
    title: String,
    completed: Boolean
)

object Todo {
  implicit val todoEncoder: JsonEncoder[Todo] = DeriveJsonEncoder.gen[Todo]
  implicit val todoDecoder: JsonDecoder[Todo] = DeriveJsonDecoder.gen[Todo]
}
