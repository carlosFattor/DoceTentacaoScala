package models

import java.util.UUID
import play.api.libs.json.Json

/**
 * @author carlos
 */
case class Category(
    catID: UUID,
    catName: Option[String],
    catURL: Option[String],
    catDesc: Option[String],
    products: Option[List[Product]]
)

object Category {
  implicit val jsonFormat= Json.format[Category]
}