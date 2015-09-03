package utils

import javax.inject.Inject
import controllers.routes
import play.api.http.DefaultHttpErrorHandler
import play.api.i18n.Messages
import play.api.mvc.Results._
import play.api.mvc.{ Result, RequestHeader }
import play.api.routing.Router
import play.api.{ OptionalSourceMapper, Configuration }
import scala.concurrent.Future
import javax.inject.Inject
import play.api.UsefulException

/**
 * @author carlos
 */
class ErrorHandler @Inject() (
  env: play.api.Environment,
  config: Configuration,
  sourceMapper: OptionalSourceMapper,
  router: javax.inject.Provider[Router])
  extends DefaultHttpErrorHandler(env, config, sourceMapper, router){
 
  /**
   * Called when a user is not authenticated.
   *
   * As defined by RFC 2616, the status code of the response should be 401 Unauthorized.
   *
   * @param request The request header.
   * @param messages The messages for the current language.
   * @return The result to send to the client.
   */
  def onNotAuthenticated(request: RequestHeader, messages: Messages): Option[Future[Result]] = {
    Some(Future.successful(Redirect(routes.Application.index())))
  }
  
  override def onProdServerError(request: RequestHeader, exception: UsefulException) = {
    Future.successful(
      InternalServerError("A server error occurred: " + exception.getMessage)
    )
  }
  
  override def onForbidden(request: RequestHeader, message: String) = {
    Future.successful(
      Forbidden("You're not allowed to access this resource.")
    )
  }

  /**
   * Called when a user is authenticated but not authorized.
   *
   * As defined by RFC 2616, the status code of the response should be 403 Forbidden.
   *
   * @param request The request header.
   * @param messages The messages for the current language.
   * @return The result to send to the client.
   */
  def onNotAuthorized(request: RequestHeader, messages: Messages): Option[Future[Result]] = {
    Some(Future.successful(Redirect(routes.Application.index()).flashing("error" -> Messages("access.denied")(messages))))
  }
  
}