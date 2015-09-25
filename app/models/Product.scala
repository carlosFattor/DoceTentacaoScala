package models

import play.api.data.Form


/**
 * @author carlos
 */
case class Product(
    _id: Option[String],
    prodName: String,
    prodDesc: String,
    prodImgSmallURL: String,
    prodImgLargeURL: String,
    prodCommentURL: String,
    prodFeature: Option[Boolean] = Some(false)
)

object Product {
  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._
  import play.modules.reactivemongo.json.BSONFormats._
  
  implicit val jsonProductFormat = Json.format[Product]
  
  val formProd = Form(
    mapping(
      "_id" -> optional(text),
      "prodName" -> nonEmptyText(minLength=4),
      "prodDesc" -> nonEmptyText,
      "prodImgSmallURL" -> nonEmptyText,
      "prodImgLargeURL" -> nonEmptyText,
      "prodCommentURL" -> nonEmptyText,
      "prodFeature" -> optional(boolean))(Product.apply)(Product.unapply))
}