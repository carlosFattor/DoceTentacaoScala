package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import javax.inject.Inject
import javax.inject.Singleton
import models.Gallery
import models.services._
import play.api.mvc.Action
import play.api.mvc.Controller
import reactivemongo.bson.BSONObjectID
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.libs.json._
import models.services.GalleryService

/**
 * @author carlos
 */
@Singleton
class GalleryControl @Inject() (galService: GalleryService) extends Controller {

  def gallery = Action.async {

    val galls = galService.findListGall()
    galls.map {
      gall =>
        Ok(views.html.gallery.list_gallery(gall))
    }
  }

  def add = Action.async { implicit request =>
    Gallery.formGall.bindFromRequest.fold(
      formErr => Future.successful(BadRequest(views.html.gallery.create_gallery(formErr))),
      data => {
        galService.find(data._id.getOrElse("")).flatMap {

          case Some(_) =>
            galService.updateGall(data).map {
              case Some(x) => Redirect(routes.GalleryControl.gallery())
              case None    => Redirect(routes.GalleryControl.gallery())
            }

          case None =>
            val gall = Gallery(
              _id = Some(BSONObjectID.generate.stringify),
              galName = Option.apply(data.galName).orNull,
              galDesc = data.galDesc,
              galURLSmall = data.galURLSmall,
              galURLLarge = data.galURLLarge)
            galService.addGall(gall)
            Future.successful(Redirect(routes.GalleryControl.gallery()))
        }
      })
  }

  def edit(id: String) = Action.async { implicit request =>
    galService.find(id).map {
      case Some(gall) => Ok(views.html.gallery.create_gallery(Gallery.formGall.fill(gall)))
      case None => Redirect(routes.GalleryControl.gallery())
    }
  }
  
  def remove(id: String) = Action.async { implicit request =>
    galService.removeGall(id).map {
      case Some(_) => Redirect(routes.GalleryControl.gallery())
      case None => Redirect(routes.GalleryControl.gallery())
    }
  }
  
}