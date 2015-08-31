package models

import java.util.UUID
import play.api.libs.json.Json

/**
 * @author carlos
 */
case class Product(
    prodID: UUID,
    prodName: Option[String],
    prodDesc: Option[String],
    prodImgSmallURL: Option[String],
    prodImgLargeURL: Option[String],
    prodCommentURL: Option[String],
    prodFeatured: Option[String]
)

object Product {
  implicit val jsonFormat = Json.format[Product]
}