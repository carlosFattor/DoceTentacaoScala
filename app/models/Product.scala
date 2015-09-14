package models

import play.api.data.Form
import play.api.data.Forms
import play.modules.reactivemongo.json.BSONFormats


/**
 * @author carlos
 */
case class Product(
    _id: Option[String],
    prodName: String,
    prodDesc: String,
    prodImgSmallURL: String,
    prodImgLargeURL: String,
    prodCommentURL: String
    
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
      "prodCommentURL" -> nonEmptyText)(Product.apply)(Product.unapply))
}