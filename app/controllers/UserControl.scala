package controllers

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import javax.inject.Inject
import models.{ User, Error }
import models.services.traits.UserService
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import play.api.libs.json.JsPath
import play.api.libs.json.JsError
import play.api.data.validation.ValidationError
import scala.util.Failure
import scala.util.Success

class UserControl @Inject() (userService: UserService) extends Controller {

  def users = Action.async { implicit request =>
    userService.findListUser().map {
      case user => Ok(views.html.user.list_user("Usuários", user))
    }
  }

  def add = Action.async { implicit request =>
    User.userFormValidation.bindFromRequest.fold(
      err => Future.successful(Ok(views.html.user.create_user(err))),
      data => {
        userService.findUser(data._id.getOrElse("")).map {
          case Some(user) => {
            userService.updateUSer(data)
            Redirect(routes.UserControl.users())
          }
          case None => {
            userService.addUser(data)
            Redirect(routes.UserControl.users())
          }
        }
      })
  }

  def edit(id: String) = Action.async { implicit request =>
    userService.findUser(id).map {
      case Some(user) => {
        println(user)
        Ok(views.html.user.create_user(User.userFormValidation.fill(user)))
      }
      case None => Redirect(routes.UserControl.users())
    }
  }
  
  def remove(id: String) = Action.async {
    userService.removeUser(id).map { 
      case Some(_) => Redirect(routes.UserControl.users())
      case None => Redirect(routes.UserControl.users())
    }
  }
}