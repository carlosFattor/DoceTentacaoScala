package models

import play.api.data.Form
import play.api.data.Forms._

case class UserData(email: String, passWord: String)

object UserData{
val userForm = Form(
  mapping(
    "email" -> nonEmptyText,
    "passWord" -> nonEmptyText
  )(UserData.apply)(UserData.unapply)
)
}