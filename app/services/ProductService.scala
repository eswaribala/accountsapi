package services

import com.google.inject.Inject
import models.{Product, ProductList}

import scala.concurrent.Future

class ProductService @Inject() (items: ProductList) {

  def addItem(item: Product): Future[String] = {
    items.add(item)
  }

  def deleteItem(id: Long): Future[Int] = {
    items.delete(id)
  }

  def updateItem(item: Product): Future[Int] = {
    items.update(item)
  }

  def getItem(id: Long): Future[Option[Product]] = {
    items.get(id)
  }

  def listAllItems: Future[Seq[Product]] = {
    items.listAll
  }
}