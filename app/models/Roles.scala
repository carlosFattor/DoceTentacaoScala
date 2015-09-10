package models

import play.api.mvc.Request
import play.api.i18n._
import scala.concurrent.Future
import play.api.libs.json.Json

case class WithRole(role: Role) {
//  def isAuthorized[B](user: User)(implicit request: Request[B], messages: Messages) = {
//    Future.successful(user.roles match {
//      case list: Set[Role] => list.contains(role)
//      case _               => false
//
//    })
//  }
}

sealed trait Role extends Serializable {
  def name: String
}

object Role {
  
  implicit val roleFormat = Json.format[Role]
  
  def apply(role: String): Role = role match {
    case Admin.name => Admin
    case User.name  => User
    case _          => Unknown
  }

  def unapply(role: Role): Option[String] = Some(role.name)

  object Admin extends Role {
    val name = "admin"
  }

  object User extends Role {
    val name = "user"
  }

  object Unknown extends Role {
    val name = "-"
  }
}