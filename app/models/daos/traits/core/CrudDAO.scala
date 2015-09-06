package models.daos.traits.core

import play.api.libs.json.JsValue
import scala.concurrent.Future
import scala.util.Try
import play.api.libs.json.OWrites
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json.Reads

trait CrudDAO[T] {
  
  def create(entity: T)(implicit tjs: OWrites[T]): Future[Try[String]]

  def read(id: String)(implicit readsT : Reads[T]): Future[Option[T]]

  def delete(id: String): Future[Try[Unit]]

  def update(id: String, updates: JsValue): Future[Try[Unit]]

  def findAll()(implicit readsT : Reads[T]): Future[List[T]]
  
}