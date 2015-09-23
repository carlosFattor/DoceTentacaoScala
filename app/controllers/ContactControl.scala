package controllers

import akka.actor.ActorSystem
import javax.inject.Inject
import models.EmailActor
import models.MailToContact
import play.api.i18n.{Messages, I18nSupport, MessagesApi}
import play.api.mvc._
import akka.actor.Props
import scala.concurrent.Future

class ContactControl @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

  lazy val system = ActorSystem.create("EmailActor")
  
  lazy val emailSend = system.actorOf(Props[EmailActor])

  def knowMore = Action { implicit request =>
    Ok(views.html.contact.learn())
  }

  def create = Action { implicit request =>
    Ok(views.html.contact.contact_send(MailToContact.contactFormValidation.fill(MailToContact("","",Some(""),""))))
  }
  
  def contact = Action.async { implicit request =>
    MailToContact.contactFormValidation.bindFromRequest().fold(
      hasErrors => Future.successful(BadRequest("").flashing("fail" -> "email.send.fail")),
      data => {
        emailSend ! data
        Future.successful(Redirect(routes.ContactControl.create).flashing("success" -> "email.send"))
      })    
  }
}