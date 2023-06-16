package todo

$if(postgres.truthy)$

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
$else$

import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.configuration.CodecRegistries.{
  fromProviders,
  fromRegistries
}
import org.mongodb.scala.{MongoClient, MongoCollection}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._

object DB {
  private val databaseURL: String =
    Option(System.getenv("MONGO_URL"))
      .getOrElse("mongodb://root:root@localhost:27018")

  private val customCodecs: CodecRegistry =
    fromProviders(classOf[Todo])

  private val codecRegistry: CodecRegistry =
    fromRegistries(customCodecs, DEFAULT_CODEC_REGISTRY)

  private val database =
    MongoClient(databaseURL)
      .getDatabase("todoapp")
      .withCodecRegistry(codecRegistry)

  val todosCollection: MongoCollection[Todo] =
    database.getCollection[Todo]("todos")
}

$endif$