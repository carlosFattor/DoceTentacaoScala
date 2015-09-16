package models.services.traits

import scala.concurrent.Future

import models.Category
import models.Product

trait CategoryService {
  //Categories
  def addCategory(cat: Category): Future[Option[Category]]
  def findListCategory(): Future[List[Category]]
  def findSimplesCategories(id: String): Future[List[Category]]
  def findOneCategory(id: String): Future[Option[Category]]
  def updateCategory(cat: Category): Future[Option[Category]]
  def removeCategory(_id: String): Future[Option[Boolean]]
  
  //Products
  def findProduts: Future[List[Category]]
  def addProduct(prod: Product, cat: Category): Future[Option[Boolean]]
  def findOneProduct(idCat: String, idProd: String): Future[Option[Product]]
  def updateProduct(prod: Product, cat: Category): Future[Option[Category]]
  def removeProduct(idCat: String, idProd: String): Future[Option[Boolean]]
}