package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.{Product, ProductForm}
import play.api.data.FormError

import services.ProductService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ProductController @Inject()(
                                cc: ControllerComponents,
                                productService: ProductService
                              ) extends AbstractController(cc) {

  implicit val productFormat = Json.format[Product]

  def getAll() = Action.async { implicit request: Request[AnyContent] =>
    productService.listAllItems map { items =>
      Ok(Json.toJson(items))
    }
  }

  def getById(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    productService.getItem(id) map { item =>
      Ok(Json.toJson(item))
    }
  }

  def add() = Action.async { implicit request: Request[AnyContent] =>
    ProductForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error!"))
      },
      data => {
        val newProductItem = Product(0, data.name, data.isAvailable)
        productService.addItem(newProductItem).map( _ => Redirect(routes.ProductController.getAll))
      })
  }

  def update(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    ProductForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error!"))
      },
      data => {
        val productItem = Product(id, data.name, data.isAvailable)
        productService.updateItem(productItem).map( _ => Redirect(routes.ProductController.getAll))
      })
  }

  def delete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    productService.deleteItem(id) map { res =>
      Redirect(routes.ProductController.getAll)
    }
  }
}