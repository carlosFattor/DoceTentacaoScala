package controllers

import play.api._
import play.api.mvc._
import play.api.i18n.{ I18nSupport, MessagesApi, Messages, Lang }
import javax.inject.Inject

class Application @Inject()(val messagesApi: MessagesApi)extends Controller with I18nSupport {

  val msgIndex = "Nilda Doce Tentação"
  
  def index = Action { implicit request =>
    Ok(views.html.index())
  }
}
