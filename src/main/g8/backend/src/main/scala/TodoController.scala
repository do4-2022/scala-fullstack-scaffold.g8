package todo

import zio._
import zio.http._
import zio.json._

object TodoController {

  val BasePath = !! / "todos"

  implicit val todoEncoder: JsonEncoder[Todo] = DeriveJsonEncoder.gen[Todo]
  implicit val todosEncoder: JsonEncoder[List[Todo]] =
    DeriveJsonEncoder.gen[List[Todo]]

  val routes: HttpApp[Any, Nothing] = Http.collect[Request] {
    case Method.GET -> BasePath => {
      Response.text("TODO: get all todos")
      // TodoService.getTodos().map(_.toJson).map(Response.text(_))
    }
    case Method.GET -> BasePath / id => {
      if (id.forall(_.isDigit)) {
        // Response.text("TODO: get a todo by id")
        TodoService
          .getTodoById(id.toInt)
          .map(_.toJson)
          .map(Response.text(_))
          .orElse(
            Response.fromHttpError(
              HttpError.NotFound(s"Todo with ID $id not found")
            )
          )
      } else {
        Response.fromHttpError(HttpError.BadRequest())
      }
    }
    case Method.POST -> BasePath => {
      Response.text("TODO: create a todo")
    }
    case Method.PUT -> BasePath / id => {
      if (id.forall(_.isDigit)) {
        Response.text("TODO: update a todo by id")
      } else {
        Response.fromHttpError(HttpError.BadRequest())
      }
    }
    case Method.DELETE -> BasePath / id => {
      if (id.forall(_.isDigit)) {
        Response.text("TODO: delete a todo by id")
      } else {
        Response.fromHttpError(HttpError.BadRequest())
      }
    }
  }
}
