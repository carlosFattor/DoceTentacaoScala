package models.daos

import scala.concurrent.Future
import scala.util.Try
import models.Category
import models.daos.traits.core.CrudDAO
import play.api.libs.json.JsValue
import play.api.libs.json.JsObject

trait CategoryDAO extends CrudDAO[Category]{
  def removeProduct(idCat: String, idProd: String): Future[Try[Unit]]
}