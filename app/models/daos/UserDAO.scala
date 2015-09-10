package models.daos

import models.User
import models.daos.traits.core.CrudDAO
import play.api.libs.json.Reads
import scala.concurrent.Future

trait UserDAO extends CrudDAO[User] {
  def findByType(field: String, value: String)(implicit readsT : Reads[User]): Future[Option[User]]
}