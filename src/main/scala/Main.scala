import scala.concurrent.Await
import scala.concurrent.duration.Duration

import components.{CoffeesComponent, SuppliersComponent}
import models.Suppliers

object Main extends App {

  Await.result(CoffeesComponent.setUpDb(), Duration.Inf)

  val coffees = Await.result(CoffeesComponent.getCoffees(), Duration.Inf)
  println("COFFEESS::::::::::::::::::\n\n" + coffees)

  val suppliers = Await.result(SuppliersComponent.getSuppliers(), Duration.Inf)
  println("SUPPLIERS::::::::::::::::::\n\n" + suppliers)
  Await.result(SuppliersComponent.deleteSupplier(Suppliers(49,"Superior Coffee","1 Party Place","Mendocino","CA","95460")), Duration.Inf)

  val coffees1 = Await.result(CoffeesComponent.getCoffees(), Duration.Inf)
  println("COFFEESS::::::::::::::::::\n\n" + coffees1)

  val suppliers1 = Await.result(SuppliersComponent.getSuppliers(), Duration.Inf)
  println("SUPPLIERS::::::::::::::::::\n\n" + suppliers1)

}
