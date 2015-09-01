package models.services

import scala.concurrent.Future
import models.Gallery
import com.google.inject.ImplementedBy

/**
 * @author carlos
 */
trait GalleryService {
  
  def findListGall(): Future[List[Gallery]]
  
  def addGall(gall: Gallery): Future[Gallery]
  
}