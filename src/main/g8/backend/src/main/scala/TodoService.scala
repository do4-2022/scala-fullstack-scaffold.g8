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

  def createTodo(): Task[Todo] =
    ???

  def updateTodo(todoId: Int, todo: Todo): Task[Todo] =
    ???

  def deleteTodoById(id: Int): Task[Unit] =
    ZIO
      .fromFuture(_ => todosCollection.deleteOne(equal("id", id)).toFuture())
      .unit

}
