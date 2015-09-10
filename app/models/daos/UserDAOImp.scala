package models.daos

import javax.inject.Inject
import javax.inject.Singleton
import models.User
import models.daos.traits.core.BasicCrudDAO
import play.api.libs.json.Reads
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.DB
import play.api.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserDAOImp @Inject()(db: DB) extends BasicCrudDAO[User](db, "users") with UserDAO{

  def findByType(field: String, value: String)(implicit readsT : Reads[User]): Future[Option[User]] = {
    Logger.debug(s"reading documents: [collection=users, query=Field=> $field + value=>  $value]")
    collection.find(Map(field -> value)).one[User]
  }  
}
