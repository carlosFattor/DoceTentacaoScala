package models

import play.api.data.Form
import play.api.data.Forms._

object MyForms {
  val productFormTuple = Form(
    tuple(
      "_id" -> optional(text),
      "prodName" -> nonEmptyText,
      "prodDesc" -> nonEmptyText,
      "prodImgSmallURL" -> nonEmptyText,
      "prodImgLargeURL" -> nonEmptyText,
      "prodCommentURL" -> nonEmptyText,
      "_idCat" -> nonEmptyText,
      "prodFeature" -> optional(boolean)))
}