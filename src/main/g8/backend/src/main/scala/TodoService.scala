package todo

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
}
