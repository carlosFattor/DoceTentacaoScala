package models.daos

import scala.Left
import scala.Right
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.math.BigDecimal.double2bigDecimal
import scala.math.BigDecimal.int2bigDecimal
import scala.math.BigDecimal.long2bigDecimal

import javax.inject.Inject
import javax.inject.Singleton
import models.Gallery
import play.api.libs.json.JsArray
import play.api.libs.json.JsBoolean
import play.api.libs.json.JsNull
import play.api.libs.json.JsNumber
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.libs.json.Writes
import play.modules.reactivemongo.json.JsObjectDocumentWriter
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.DB
import reactivemongo.api.ReadPreference

/**
 * @import models.daos.GalleryDAO
 * author carlos
 */
@Singleton
class GalleryDAOImp @Inject() (db: DB) extends GalleryDAO {

  def collection: JSONCollection = db.collection[JSONCollection]("gallery")

  def find(): Future[List[Gallery]] = {
    val query = Json.obj()
      Some(collection.find(query).cursor[Gallery]().collect[List]()).get      
  }

  def find(id: String): Future[Option[Gallery]] = {
    val query = Json.obj("id" -> id)
    collection.find(query).one[Gallery] map {
      case Some(x) => Option.apply(x)
      case _       => None
    }
  }

  def findByCriteria(criteria: Map[String, Any], limit: Int): Future[Traversable[Gallery]] =
    findByCriteria(CriteriaJSONWriter.writes(criteria), limit)

  private def findByCriteria(criteria: JsObject, limit: Int): Future[Traversable[Gallery]] =
    collection.
      find(criteria).
      cursor[Gallery](readPreference = ReadPreference.primary).
      collect[List](limit)

  def add(gall: Gallery): Future[Either[Exception, Gallery]] = {
    collection.insert(gall).map {
      case le if le.ok == true => Right(gall)
      case le                  => Left(le)
    }
  }

  def remove(gallId: String): Future[Either[Exception, Boolean]] = {
    val query = Json.obj("id" -> gallId)
    collection.remove(query, firstMatchOnly = true).map {
      case le if le.ok == true => Right(le.ok)
      case le                  => Left(le)
    }
  }

  def update(gall: Gallery): Future[Either[Exception, Gallery]] = {
    val query = Json.obj("id" -> gall.id)
    val modifier = Json.obj(
      "$set" -> Json.obj(
        "galName" -> gall.galName,
        "galDesc" -> gall.galDesc,
        "galURLSmall" -> gall.galURLSmall,
        "galURLLarge" -> gall.galURLLarge
        ))
        println(modifier.toString())
    collection.update(query, modifier).map {
      case le if le.ok == true => Right(gall)
      case le                  => Left(le)
    }
  }
}

object CriteriaJSONWriter extends Writes[Map[String, Any]] {
  override def writes(criteria: Map[String, Any]): JsObject = JsObject(criteria.mapValues(toJsValue(_)).toSeq)
  val toJsValue: PartialFunction[Any, JsValue] = {
    case v: String                        => JsString(v)
    case v: Int                           => JsNumber(v)
    case v: Long                          => JsNumber(v)
    case v: Double                        => JsNumber(v)
    case v: Boolean                       => JsBoolean(v)
    case obj: JsValue                     => obj
    case map: Map[String, Any] @unchecked => CriteriaJSONWriter.writes(map)
    case coll: Traversable[_]             => JsArray(coll.map(toJsValue(_)).toSeq)
    case null                             => JsNull
    case other                            => throw new IllegalArgumentException(s"Criteria value type not supported: $other")
  }
}