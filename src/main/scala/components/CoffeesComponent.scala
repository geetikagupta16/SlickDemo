package components

import scala.concurrent.Future

import db.MySqlDBComponent
import models.{Coffees, Suppliers}
import slick.jdbc.MySQLProfile.api._

object CoffeesComponent extends CoffeesTable with MySqlDBComponent

trait CoffeesTable extends SuppliersTable {

  // Definition of the COFFEES table
  class CoffeesTable(tag: Tag) extends Table[Coffees](tag, "COFFEES") {
    def name = column[String]("COF_NAME", O.PrimaryKey)

    def supID = column[Int]("SUP_ID")

    def price = column[Double]("PRICE")

    def sales = column[Int]("SALES")

    def total = column[Int]("TOTAL")

    def * = (name, supID, price, sales, total) <> (Coffees.tupled, Coffees.unapply)

    // A reified foreign key relation that can be navigated to create a join
    def supplier = foreignKey("SUP_FK", supID, suppliers)(_.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.Cascade )
  }

  val coffees = TableQuery[CoffeesTable]

  def setUpDb(): Future[Unit] = {

    val setup = DBIO.seq(
      // Create the tables, including primary and foreign keys
      (suppliers.schema ++ coffees.schema).create,

      // Insert some suppliers
      suppliers += Suppliers(101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199"),
      suppliers += Suppliers(49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460"),
      suppliers += Suppliers(150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966"),
      // Equivalent SQL code:
      // insert into SUPPLIERS(SUP_ID, SUP_NAME, STREET, CITY, STATE, ZIP) values (?,?,?,?,?,?)

      // Insert some coffees (using JDBC's batch insert feature, if supported by the DB)
      coffees ++= Seq(
        Coffees("Colombian", 101, 7.99, 0, 0),
        Coffees("French_Roast", 49, 8.99, 0, 0),
        Coffees("Espresso", 150, 9.99, 0, 0),
        Coffees("Colombian_Decaf", 101, 8.99, 0, 0),
        Coffees("French_Roast_Decaf", 49, 9.99, 0, 0)
      )
      // Equivalent SQL code:
      // insert into COFFEES(COF_NAME, SUP_ID, PRICE, SALES, TOTAL) values (?,?,?,?,?)
    )

    /*val setupFuture = */db.run(setup)
  }

  def getCoffees(): Future[List[Coffees]] = {
    db.run(coffees.to[List].result)
  }

  def addCoffees(coffee: Coffees): Future[Int] = {
    db.run(coffees += coffee)
  }

  def deleteCoffee(coffee: Coffees): Future[Int] = {
    db.run(coffees.filter(_.name === coffee.name).delete)
  }

  def updateCoffee(coffee: Coffees) = {
    db.run(coffees.update(coffee))
  }

}