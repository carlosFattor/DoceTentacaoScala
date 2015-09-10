package controllers

import scala.concurrent.Future

import play.api.mvc.{Action, RequestHeader, Result, Controller}
import models.User
import play.api.mvc.RequestHeader

trait BasicControl extends Controller {
  self:Controller =>
  
  def withSession(f: Boolean => Result) = Action.async { implicit request =>
    if(parseUserFromCookie){
      Future.successful(f(true))
    } else {
      Future.successful(Forbidden("Invalid username or password"))
    }
    
  }

  def parseUserFromCookie(implicit request: RequestHeader) = {
    request.session.get("email") match {
      case Some(user) => true
      case None       => false
    }
  }
}