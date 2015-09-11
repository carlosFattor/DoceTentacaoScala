package models


/**
 * @author carlos
 */
case class Contact(
    _id: Option[String],
    contName: Option[String],
    contEmail: Option[String],
    contWebSite: Option[String],
    contSend: Option[Boolean],
    contTextEmail: Option[String]
)

object Contact {
  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._
  import play.modules.reactivemongo.json.BSONFormats._
  
  implicit val jsonFormat = Json.format[Contact]
}