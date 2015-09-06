package models.daos

import models.User
import models.daos.traits.core.CrudDAO

trait UserDAO extends CrudDAO[User]