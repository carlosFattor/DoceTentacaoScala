package models.services

import models.services.traits.CategoryService
import scala.concurrent.Future
import models.Category
import javax.inject.Inject
import models.daos.CategoryDAO
import scala.util.Failure
import scala.util.Success
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONObjectID
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.libs.json.Json.JsValueWrapper

class CategoryServiceImp @Inject() (catDao: CategoryDAO) extends CategoryService {

  def findListCategory(): Future[List[Category]] = catDao.findAll()

  def addCategory(cat: Category): Future[Option[Category]] = {
    val catComp = cat.copy(_id = Some(BSONObjectID.generate.stringify))
    catDao.create(catComp).map {
      case Success(cat) => Some(catComp)
      case Failure(e)   => None
    }
  }

  def findSimplesCategories(id: String): Future[List[Category]] = {
    val simpleCategory = Json.obj("_id" -> 1, "catName" -> 1, "catURL" -> 1, "catDesc" -> 1)
    var selector = Json.obj()
    
    if(!id.isEmpty()){
      selector = Json.obj("_id" -> id)
    }
    
    catDao.findProjection(selector, simpleCategory)
  }

  def updateCategory(cat: Category): Future[Option[Category]] = {
    catDao.update(cat._id.get, Json.toJson(cat)).map {
      case Success(resp) => Some(cat)
      case _             => None
    }
  }

  def findOneCategory(id: String): Future[Option[Category]] = {
    catDao.read(id)
  }
  implicit val objectMapFormat = new Format[Map[String, Object]] {

    def writes(map: Map[String, Object]): JsValue =
      Json.obj(map.map {
        case (s, o) =>
          val ret: (String, JsValueWrapper) = o match {
            case _: String => s -> JsString(o.asInstanceOf[String])
            case _: Number => s -> JsNumber(o.asInstanceOf[Int])
            case _         => s -> JsArray(o.asInstanceOf[List[String]].map(JsString(_)))
          }
          ret
      }.toSeq: _*)

    def reads(jv: JsValue): JsResult[Map[String, Object]] =
      JsSuccess(jv.as[Map[String, JsValue]].map {
        case (k, v) =>
          k -> (v match {
            case s: JsString => s.as[String]
            case l           => l.as[List[String]]
          })
      })
  }
}