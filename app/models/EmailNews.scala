package models

import java.time.LocalDate

/**
 * @author carlos
 */
case class EmailNews (
    _id: Option[String],
    newsEmail: Option[String],
    date: LocalDate = LocalDate.now()
)

object EmailNews {
  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._
  import play.modules.reactivemongo.json.BSONFormats._
  
  implicit val jsonFormat = Json.format[EmailNews]
}