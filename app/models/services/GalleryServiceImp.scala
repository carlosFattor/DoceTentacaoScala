package models.services

import scala.concurrent.Future
import javax.inject.Inject
import javax.inject.Singleton
import models.Gallery
import models.daos.GalleryDAO
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import models.services.traits.GalleryService

/**
 * @author carlos
 */
@Singleton
class GalleryServiceImp @Inject() (galleryDao: GalleryDAO) extends GalleryService {

  def findListGall(): Future[List[Gallery]] = galleryDao.find()

  def find(id: String): Future[Option[Gallery]] = galleryDao.find(id)

  def addGall(gall: Gallery): Future[Option[Gallery]] = {
    galleryDao.add(gall).map {
      case Right(g)  => Some(g)
      case Left(err) => None
    }
  }

  def updateGall(gall: Gallery): Future[Option[Gallery]] = {
    galleryDao.update(gall).map {
      case Right(g)  => Some(g)
      case Left(err) => None
    }
  }

  def removeGall(id: String): Future[Option[Boolean]] = {
    galleryDao.remove(id).map {
      case Right(g)  => Some(g)
      case Left(err) => None
    }
  }

}