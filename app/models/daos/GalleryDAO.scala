package models.daos

import scala.concurrent.Future
import models.Gallery

/**
 * @author carlos
 */
trait GalleryDAO {
  
  def find(): Future[List[Gallery]]  
  def add(gall: Gallery): Future[Gallery]
}