package models

import java.util.UUID
import play.api.libs.json.Json

/**
 * @author carlos
 */
case class Gallery(
    galID: UUID,
    galName: Option[String],
    galDesc: Option[String],
    galURLSmall: Option[String],
    galURLLarge: Option[String]
)

object Gallery {
  implicit val jsonFormat = Json.format[Gallery]
}