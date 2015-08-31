package api

import reactivemongo.api.{ DB, MongoConnection, MongoDriver }

/**
 * @author carlos
 */
trait ReactiveMongoApi {
  def driver: MongoDriver
  def connection: MongoConnection
  def db: DB
}