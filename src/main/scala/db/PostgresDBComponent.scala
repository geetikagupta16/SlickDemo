package db

import slick.jdbc.PostgresProfile

trait PostgresDBComponent extends DBComponent {
  override val driver = PostgresProfile

  import driver.api._

  val db = Database.forConfig("mydb")
}
