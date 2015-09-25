package models.daos

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import javax.inject.Inject
import models.Category
import models.daos.traits.core.BasicCrudDAO
import play.api.Logger
import play.api.libs.json.JsObject
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.modules.reactivemongo.json.JsObjectDocumentWriter
import reactivemongo.api.DB
import reactivemongo.api.commands.WriteResult
import models.daos.traits.core.CrudDAO


class CategoryDAOImp @Inject() (db: DB) extends BasicCrudDAO[Category](db, "categories") with CategoryDAO {
  
  def removeProduct(idCat: String, idProd: String): Future[Try[Unit]] = {
    
    val search = Json.obj("_id" -> idCat, "products" -> Json.obj("$elemMatch" -> Json.obj("_id" -> idProd)))
    val pull = Json.obj("$pull" -> Json.obj("products" -> Json.obj("_id" -> idProd)))
    
    Logger.debug(s"update documents: [collection=categorie.products, query=search + $pull]")
    
    collection.update( search , pull) flatMap asTryUnit
  }
  
  private def asTryUnit(writeResult: WriteResult): Future[Try[Unit]] = Future {
    if (writeResult.ok) {
      Success(())
    } else {
      Failure((writeResult))
    }
  }
}

trait CategoryDAO extends CrudDAO[Category]{
  def removeProduct(idCat: String, idProd: String): Future[Try[Unit]]
}