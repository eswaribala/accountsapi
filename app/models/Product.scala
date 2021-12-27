package models
import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._


case class Product(id: Long, name: String, isAvailable: Boolean)

case class ProductFormData(name: String, isAvailable: Boolean)

object ProductForm {
  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "isComplete" -> boolean
    )(ProductFormData.apply)(ProductFormData.unapply)
  )
}

class ProductTableDef(tag: Tag) extends Table[Product](tag, "product") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def isAvailable = column[Boolean]("isAvailable")

  override def * = (id, name, isAvailable) <> (Product.tupled, Product.unapply)
}


class ProductList @Inject()(
                          protected val dbConfigProvider: DatabaseConfigProvider
                        )(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  var productList = TableQuery[ProductTableDef]

  def add(productItem: Product): Future[String] = {
    dbConfig.db
      .run(productList += productItem)
      .map(res => "ProductItem successfully added")
      .recover {
        case ex: Exception => {
          printf(ex.getMessage())
          ex.getMessage
        }
      }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(productList.filter(_.id === id).delete)
  }


  def update(productItem: Product): Future[Int] = {
    dbConfig.db
      .run(productList.filter(_.id === productItem.id)
        .map(x => (x.name, x.isAvailable))
        .update(productItem.name, productItem.isAvailable)
      )
  }


  def get(id: Long): Future[Option[Product]] = {
    dbConfig.db.run(productList.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Product]] = {
    dbConfig.db.run(productList.result)
  }
}