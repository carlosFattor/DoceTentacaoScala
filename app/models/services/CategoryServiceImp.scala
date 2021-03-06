package models.services

import scala.concurrent.Future
import scala.math.BigDecimal.int2bigDecimal
import scala.util.Failure
import scala.util.Success
import javax.inject.Inject
import models.Category
import models.Product
import models._
import models.daos.CategoryDAO
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Format
import play.api.libs.json.JsArray
import play.api.libs.json.JsNumber
import play.api.libs.json.JsResult
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import reactivemongo.bson.BSONObjectID

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

    if (!id.isEmpty()) {
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

  def removeCategory(_id: String): Future[Option[Boolean]] = {
    catDao.delete(_id).map {
      case Success(a) => Some(true)
      case Failure(e) => None
    }
  }

  /**
   * Products
   */
  def findProduts: Future[List[Category]] = catDao.findAll()

  def addProduct(prod: Product, cat: Category): Future[Option[Boolean]] = {
    var prodMutable = scala.collection.mutable.Seq[models.Product]()
    if (!cat.products.isEmpty) {
      prodMutable = scala.collection.mutable.ArraySeq(cat.products.get.toSeq: _*).+:(prod)
    } else {
      prodMutable = scala.collection.mutable.ArraySeq(prod)
    }

    val catComplet = cat.copy(products = Some(prodMutable.toList))
    this.updateCategory(catComplet).map {
      case Some(_) => Some(true)
      case _       => None
    }
  }

  def updateProduct(prod: Product, cat: Category): Future[Option[Category]] = {
    val prods = cat.products.get.filter { p => p._id.get != prod._id.get }.+:(prod)
    val newCat = Category(cat._id, cat.catName, cat.catURL, cat.catDesc, Some(prods.toList))
    this.updateCategory(newCat)
  }

  def findOneProduct(idCat: String, idProd: String): Future[Option[Product]] = {
    catDao.read(idCat).map {
      case Some(cat) => {
        cat.products.get.find { p => p._id.get == idProd }
      }
      case _ => None
    }
  }

  def removeProduct(idCat: String, idProd: String): Future[Option[Boolean]] = {
    catDao.removeProduct(idCat, idProd).map {
      case Success(a) => Some(true)
      case Failure(e) => None
    }
  }

  /**
   * Implicits
   */
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

trait CategoryService {
  //Categories
  def addCategory(cat: Category): Future[Option[Category]]
  def findListCategory(): Future[List[Category]]
  def findSimplesCategories(id: String): Future[List[Category]]
  def findOneCategory(id: String): Future[Option[Category]]
  def updateCategory(cat: Category): Future[Option[Category]]
  def removeCategory(_id: String): Future[Option[Boolean]]
  
  //Products
  def findProduts: Future[List[Category]]
  def addProduct(prod: Product, cat: Category): Future[Option[Boolean]]
  def findOneProduct(idCat: String, idProd: String): Future[Option[Product]]
  def updateProduct(prod: Product, cat: Category): Future[Option[Category]]
  def removeProduct(idCat: String, idProd: String): Future[Option[Boolean]]
}