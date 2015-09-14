package models

import play.api.data.Form
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.optional
import play.api.data.Forms.text
import play.api.data.Forms.tuple

object MyForms {
  val productFormTuple = Form(
    tuple(
      "_id" -> optional(text),
      "prodName" -> nonEmptyText(minLength = 4),
      "prodDesc" -> nonEmptyText,
      "prodImgSmallURL" -> nonEmptyText,
      "prodImgLargeURL" -> nonEmptyText,
      "prodCommentURL" -> nonEmptyText,
      "_idCat" -> nonEmptyText))
}