package models.daos

import javax.inject.Inject
import models.Category
import models.daos.traits.core.BasicCrudDAO
import reactivemongo.api.DB

class CategoryDAOImp @Inject() (db: DB) extends BasicCrudDAO[Category](db, "categories") with CategoryDAO {

}