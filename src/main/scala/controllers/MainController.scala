package controllers

import akka.http.scaladsl.server.Directives._
import components.CoffeesComponent
import models.{CoffeeList, Coffees}
import util.JsonHelper
import scala.concurrent.ExecutionContext.Implicits.global

class MainController extends JsonHelper {

  val coffeeComponent = CoffeesComponent
  val routes = post {
    path("addCoffees") {
      entity(as[Coffees]) { coffees =>
        coffeeComponent.addCoffees(coffees)
        complete("Coffee added successfully")
      }
    }
  } ~
  get {
    path("getCoffees") {
      complete{
        coffeeComponent.getCoffees().map (coffees => CoffeeList(coffees))
      }
    }
  }
}
