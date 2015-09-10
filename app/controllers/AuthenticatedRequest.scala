package controllers

import play.api.mvc._
import scala.concurrent.Future

class UserRequest[A](val email: Option[String], request: Request[A]) extends WrappedRequest[A](request)

class AuthenticatedRequest[A](val email: String, request: Request[A]) extends WrappedRequest[A](request)

object UserAction extends ActionBuilder[UserRequest] with ActionTransformer[Request, UserRequest] {
  def transform[A](request: Request[A]) = Future.successful {
    new UserRequest(request.session.get("email"), request)
  }
}

object Authenticated extends ActionBuilder[AuthenticatedRequest] {
  def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[Result]) = {
    request.session.get("email").map { email =>
      block(new AuthenticatedRequest(email, request))
    } getOrElse {
      Future.successful(Results.Forbidden("Invalid username or password"))
    }
  }
}