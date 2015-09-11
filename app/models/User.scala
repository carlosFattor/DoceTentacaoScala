package models



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
  active: Option[Boolean]) {

  def checkPassword(password: String): Boolean = this.password == password
}
object User {
  import play.api.libs.json.Json
  import play.api.data._
  import play.api.data.Forms._
  import play.modules.reactivemongo.json.BSONFormats._
  import play.api.data.validation.Constraints.pattern
  
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