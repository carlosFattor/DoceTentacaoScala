package models

import akka.actor.Actor
import akka.actor.Props
import javax.inject.Inject
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms
import play.api.libs.concurrent.Akka
import play.api.libs.json.Json
import play.api.libs.mailer.Email
import play.api.libs.mailer.MailerClient

class EmailActor @Inject() (mailer: MailerClient) {

  def sendEmail(m: EmailMessage, body: String) {
    val email2Contact = Email(
      m.subject,
      "Nilda Doce Tentação <carlos.fattor@gmail.com>",
      Seq(m.to),
      bodyText = Some(body),
      bodyHtml = Some(body))
    mailer.send(email2Contact)
  }

  def sendEmailAdmin(m: EmailMessage, body: String) {
    val email2Contact = Email(
      m.subject,
      "Nilda Doce Tentação <carlos.fattor@gmail.com>",
      Seq("carlos.fattor@gmail.com"),
      bodyText = Some(body),
      bodyHtml = Some(body))
    mailer.send(email2Contact)
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