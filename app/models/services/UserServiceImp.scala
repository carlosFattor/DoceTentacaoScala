package models.services

import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success
import javax.inject.Inject
import models.User
import models.daos.UserDAO
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

class UserServiceImp @Inject() (userDao: UserDAO) extends UserService {

  val collectionName = "users"

  def findListUser(): Future[List[User]] = userDao.findAll()

  def addUser(user: User): Future[Option[User]] = {
    val userComp = user.copy(_id = Some(BSONObjectID.generate.stringify), active = Some(true))
    userDao.create(userComp).map {
      case Success(user) => Some(userComp)
      case Failure(e)    => None
    }
  }
  def findUser(id: String): Future[Option[User]] = userDao.read(id)

  def updateUSer(user: User): Future[Option[User]] = {
    userDao.update(user._id.get, Json.toJson(user)).map {
      case Success(resp) => Some(user)
      case Failure(e)    => None
    }
  }
  
  def removeUser(id: String): Future[Option[Boolean]] = {
    userDao.delete(id).map { 
      case Success(resp) => Some(true) 
      case Failure(e) => None  
    }
  }
  
  def findUserByEmail(email: String): Future[Option[User]] = {
    userDao.findByType("email", email)
  }

}

trait UserService {
  def findListUser(): Future[List[User]]
  def addUser(user: User): Future[Option[User]]
  def findUser(id: String): Future[Option[User]]
  def updateUSer(user: User): Future[Option[User]]
  def removeUser(id: String): Future[Option[Boolean]]
  def findUserByEmail(email: String): Future[Option[User]]
}