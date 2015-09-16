package controllers

import java.util.concurrent.TimeoutException

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

import javax.inject.Inject
import javax.inject.Singleton
import models.Gallery
import models.services.traits.GalleryService
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.api.mvc.Action
import play.api.mvc.Controller
import reactivemongo.bson.BSONObjectID

/**
 * @author carlos
 */
@Singleton
class GalleryControl @Inject() (galService: GalleryService, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  implicit val timeout = 10.seconds

  def gallery = Action.async { implicit request =>

    val galls = galService.findListGall()
    galls.map {
      gall =>
        Ok(views.html.gallery.list_gallery(gall))
    }.recover {
      case t: TimeoutException =>
        Logger.error("Problem found in gallery list process")
        InternalServerError(t.getMessage)
    }
  }

  def galleryManager = Authenticated.async { implicit request =>

    val galls = galService.findListGall()
    galls.map {
      gall =>
        Ok(views.html.manager.gallery.list_gallery(gall))
    }.recover {
      case t: TimeoutException =>
        Logger.error("Problem found in gallery list process")
        InternalServerError(t.getMessage)
    }
  }

  def add = Authenticated.async { implicit request =>
    Gallery.formGall.bindFromRequest.fold(
      formErr => Future.successful(Ok(views.html.manager.gallery.create_gallery(formErr)).flashing("fail" -> messagesApi("fail.add"))),
      data => {
        galService.find(data._id.getOrElse("")).flatMap {
          case Some(_) =>
            galService.updateGall(data).map {
              case Some(x) => Redirect(routes.GalleryControl.galleryManager()).flashing("success" -> messagesApi("success.update"))
              case None    => Redirect(routes.GalleryControl.galleryManager()).flashing("fail" -> messagesApi("fail.update"))
            }

          case None =>
            val gall = Gallery(
              _id = Some(BSONObjectID.generate.stringify),
              galName = Option.apply(data.galName).orNull,
              galDesc = data.galDesc,
              galURLSmall = data.galURLSmall,
              galURLLarge = data.galURLLarge)
            galService.addGall(gall)
            Future.successful(Redirect(routes.GalleryControl.galleryManager()).flashing("success" -> messagesApi("success.add")))
        }
      }).recover {
        case t: TimeoutException =>
          Logger.error("Problem adding in gallery list process")
          InternalServerError(t.getMessage)
      }
  }

  def edit(id: String) = Authenticated.async { implicit request =>
    galService.find(id).map {
      case Some(gall) => Ok(views.html.manager.gallery.create_gallery(Gallery.formGall.fill(gall)))
      case None       => Redirect(routes.GalleryControl.galleryManager())
    }
  }

  def remove(id: String) = Authenticated.async { implicit request =>
    galService.removeGall(id).map {
      case Some(_) => Redirect(routes.GalleryControl.galleryManager()).flashing("success" -> messagesApi("success.remove"))
      case None    => Redirect(routes.GalleryControl.galleryManager()).flashing("fail" -> messagesApi("fail.update"))
    }
  }

}