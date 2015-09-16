package models.daos.traits.core

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import models.daos.traits.core.BasicCrudDAO.RemoveFailed
import models.daos.traits.core.BasicCrudDAO.SavingFailed
import play.api.Logger
import play.api.libs.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.json.collection.JSONCollectionProducer
import play.modules.reactivemongo.json._
import play.api.libs.json.Reads
import reactivemongo.api.DB
import reactivemongo.api.Cursor
import reactivemongo.api.commands.WriteResult
import play.api.libs.json.Json.JsValueWrapper


abstract class BasicCrudDAO[T](db: DB, collectionName: String) extends CrudDAO[T] {

  val collection = db[JSONCollection](collectionName)

  def create(t: T)(implicit tjs: OWrites[T]): Future[Try[String]] = {
    Logger.debug(s"create documents: [collection=$collectionName, query=$t]")
    collection.insert(t) map {
      lastError =>
        if (lastError.ok) {
          Success(t.toString())
        } else {
          Failure(SavingFailed(lastError.errmsg.get))
        }
    }
  }

  def read(id: String)(implicit readsT: Reads[T]): Future[Option[T]] = {
    Logger.debug(s"reading documents: [collection=$collectionName, query=$id]")
    collection.find(Map("_id" -> id)).one[T]
  }

  def update(id: String, updates: JsValue): Future[Try[Unit]] = {
    Logger.debug(s"update documents: [collection=$collectionName, query=$id][updates=$updates]")
    collection.update(Map("_id" -> id), Map("$set" -> updates)) flatMap asTryUnit
  }

  def delete(id: String): Future[Try[Unit]] = {
    Logger.debug(s"delete documents: [collection=$collectionName, query=$id]")
    collection.remove(Map("_id" -> id)) flatMap asTryUnit
  }

  def findAll()(implicit readsT: Reads[T]): Future[List[T]] = {
    Logger.debug(s"findAll documents: [collection=$collectionName]")
    implicit val myWrites: OWrites[Unit] = new OWrites[Unit] {
      def writes(a: Unit) = Json.obj()
    }
    collection.find(()).cursor[T]().collect[List]()
  }
  
  def findProjection(selector: JsObject, projection: JsObject)(implicit readsT: Reads[T]): Future[List[T]] = {
    val cursor: Cursor[T] = collection
      .find(selector, projection).cursor[T]()
    cursor.collect[List]()
  }
  
  def findSimpleProjection(selector: JsObject, projection: JsObject)(implicit readsT: Reads[T]): Future[Option[T]] = {
    collection.find(selector, projection).one[T]
  }

  private def asTryUnit(writeResult: WriteResult): Future[Try[Unit]] = Future {
    if (writeResult.ok) {
      Success(())
    } else {
      Failure(RemoveFailed(writeResult.errmsg.get))
    }
  }
}

object BasicCrudDAO {

  case class SavingFailed(msg: String = "") extends RuntimeException(msg)

  case class RemoveFailed(msg: String = "") extends RuntimeException(msg)

}
  
