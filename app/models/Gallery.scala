package models



/**
 * @author carlos
 */
case class Gallery(
  _id: Option[String],
  galName: String,
  galDesc: Option[String],
  galURLSmall: Option[String],
  galURLLarge: Option[String])

object Gallery {
  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._
  import play.modules.reactivemongo.json.BSONFormats._
  
  implicit val galJsonFormat = Json.format[Gallery]

  val formGall = Form(
    mapping(
      "_id" -> optional(text),
      "galName" -> nonEmptyText(minLength=4),
      "galDesc" -> optional(text),
      "galURLSmall" -> optional(text),
      "galURLLarge" -> optional(text))(Gallery.apply)(Gallery.unapply))
}