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
class GalleryServiceImp @Inject() (galleryDao: GalleryDAO) extends GalleryService {

  def findListGall(): Future[List[Gallery]] = galleryDao.find()

  def find(id: String): Future[Option[Gallery]] = galleryDao.find(id)

  def addGall(gall: Gallery): Future[Option[Gallery]] = {
    galleryDao.add(gall).map{
      case Right(g) => Some(g)
      case Left(err) => None
    }
  }
  
  def updateGall(gall: Gallery): Future[Option[Gallery]] = {
    galleryDao.update(gall).map{
      case Right(g) => Some(g)
      case Left(err) => None
    }
  }

}