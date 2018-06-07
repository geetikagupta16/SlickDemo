package db

import slick.jdbc.MySQLProfile

trait MySqlDBComponent extends DBComponent {
  override val driver = MySQLProfile

  import driver.api._

  val db = Database.forConfig("mydb")
}
