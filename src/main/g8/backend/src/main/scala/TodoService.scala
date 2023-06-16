package todo

import zio.Task
import zio.{ZIO}
import java.sql.{Connection, DriverManager, ResultSet}
import scala.concurrent.{ Future , ExecutionContext}

object TodoService {



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
      conn <- DB.connection
      statement <- ZIO.succeed(conn.createStatement())
      resultSet <- ZIO.succeed(statement.executeQuery("SELECT * FROM todos"))
      todos <- ZIO.succeed(readTodosFromResultSet(resultSet))
    } yield todos


  def getTodoById(id: Int): Task[Option[Todo]] = 
    for {
      conn <- DB.connection
      statement <- ZIO.succeed(conn.createStatement())
      resultSet <- ZIO.succeed(statement.executeQuery(s"SELECT * FROM todos WHERE id = $id"))
      todos <- ZIO.succeed(readTodosFromResultSet(resultSet))
    } yield todos.headOption

  def createTodo(
      title: String): Task[Option[Todo]] =
    for {
      conn <- DB.connection
      statement <- ZIO.succeed(conn.createStatement())
      resultSet <- ZIO.succeed(statement.executeQuery(s"INSERT INTO todos (title, completed) VALUES ('$title', false) RETURNING *"))
      todos <- ZIO.succeed(readTodosFromResultSet(resultSet))
    } yield todos.headOption

  def updateTodoTitle(todoId: Int, todo: String): Task[Todo] =
    for {
      conn <- DB.connection
      statement <- ZIO.succeed(conn.createStatement())
      resultSet <- ZIO.succeed(statement.executeQuery(s"UPDATE todos SET title = ${todo} WHERE id = $todoId RETURNING *"))
      todos <- ZIO.succeed(readTodosFromResultSet(resultSet))
    } yield todos.head

  def updateTodoCompleted(todoId : Int): Task[Todo] =
    for {
      conn <- DB.connection
      statement <- ZIO.succeed(conn.createStatement())
      resultSet <- ZIO.succeed(statement.executeQuery(s"UPDATE todos SET completed = NOT completed WHERE id = $todoId RETURNING *"))
      todos <- ZIO.succeed(readTodosFromResultSet(resultSet))
    } yield todos.head

  def deleteTodoById(id: Int): Task[Unit] =
    for {
      conn <- DB.connection
      statement <- ZIO.succeed(conn.createStatement())
      _ <- ZIO.succeed(statement.executeUpdate(s"DELETE FROM todos WHERE id = $id"))
    } yield ()

}
