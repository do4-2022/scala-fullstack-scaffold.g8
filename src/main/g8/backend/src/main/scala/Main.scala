package todo

import zio._

import zio.http._

object TodoApp extends ZIOAppDefault {
  // Run it like any simple app
  override val run = Server.serve(TodoController.routes).provide(Server.default)
}