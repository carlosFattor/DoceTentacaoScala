package models

import java.util.UUID
import java.time.LocalDate
import play.api.libs.json.Json

/**
 * @author carlos
 */
case class EmailNews (
    newsID: UUID,
    newsEmail: Option[String],
    date: LocalDate = LocalDate.now()
)

object EmailNews {
  implicit val jsonFormat = Json.format[EmailNews]
}