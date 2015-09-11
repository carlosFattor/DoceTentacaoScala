package models.services.traits

import scala.concurrent.Future
import models.Category

trait CategoryService {
  def addCategory(cat: Category): Future[Option[Category]]
  def findListCategory(): Future[List[Category]]
  def findSimplesCategories(id: String): Future[List[Category]]
  def findOneCategory(id: String): Future[Option[Category]]
  def updateCategory(cat: Category): Future[Option[Category]]
}