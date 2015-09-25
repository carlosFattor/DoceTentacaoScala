package controllers

import scala.concurrent.Future

import javax.inject.Inject
import models.EmailActor
import models.MailToContact
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc.Action
import play.api.mvc.Controller

class ContactControl @Inject() (emailActor: EmailActor, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def knowMore = Action { implicit request =>
    Ok(views.html.contact.learn())
  }

  def create = Action { implicit request =>
    Ok(views.html.contact.contact_send(MailToContact.contactFormValidation.fill(MailToContact("", "", Some(""), ""))))
  }

  def doCreate = Action.async { implicit request =>
    MailToContact.contactFormValidation.bindFromRequest().fold(
      hasErrors => Future.successful(BadRequest("").flashing("fail" -> "email.send.fail")),
      data => {
        Future {
          emailActor.sendEmail(data, views.html.email.email_confirm(data).body)
          emailActor.sendEmailAdmin(data, views.html.email.email_contact(data).body)  
        }
        Future.successful(Redirect(routes.ContactControl.create).flashing("success" -> messagesApi("email.send")))
      })
  }
}