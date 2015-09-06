package models.daos

import javax.inject.Inject
import javax.inject.Singleton
import models.User
import models.daos.traits.core.BasicCrudDAO
import play.api.libs.json.Reads
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.DB


@Singleton
class UserDAOImp @Inject()(db: DB) extends BasicCrudDAO[User](db, "users") with UserDAO{

}
