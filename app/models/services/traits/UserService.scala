package models.services.traits

import scala.concurrent.Future

import models.User

trait UserService {
  def findListUser(): Future[List[User]]
  def addUser(user: User): Future[Option[User]]
  def findUser(id: String): Future[Option[User]]
  def updateUSer(user: User): Future[Option[User]]
  def removeUser(id: String): Future[Option[Boolean]]
}