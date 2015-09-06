package models

import play.api.data.Form
import play.api.data.Forms.boolean
import play.api.data.Forms.mapping
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.optional
import play.api.data.Forms.text
import play.api.data.validation.Constraints.pattern
import play.api.libs.json.Json


/**
 * @author carlos
 */
case class User(
  _id: Option[String],
  firstName: String,
  lastName: String,
  email: String,
  password: String,
  avatarURL: Option[String],
  active: Option[Boolean])

object User {
  implicit val userJsonFormat = Json.format[User]

  val userFormValidation = Form(
    mapping(
      "_id" -> optional(text verifying pattern(
        """[a-fA-F0-9]{24}""".r, error = "error.objectId")),
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "email" -> nonEmptyText,
      "password" -> nonEmptyText,
      "avatarURL" -> optional(text),
      "active" -> optional(boolean)) {
        (_id, firstName, lastName, email, password, avatarURL, active) =>
          User(
            _id,
            firstName,
            lastName,
            email,
            password,
            avatarURL,
            active)
      } { user =>
        Some(
          (user._id,
            user.firstName,
            user.lastName,
            user.email,
            user.password,
            user.avatarURL,
            user.active))
      })
}