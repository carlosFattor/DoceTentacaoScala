package controllers

import play.api._
import play.api.mvc._
import play.api.i18n.{ I18nSupport, MessagesApi, Messages, Lang }
import javax.inject.Inject
import models.services.CategoryService
import scala.concurrent.ExecutionContext.Implicits._
import play.api.cache.Cached
import scala.concurrent.duration._

class Application @Inject() (catService: CategoryService, val messagesApi: MessagesApi, cached: Cached) extends Controller with I18nSupport {

  val msgIndex = "Nilda Doce Tentação"
  implicit val timeout = 10.minute

  def index = cached("index").default(timeout) {
    Action.async { implicit request =>
      val prods = catService.findListCategory().map {
        cat =>
          cat.filter {
            cp => cp.products.isDefined
          }.flatMap { prod => prod.products.get }
      }
      prods.map { prod =>
        prod.filter { p => p.prodFeature == true }
        Ok(views.html.index(prod))
      }
    }
    
  }

  def viewLogin = Action {
    Ok(views.html.manager.loginView())
  }

  def indexManager = Authenticated { implicit request =>
    Ok(views.html.manager.home())
  }
}
