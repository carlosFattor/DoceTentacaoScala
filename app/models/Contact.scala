package models

import java.util.UUID
import play.api.libs.json.Json

/**
 * @author carlos
 */
case class Contact(
    contID: UUID,
    contName: Option[String],
    contEmail: Option[String],
    contWebSite: Option[String],
    contSend: Option[Boolean],
    contTextEmail: Option[String]
)

object Contact {
  implicit val jsonFormat = Json.format[Contact]
}