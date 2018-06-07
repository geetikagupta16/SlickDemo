package components

import scala.concurrent.Future

import db.{DBComponent, MySqlDBComponent}
import models.Suppliers
import slick.jdbc.MySQLProfile.api._

object SuppliersComponent extends SuppliersTable with MySqlDBComponent

trait SuppliersTable extends DBComponent {

  // Definition of the SUPPLIERS table
  class SuppliersTable(tag: Tag) extends Table[Suppliers](tag, "SUPPLIERS") {

    def id = column[Int]("SUP_ID", O.PrimaryKey) // This is the primary key column
    def name = column[String]("SUP_NAME")

    def street = column[String]("STREET")

    def city = column[String]("CITY")

    def state = column[String]("STATE")

    def zip = column[String]("ZIP")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, name, street, city, state, zip) <> (Suppliers.tupled, Suppliers.unapply)
  }

  val suppliers = TableQuery[SuppliersTable]

  def getSuppliers(): Future[List[Suppliers]] = {
    db.run(suppliers.to[List].result)
  }

  def deleteSupplier(supplier: Suppliers): Future[Int] = {
    db.run(suppliers.filter(_.id === supplier.id).delete)
  }
}