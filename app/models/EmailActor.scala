package models

import akka.actor._
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.mailer._
import play.api.Play.current
import java.io.File
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi

object EmailActor {
  val actor = Akka.system.actorOf(Props[EmailActor])
}

class EmailActor @Inject()(mailer: MailerClient) extends Actor {

  def receive = {
    case m: MailToContact    => sendEmail(m, views.html.email.email_contact(m).body)
    case m: MailToNewsLetter => sendEmail(m, "")
  }

  def sendEmail(m: EmailMessage, body: String) {
    val email = Email(
      m.subject,
      "Nilda Doce Tentação <carlos.fattor@gmail.com>",
      Seq(m.to),
      bodyText = Some(body),
      bodyHtml = Some(body))
    mailer.send(email)
  }

}

sealed trait EmailMessage {
  val name: String
  val to: String
  val site: Option[String]
  val comment: String
  val subject: String
}

case class MailToContact(name: String, to: String, site: Option[String], comment: String) extends EmailMessage {
  val subject = "Email de contato"
}

object MailToContact {
  import play.api.data.Forms._

  implicit val jsonFormat = Json.format[MailToContact]

  val contactFormValidation = Form(
    mapping(
      "name" -> nonEmptyText,
      "to" -> nonEmptyText,
      "site" -> optional(text),
      "comment" -> nonEmptyText) {
        (name, to, site, comment) =>
          MailToContact(
            name,
            to,
            site,
            comment)
      } {
        mailToContact =>
          Some(
            mailToContact.name,
            mailToContact.to,
            mailToContact.site,
            mailToContact.comment)
      })
}

case class MailToNewsLetter(to: String) extends EmailMessage {
  val subject = "Email de new letter"
  val name: String = ""
  val site: Option[String] = Some("")
  val comment: String = ""
}

object MailToNewsLetter {
  implicit val jsonFormat = Json.format[MailToNewsLetter]
}