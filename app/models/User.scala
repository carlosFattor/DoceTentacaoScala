package models

import play.api.data.Form
import play.api.data.Forms._
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
  active: Option[Boolean],
  isPremium: Option[Boolean], 
  balance: Option[Int]) {

  def checkPassword(password: String): Boolean = this.password == password
}
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
      "active" -> optional(boolean),
      "isPremium" -> optional(boolean),
      "balance" -> optional(number)) {
        (_id, firstName, lastName, email, password, avatarURL, active, isPremium, balance) =>
          User(
            _id,
            firstName,
            lastName,
            email,
            password,
            avatarURL,
            active,
            isPremium,
            balance)
      } { user =>
        Some(
          (user._id,
            user.firstName,
            user.lastName,
            user.email,
            user.password,
            user.avatarURL,
            user.active,
            user.isPremium,
            user.balance))
      })    
}