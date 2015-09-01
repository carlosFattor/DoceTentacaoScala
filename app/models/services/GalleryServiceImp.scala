package models.services

import javax.inject._
import models.daos.GalleryDAO
import scala.concurrent.Future
import models.Gallery

import play.api.libs.concurrent.Execution.Implicits._

/**
 * @author carlos
 */
@Singleton
class GalleryServiceImp @Inject()(val galleryDao: GalleryDAO) extends GalleryService{
  
  def findListGall(): Future[List[Gallery]] = galleryDao.find()
  
   def addGall(gall: Gallery): Future[Gallery] = galleryDao.add(gall)
  
}