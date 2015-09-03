package models.daos

import scala.concurrent.Future

import models.Gallery
import play.libs.Json

/**
 * @author carlos
 */
trait GalleryDAO {
  def add(gall: Gallery): Future[Either[Exception, Gallery]]
  def remove(gallId: String): Future[Either[Exception, Boolean]] //Either -> A common use of Either is as an alternative to scala.Option for dealing with possible missing values. In this usage, scala.None is replaced with a scala.util.Left qual can contain useful information. scala.util.Right takes the place of scala.Some
  def find(): Future[List[Gallery]]
  def find(id: String): Future[Option[Gallery]]
  def findByCriteria(criteria: Map[String, Any], limit: Int): Future[Traversable[Gallery]]
  def update(gall: Gallery): Future[Either[Exception, Gallery]]
}