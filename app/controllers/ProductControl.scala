package controllers

import java.util.concurrent.TimeoutException
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import javax.inject.Inject
import models.Category
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
import models.services.CategoryService

class ProductControl @Inject() (catService: CategoryService, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  implicit val timeout = 10.seconds

  def list(idCat: String) = Action.async { implicit request =>
    catService.findOneCategory(idCat).map {
      case Some(a) => Ok(views.html.category.list_products(a.products.getOrElse(List.empty), idCat))
      case None    => Redirect(routes.CategoryControl.category()).flashing("fail" -> messagesApi("faill.find"))
    }
  }

  def detail(idCat: String, idProd: String) = Action.async { implicit request =>
    catService.findOneProduct(idCat, idProd).map {
      case Some(a) => Ok(views.html.category.product(a))
      case None    => Ok("")
    }

  }

  def detailProds(prodName: String) = Action.async { implicit request =>
    val prods = catService.findListCategory().map { cats =>
      cats.flatMap { cat =>
        cat.products.filter { p => !p.isEmpty }.getOrElse(Nil)
      }
    }
    Future.successful(Ok(""))
  }
  
  def detailOne(idProd: String) = Action.async { implicit request =>
    val prods = catService.findListCategory().map { cats =>
      cats.flatMap { cat =>
        cat.products.getOrElse(Nil)
      }
    }

    prods.map { prod =>
      val p = prod.find { p => p._id.get.equalsIgnoreCase(idProd) }
      println(p)
      Ok(views.html.category.product(p.get))
    }
  }

  def prodAndCat = Authenticated.async { implicit request =>
    catService.findProduts.flatMap { cats =>
      Future.successful(Ok(views.html.manager.product.list_product(cats)))
    }.recover {
      case t: TimeoutException =>
        Logger.error("Problem adding in Category list process")
        InternalServerError(t.getMessage)
    }
  }

  def addProduct = Authenticated.async { implicit request =>
    val cat = MyForms.productFormTuple.bindFromRequest()
    cat.fold(
      frmError => {
        catService.findListCategory().flatMap { lista =>
          Future.successful(Ok(views.html.manager.product.create_product(cat, lista.map { t => (t._id.get, t.catName) })))
        }
      },
      {
        case (Some(_id), prodName, prodDesc, prodImgSmallURL, prodImgLargeURL, prodCommentURL, _idCat, prodFeature) => {
          val productComp = Product(Some(_id), prodName, prodDesc, prodImgSmallURL, prodImgLargeURL, prodCommentURL, prodFeature)
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
        case (None, prodName, prodDesc, prodImgSmallURL, prodImgLargeURL, prodCommentURL, _idCat, prodFeature) => {
          val productNComp = Product(Some(BSONObjectID.generate.stringify), prodName, prodDesc, prodImgSmallURL, prodImgLargeURL, prodCommentURL, prodFeature)
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

  def edit(idProd: String, idCat: String) = Authenticated.async { implicit request =>
    catService.findListCategory().map { cats =>
      val prod = cats.flatMap { cat =>
        cat.products.get.find { p => p._id.get == idProd }
      }.head

      val form = MyForms.productFormTuple.fill(prod._id, prod.prodName, prod.prodDesc, prod.prodImgSmallURL, prod.prodImgLargeURL, prod.prodCommentURL, idCat, prod.prodFeature)

      Ok(views.html.manager.product.create_product(form, cats.map { t => (t._id.get, t.catName) }))
    }
  }

  def remove(idCat: String, idProd: String) = Authenticated.async { implicit request =>
    catService.removeProduct(idCat, idProd).map {
      case Some(ok) => Redirect(routes.ProductControl.prodAndCat()).flashing("success" -> messagesApi("success.remove"))
      case None     => Redirect(routes.ProductControl.prodAndCat()).flashing("fail" -> messagesApi("fail.remove.product"))
    }
  }

}