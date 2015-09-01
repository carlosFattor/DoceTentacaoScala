package models

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.optional
import play.api.data.Forms._
import play.api.libs.json.Json

/**
 * @author carlos
 */
case class Gallery(
  id: Option[String],
  galName: String,
  galDesc: Option[String],
  galURLSmall: Option[String],
  galURLLarge: Option[String])

object Gallery {
  implicit val galJsonFormat = Json.format[Gallery]

  val formGall = Form(
    mapping(
      "id" -> optional(text),
      "galName" -> nonEmptyText(minLength=4),
      "galDesc" -> optional(text),
      "galURLSmall" -> optional(text),
      "galURLLarge" -> optional(text))(Gallery.apply)(Gallery.unapply))
}