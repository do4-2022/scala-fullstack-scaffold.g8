package todo

import zio._
import zio.http._

object TodoApp extends ZIOAppDefault {
  val port: Int =
    sys.env
      .get("PORT")
      .filter(_.nonEmpty)
      .map(_.toInt)
      .getOrElse(8080)

  private val config = Server.Config.default.port(port)
  private val configLayer = ZLayer.succeed(config)

  printf("Server listening on http://0.0.0.0:%d\n", port)
  override val run =
    Server.serve(TodoController.routes).provide(configLayer, Server.live)
}
