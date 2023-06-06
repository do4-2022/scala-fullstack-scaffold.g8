package todo

import zio._

object TodoService {

     private var todos: List[Todo] = List(
        Todo(1, "Faire les courses", completed = false),
        Todo(2, "Terminer le projet", completed = false),
        Todo(3, "Apprendre Scala", completed = true)
    )

    def getTodos(): Task[List[Todo]] = ???

    def getTodoById(id: Int): Task[Option[Todo]] = ???

    def createTodo(): Task[Todo] = ???

    def updateTodo( todoId : Int, todo : Todo): Task[Todo] = ???

    def deleteTodoById(id: Int): Task[Unit] = ???
    
}
