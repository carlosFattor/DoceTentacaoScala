package models.daos.traits.core

import play.api.libs.json.JsValue
import scala.concurrent.Future
import scala.util.Try
import play.api.libs.json.OWrites
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json.Reads
import play.api.libs.json.JsObject
import play.api.libs.json.Writes
import play.api.libs.json.Json

trait CrudDAO[T] {
  
  def create(entity: T)(implicit tjs: OWrites[T]): Future[Try[String]]

  def read(id: String)(implicit readsT : Reads[T]): Future[Option[T]]

  def delete(id: String): Future[Try[Unit]]

  def update(id: String, updates: JsValue): Future[Try[Unit]]

  def findAll()(implicit readsT : Reads[T]): Future[List[T]]
  
  def findProjection(selector: JsObject, projection: JsObject)(implicit readsT: Reads[T]): Future[List[T]]
  
  def findSimpleProjection(selector: JsObject, projection: JsObject)(implicit readsT: Reads[T]): Future[Option[T]]
  
}