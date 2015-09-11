package controllers

import play.api.i18n.{ I18nSupport, MessagesApi, Messages, Lang }
import javax.inject.Inject
import play.api.mvc._

class ContactControl @Inject()(val messagesApi: MessagesApi)extends Controller with I18nSupport {
  
  def knowMore = Action {implicit request=>
    Ok(views.html.contact.learn())
  }
}