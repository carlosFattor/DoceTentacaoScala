package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import javax.inject.Inject
import javax.inject.Singleton
import models.Gallery
import models.services.GalleryServiceImp
import play.api.mvc.Action
import play.api.mvc.Controller
import reactivemongo.bson.BSONObjectID
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
/**
 * @author carlos
 */
@Singleton
class GalleryControl @Inject()(val galService: GalleryServiceImp) extends Controller {
  
  def gallery = Action.async {
    
    val galls = galService.findListGall()
    galls.map { 
      gall =>
        Ok(views.html.gallery.list_gallery(gall))
    }
  }
  
  
  def add = Action.async { implicit request =>
    Gallery.formGall.bindFromRequest.fold(
        form => Future.successful(BadRequest(views.html.gallery.create_gallery(form))),
        data => {
          val gall = Gallery(
              id = Some(BSONObjectID.generate.stringify),
              galName = Option.apply(data.galName).orNull,
              galDesc = data.galDesc,
              galURLSmall = data.galURLSmall,
              galURLLarge = data.galURLLarge
          )
          for {
            gall <- galService.addGall(gall)
          }yield {
            Redirect(routes.GalleryControl.gallery())
          }
        }
    )
  }
}