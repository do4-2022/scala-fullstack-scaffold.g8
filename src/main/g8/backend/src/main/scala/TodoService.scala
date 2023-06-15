package todo

import zio.Task
import zio.{Runtime, ZIO}
import java.sql.{Connection, DriverManager, ResultSet}
import scala.concurrent.{ Future , ExecutionContext}


object TodoService {


    val url = "jdbc:postgresql://localhost:5432/postgres"
    val username = "postgres"
    val password = "postgres"

    val runtime = Runtime.default
  
    val connection: ZIO[Any, Throwable, java.sql.Connection] =
    ZIO.fromFuture { implicit ec: ExecutionContext =>
      Future(DriverManager.getConnection(url, username, password))
    }

  def readTodosFromResultSet(rs: ResultSet): Seq[Todo] = {
    var todos = Seq.empty[Todo]
    while (rs.next()) {
      val id = rs.getInt("id")
      val title = rs.getString("title")
      val done = rs.getBoolean("completed")
      todos = todos :+ Todo(id, title, done)
    }
    todos
  }

  def getTodos(): ZIO[Any, Throwable, Seq[Todo]] = 
    for {
      conn <- connection
      statement <- ZIO.succeed(conn.createStatement()).debug("todos")
      resultSet <- ZIO.succeed(statement.executeQuery("SELECT * FROM todos")).debug("todos")
      todos <- ZIO.succeed(readTodosFromResultSet(resultSet)).debug("todos")
    } yield todos


  def getTodoById(id: Int): Task[Option[Todo]] = 
    for {
      conn <- connection
      statement <- ZIO.succeed(conn.createStatement())
      resultSet <- ZIO.succeed(statement.executeQuery(s"SELECT * FROM todos WHERE id = $id"))
      todos <- ZIO.succeed(readTodosFromResultSet(resultSet))
    } yield todos.headOption

  def createTodo(): Task[Todo] =
    ???

  def updateTodo(todoId: Int, todo: Todo): Task[Todo] =
    ???

  def deleteTodoById(id: Int): Task[Unit] =
    for {
      conn <- connection
      statement <- ZIO.succeed(conn.createStatement())
      _ <- ZIO.succeed(statement.executeUpdate(s"DELETE FROM todos WHERE id = $id"))
    } yield ()

}
