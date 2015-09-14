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
          Future.successful(Ok(views.html.manager.product.create_product(cat, lista)))
        }
      },
      {
        case (Some(_id), prodName, prodDesc, prodImgSmallURL, prodImgLargeURL, prodCommentURL, _idCat) => {
          val productComp = Product(Some(_id), prodName, prodDesc, prodImgSmallURL, prodImgLargeURL, prodCommentURL)
          catService.findOneCategory(_idCat).map {
            case Some(category) => {
              catService.addProduct(productComp, category)
              Ok("productComp")
            }
            case None => Ok("ProdComp NOK")
          }
        }
        case (None, prodName, prodDesc, prodImgSmallURL, prodImgLargeURL, prodCommentURL, _idCat) => {
          val productNComp = Product(Some(BSONObjectID.generate.stringify), prodName, prodDesc, prodImgSmallURL, prodImgLargeURL, prodCommentURL)
          catService.findOneCategory(_idCat).map {
            case Some(category) => {
              catService.addProduct(productNComp, category)
              Ok("productNComp")
            }
            case None => Ok("productNComp NOK")
          }
        }
      })
  }
  
//  def edit(_id: String) = Action.async { implicit request =>
//    catService.find
//  }
}