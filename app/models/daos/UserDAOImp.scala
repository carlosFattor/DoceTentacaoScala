package models.daos

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import javax.inject.Inject
import javax.inject.Singleton
import models.User
import models.daos.traits.core.BasicCrudDAO
import play.api.Logger
import play.api.libs.json.Reads
import reactivemongo.api.DB


@Singleton
class UserDAOImp @Inject()(db: DB) extends BasicCrudDAO[User](db, "users") with UserDAO{

  def findByType(field: String, value: String)(implicit readsT : Reads[User]): Future[Option[User]] = {
    Logger.debug(s"findByType documents: [collection=users, query=Field=> $field + value=>  $value]")
    collection.find(Map(field -> value)).one[User]
  }  
}
