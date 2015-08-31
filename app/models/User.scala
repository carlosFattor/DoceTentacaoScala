package models

import java.util.UUID
import play.api.libs.json.Json


/**
 * @author carlos
 */
case class User(
    userId: UUID,
    firstName: Option[String],
    lastName: Option[String],
    email: Option[String],
    password: Option[String],
    avatarURL: Option[String]
    )
    
object User {
  implicit val jsonFormat = Json.format[User]
}