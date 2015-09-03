package models.services

import scala.concurrent.Future
import models.Gallery
import com.google.inject.ImplementedBy

/**
 * @author carlos
 */
trait GalleryService {
  
  def findListGall(): Future[List[Gallery]]
  def find(id: String): Future[Option[Gallery]]  
  def addGall(gall: Gallery): Future[Option[Gallery]]
  def updateGall(gall: Gallery): Future[Option[Gallery]]
}