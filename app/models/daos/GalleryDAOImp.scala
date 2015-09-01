package models.daos

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import javax.inject._
import models.Gallery
import play.api.libs.json._
import play.api.inject._
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api._
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONObjectID


/**
 * @import models.daos.GalleryDAO
author carlos
 */
@Singleton
class GalleryDAOImp @Inject()(db: DB) extends GalleryDAO{
  
  def collection: JSONCollection = db.collection[JSONCollection]("gallery")
  
  def find(): Future[List[Gallery]] = {
    val query = BSONDocument()
    collection.find(query).cursor[Gallery].collect[List]()
  }
  
  def add(gall: Gallery): Future[Gallery] = {
    collection.insert(gall).map {
      _ => gall
    }
  }
}