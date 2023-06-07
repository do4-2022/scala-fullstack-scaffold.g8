package todo

import org.mongodb.scala._

object MongoDBClient {
  def createClient(config: MongoDBConfig): MongoClient =
    MongoClient(config.uri)
}
