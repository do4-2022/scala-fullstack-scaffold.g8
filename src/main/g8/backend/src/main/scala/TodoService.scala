package todo

$if(postgres.truthy)$
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
$else$
import zio.Task
import zio._

import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._

import org.mongodb.scala.MongoCollection

object TodoService {
  private val todosCollection: MongoCollection[Todo] = DB.todosCollection

  def getTodos(): Task[Seq[Todo]] =
    ZIO.fromFuture(_ => todosCollection.find().toFuture())

  def getTodoById(id: Int): Task[Option[Todo]] =
    ZIO.fromFuture(_ => todosCollection.find(equal("id", id)).headOption())

  def createTodo(title: String): Task[Todo] =
    for {
      todos <- getTodos()
      newId = todos.map(_.id).max + 1
      newTodo = Todo(newId, title, false)
      _ <- ZIO
        .fromFuture(_ => todosCollection.insertOne(newTodo).toFuture())
        .unit
    } yield newTodo

  def updateTodoTitleField(todoId: Int, title: String): Task[Todo] =
    for {
      taskToChange <- getTodoById(todoId)
      updatedTodo <- taskToChange match {
        case Some(todo) =>
          val updated = todo.copy(title = title)
          ZIO
            .fromFuture(_ =>
              todosCollection
                .updateOne(equal("id", todoId), set("title", updated.title))
                .toFuture()
            )
            .map(_ => updated)
        case None =>
          ZIO.fail(
            new NoSuchElementException(s"Todo with ID $todoId not found")
          )
      }
    } yield updatedTodo

  def updateTodoCompletedField(todoId: Int): Task[Todo] =
    for {
      taskToChange <- getTodoById(todoId)
      updatedTodo <- taskToChange match {
        case Some(todo) =>
          val updated = todo.copy(completed = !todo.completed)
          ZIO
            .fromFuture(_ =>
              todosCollection
                .updateOne(
                  equal("id", todoId),
                  set("completed", updated.completed)
                )
                .toFuture()
            )
            .map(_ => updated)
        case None =>
          ZIO.fail(
            new NoSuchElementException(s"Todo with ID $todoId not found")
          )
      }
    } yield updatedTodo

  def deleteTodoById(id: Int): Task[Unit] =
    ZIO
      .fromFuture(_ => todosCollection.deleteOne(equal("id", id)).toFuture())
      .flatMap { result =>
        if (result.wasAcknowledged() && result.getDeletedCount == 0) {
          ZIO.fail(new Exception("Todo not found"))
        } else {
          ZIO.unit
        }
      }

  def deleteCompletedTodo(): Task[Unit] =
    ZIO
      .fromFuture(_ =>
        todosCollection.deleteMany(equal("completed", true)).toFuture()
      )
      .flatMap { result =>
        if (result.wasAcknowledged() && result.getDeletedCount == 0) {
          ZIO.fail(new Exception("No todo deleted"))
        } else {
          ZIO.unit
        }
      }
}
$endif$

