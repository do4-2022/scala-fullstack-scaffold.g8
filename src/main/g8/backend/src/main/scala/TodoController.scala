package todo

import zio._
import zio.http._
import zio.json._
import zio.http.Header.{AccessControlAllowMethods, AccessControlAllowOrigin, Origin}
import zio.http.HttpAppMiddleware.cors
import zio.http.internal.middlewares.Cors.CorsConfig

object TodoController {

  val config: CorsConfig =
    CorsConfig(
    allowedOrigin = {
      case origin@Origin.Value(_, host, _) if host == "dev" => Some(AccessControlAllowOrigin.Specific(origin))
      case _ => Some(AccessControlAllowOrigin.All) // Allow any origin by setting Access-Control-Allow-Origin to *
    },
      allowedMethods = AccessControlAllowMethods(Method.PUT, Method.DELETE, Method.POST, Method.GET),
    )

  val BasePath = !! / "todos"
  
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
                  HttpError.NotFound(s"Todo with ID \$id not found")
                )
              )
            )
        } else {
          ZIO.succeed(
            Response.fromHttpError(HttpError.BadRequest("Invalid ID format"))
          )
        }
      }
      case req @ Method.POST -> BasePath => {
        (for {
          queryParams <- ZIO
            .fromOption(Option(req.url.queryParams))
            .orElseFail(HttpError.BadRequest("Missing query parameters"))
          title <- ZIO
            .fromOption(queryParams.get("title").collect(_.head))
            .orElseFail(HttpError.BadRequest("Missing 'title' parameter"))
          createdTodo <- TodoService.createTodo(title)
        } yield createdTodo)
          .fold(
            error => Response.fromHttpError(HttpError.InternalServerError()),
            todo => Response.text(todo.toJson)
          )
      }
      case req @ Method.PUT -> BasePath / id => {
        if (id.forall(_.isDigit)) {
          (for {
            queryParams <- ZIO.fromOption(Option(req.url.queryParams))
                                .orElseFail(HttpError.BadRequest("Missing query parameters"))
            title <- ZIO.fromOption(queryParams.get("title").collect(_.head))
                          .orElseFail(HttpError.BadRequest("Missing 'title' parameter"))
            updatedTodo <- TodoService.updateTodoTitle(id.toInt, title)
          } yield updatedTodo)
            .map(_.toJson)
            .map(Response.text(_))
            .orElse(
              ZIO.succeed(
                Response.fromHttpError(
                  HttpError.NotFound(s"Todo with ID \$id not found")
                )
              )
            )
        } else {
          ZIO.succeed(Response.fromHttpError(HttpError.BadRequest()))
        }
      }
      case Method.POST -> BasePath / id / "completed" => {
      if (id.forall(_.isDigit)) {
        TodoService
          .updateTodoCompleted(id.toInt)
          .map(_.toJson)
          .map(Response.text(_))
          .orElse(
            ZIO.succeed(
              Response.fromHttpError(
                HttpError.NotFound(s"Todo with ID \$id not found")
              )
            )
          )
      } else {
        ZIO.succeed(Response.fromHttpError(HttpError.BadRequest()))
      }
    }
    case Method.DELETE -> BasePath / id => {
        if (id.forall(_.isDigit)) {
          TodoService
            .deleteTodoById(id.toInt)
            .map(_ => Response.text(s"Todo with ID \$id has been deleted"))
            .orElse(
              ZIO.succeed(
                Response.fromHttpError(
                  HttpError.NotFound(s"Todo with ID \$id not found")
                )
              )
            )
        } else {
          ZIO.succeed(Response.fromHttpError(HttpError.BadRequest()))
        }
      }
    } @@ cors(config)
}