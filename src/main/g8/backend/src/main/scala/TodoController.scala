package todo

import zio._
import zio.http._
import zio.json._

object TodoController {

  val BasePath = !! / "todos"

  implicit val todoEncoder: JsonEncoder[Todo] = DeriveJsonEncoder.gen[Todo]
  implicit val todosEncoder: JsonEncoder[List[Todo]] =
    DeriveJsonEncoder.gen[List[Todo]]

  val routes: Http[Any, Nothing, Request, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> BasePath => {
        TodoService
          .getTodos()
          .map(_.toJson)
          .map(Response.text(_))
          .orElse(
            ZIO.succeed(
              Response.fromHttpError(
                HttpError.NotFound("No todos found")
              )
            )
          )
      }
      case Method.GET -> BasePath / id => {
        if (id.forall(_.isDigit)) {
          TodoService
            .getTodoById(id.toInt)
            .map(_.toJson)
            .map(Response.text(_))
            .orElse(
              ZIO.succeed(
                Response.fromHttpError(
                  HttpError.NotFound(s"Todo with ID $id not found")
                )
              )
            )
        } else {
          ZIO.succeed(
            Response.fromHttpError(HttpError.BadRequest("Invalid ID format"))
          )
        }
      }
      case Method.POST -> BasePath => {
        ZIO.succeed(Response.text("TODO: create a todo"))
      }
      case Method.PUT -> BasePath / id => {
        if (id.forall(_.isDigit)) {
          ZIO.succeed(Response.text("TODO: update a todo by id"))
        } else {
          ZIO.succeed(Response.fromHttpError(HttpError.BadRequest()))
        }
      }
      case Method.DELETE -> BasePath / id => {
        if (id.forall(_.isDigit)) {
          ZIO.succeed(Response.text("TODO: delete a todo by id"))
        } else {
          ZIO.succeed(Response.fromHttpError(HttpError.BadRequest()))
        }
      }
    }
}
