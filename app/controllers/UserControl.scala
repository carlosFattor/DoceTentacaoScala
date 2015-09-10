package controllers

import scala.concurrent.Future
import javax.inject.Inject
import models.User
import models.services.traits.UserService
import play.api.i18n.{ I18nSupport, MessagesApi, Messages, Lang }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{ AnyContent, Result, Controller, Action }
import play.api.Play.current
import models.UserData

class UserControl @Inject() (userService: UserService, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def login = Action.async { implicit request =>
    UserData.userForm.bindFromRequest.fold(
      formErr => Future.successful(BadRequest(views.html.manager.login(formErr))),
      data => {
        userService.findUserByEmail(data.email).flatMap {
          case Some(user) => {
            if (user.checkPassword(data.passWord) == true) {
              println("login OK")
              Future.successful(Ok("").withSession("email" -> data.email))
            } else {
              Future.successful(BadRequest(views.html.manager.login(UserData.userForm.fill(data))))
            }

          }
          case None => Future.successful(BadRequest(views.html.manager.login(UserData.userForm.fill(data))))
        }
      })
  }
  
  def logout = Action.async { implicit request =>
    Future.successful(Ok(views.html.index("")).withNewSession)    
  }

  def users() = Action.async { implicit request =>
    userService.findListUser().map {
      case user => Ok(views.html.user.list_user("Usuários", user))
    }
  }

  def add = Authenticated.async { implicit request =>
    User.userFormValidation.bindFromRequest.fold(
      err => Future.successful(BadRequest(views.html.user.create_user(err))),
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

  def edit(id: String) = Authenticated.async { implicit request =>
    userService.findUser(id).map {
      case Some(user) => {
        println(user)
        Ok(views.html.user.create_user(User.userFormValidation.fill(user)))
      }
      case None => Redirect(routes.UserControl.users())
    }
  }

  def remove(id: String) = Authenticated.async {
    userService.removeUser(id).map {
      case Some(_) => Redirect(routes.UserControl.users())
      case None    => Redirect(routes.UserControl.users())
    }
  }
}