package controllers

import java.util.concurrent.TimeoutException
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import javax.inject.Inject
import models.Category
import models.services.traits.CategoryService
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.api.mvc.Action
import play.api.mvc.Controller
import models.MyForms
import models.Product
import scala.util.Success
import scala.util.Failure
import reactivemongo.bson.BSONObjectID
import scala.util.Failure
import scala.concurrent.Await

class ProductControl @Inject() (catService: CategoryService, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  implicit val timeout = 10.seconds

  def prodAndCat = Action.async { implicit request =>
    catService.findProduts.flatMap { cats =>
      Future.successful(Ok(views.html.manager.product.list_product(cats)))
    }.recover {
      case t: TimeoutException =>
        Logger.error("Problem adding in Category list process")
        InternalServerError(t.getMessage)
    }
  }

  def addProduct = Action.async { implicit request =>
    val cat = MyForms.productFormTuple.bindFromRequest()
    cat.fold(
      frmError => {
        catService.findListCategory().flatMap { lista =>
          Future.successful(Ok(views.html.manager.product.create_product(cat, lista)).flashing("fail" -> messagesApi("fail.add")))
        }
      },
      {
        case (Some(_id), prodName, prodDesc, prodImgSmallURL, prodImgLargeURL, prodCommentURL, _idCat) => {
          val productComp = Product(Some(_id), prodName, prodDesc, prodImgSmallURL, prodImgLargeURL, prodCommentURL)
          catService.findOneCategory(_idCat).flatMap {
            case Some(category) => {
              catService.updateProduct(productComp, category).map {
                case Some(a) => Redirect(routes.ProductControl.prodAndCat()).flashing("success" -> messagesApi("success.update"))
                case None    => Redirect(routes.ProductControl.prodAndCat()).flashing("fail" -> messagesApi("fail.update"))
              }

            }
            case None => Future.successful(Ok("ProdComp NOK"))
          }
        }
        case (None, prodName, prodDesc, prodImgSmallURL, prodImgLargeURL, prodCommentURL, _idCat) => {
          val productNComp = Product(Some(BSONObjectID.generate.stringify), prodName, prodDesc, prodImgSmallURL, prodImgLargeURL, prodCommentURL)
          catService.findOneCategory(_idCat).map {
            case Some(category) => {
              catService.addProduct(productNComp, category)
              Redirect(routes.ProductControl.prodAndCat()).flashing("success" -> messagesApi("success.add"))
            }
            case None => Redirect(routes.ProductControl.addProduct()).flashing("fail" -> messagesApi("fail.add"))
          }
        }
      })
  }

  def edit(idProd: String, idCat: String) = Action.async { implicit request =>
    catService.findListCategory().map { cats =>
      val prod = cats.flatMap { cat =>
        cat.products.get.find { p => p._id.get == idProd }
      }.head

      val form = MyForms.productFormTuple.fill(prod._id, prod.prodName, prod.prodDesc, prod.prodImgSmallURL, prod.prodImgLargeURL, prod.prodCommentURL, idCat)

      Ok(views.html.manager.product.create_product(form, cats))
    }
  }
  
  def remove(idCat: String, idProd: String) = Action.async { implicit request =>
    catService.removeProduct(idCat, idProd).map {
      case Some(ok) => Redirect(routes.ProductControl.prodAndCat()).flashing("success" -> messagesApi("success.remove"))
      case None => Redirect(routes.ProductControl.prodAndCat()).flashing("fail" -> messagesApi("fail.remove.product"))
    }
  }

}