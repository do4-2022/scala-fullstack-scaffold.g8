package todo

import zio.Task
import zio.{ZIO}
import java.sql.{Connection, DriverManager, ResultSet}
import scala.concurrent.{ Future , ExecutionContext}

object DB {

    private val databaseURL: String =
    Option(System.getenv("POSTGRES_URL"))
      .getOrElse("jdbc:postgresql://localhost:5432/postgres")

    private val databaseUser: String =
      Option(System.getenv("POSTGRES_USER"))
        .getOrElse("postgres")

    private val databasePassword: String =
        Option(System.getenv("POSTGRES_PASSWORD"))
            .getOrElse("postgres")

    val connection : ZIO[Any, Throwable, java.sql.Connection] =
    ZIO.fromFuture { implicit ec: ExecutionContext =>
      Future(DriverManager.getConnection(databaseURL, databaseUser, databasePassword))
    }

}
