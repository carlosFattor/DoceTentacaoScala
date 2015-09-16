package controllers

import java.util.concurrent.TimeoutException

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

import javax.inject.Inject
import models.Category
import models.services.traits.CategoryService
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.api.mvc.Action
import play.api.mvc.Controller
import reactivemongo.bson.BSONObjectID

class CategoryControl @Inject() (catService: CategoryService, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  implicit val timeout = 10.seconds

  def category = Authenticated.async { implicit request =>
    val cats = catService.findSimplesCategories(new String)
    cats.map {
      cat => Ok(views.html.category.list_category(cat))
    }.recover {
      case t: TimeoutException =>
        Logger.error("Problem adding in Category list process")
        InternalServerError(t.getMessage)
    }
  }

  def categoryManager = Authenticated.async { implicit request =>
    val cats = catService.findSimplesCategories(new String)
    cats.map {
      cat => Ok(views.html.manager.category.list_category(cat))
    }.recover {
      case t: TimeoutException =>
        Logger.error("Problem adding in Category list process")
        InternalServerError(t.getMessage)
    }
  }

  def add = Authenticated.async { implicit request =>
    Category.formGall.bindFromRequest.fold(
      error => Future.successful(Ok(views.html.manager.category.create_category(error)).flashing("success" -> messagesApi("fail.add"))),
      data => {
        catService.findOneCategory(data._id.getOrElse("")).map {
          case Some(cat) => {
            catService.updateCategory(data)
            Redirect(routes.CategoryControl.categoryManager()).flashing("success" -> messagesApi("success.update"))
          }
          case None => {
            val cat = data.copy(_id = Some(BSONObjectID.generate.stringify))
            catService.addCategory(cat)
            Redirect(routes.CategoryControl.categoryManager()).flashing("success" -> messagesApi("success.add"))
          }
        }
      }).recover {
        case t: TimeoutException =>
          Logger.error("Problem adding in Category list process")
          InternalServerError(t.getMessage)
      }
  }

  def edit(id: String) = Authenticated.async { implicit request =>
    catService.findOneCategory(id).map {
      case Some(cat) => Ok(views.html.manager.category.create_category(Category.formGall.fill(cat)))
      case None      => Redirect(routes.CategoryControl.categoryManager())
    }.recover {
        case t: TimeoutException =>
          Logger.error("Problem adding in Category list process")
          InternalServerError(t.getMessage)
      }
  }

  def remove(id: String) = Authenticated.async { implicit request =>
    catService.removeCategory(id).map {
      case Some(ok) => Redirect(routes.CategoryControl.categoryManager()).flashing("success" -> messagesApi("success.remove"))
      case None     => Redirect(routes.CategoryControl.categoryManager()).flashing("fail" -> messagesApi("fail.remove.category"))
    }.recover {
        case t: TimeoutException =>
          Logger.error("Problem adding in Category list process")
          InternalServerError(t.getMessage)
      }
  }

}