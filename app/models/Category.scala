package models

/**
 * @author carlos
 */
case class Category(
  _id: Option[String],
  catName: String,
  catURL: String,
  catDesc: String,
  products: Option[List[Product]])

object Category {
  
  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._
  import play.modules.reactivemongo.json.BSONFormats._
  
  implicit val jsonCategoryFormat = Json.format[Category]

  val formGall = Form(
    mapping(
      "_id" -> optional(text),
      "catName" -> nonEmptyText,
      "catURL" -> nonEmptyText,
      "catDesc" -> nonEmptyText,
      "products" -> optional(list(mapping(
        "_id" -> optional(text),
        "prodName" -> nonEmptyText,
        "prodDesc" -> nonEmptyText,
        "prodImgSmallURL" -> nonEmptyText,
        "prodImgLargeURL" -> nonEmptyText,
        "prodCommentURL" -> optional(text),
        "prodFeatured" -> optional(text))(Product.apply)(Product.unapply))))(Category.apply)(Category.unapply))
}