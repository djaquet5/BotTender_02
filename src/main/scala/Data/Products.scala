package Data

import Chat.Tokens

object Products {
  // TODO: step 2 - here your will have an attribute that will contain the products (e.g. "bière"), their types (e.g. "Boxer"), and their prices (e.g. 2.0).
  // TODO: step 2 - You will also have to find a way to store the default type/brand of a product.

  type Product = String
  type Brand = Map[Product,String]

  val BIERE: Product = "biere"
  val BOXER: Brand = Map{BIERE->"Boxer"}
  val FARMER: Brand = Map{BIERE->"Farmer"}
  val WITTEKOP: Brand = Map{BIERE->"Wittekop"}
  val PUNKIPA: Brand = Map{BIERE->"PunkIPA"}
  val JACKHAMMER: Brand = Map{BIERE->"Jackhammer"}
  val TENEBREUSE: Brand = Map{BIERE->"Tenebreuse"}

  val CROISSANT: Product = "croissant"
  val MAISON: Brand = Map{CROISSANT->"Maison"}
  val CAILLER: Brand = Map{CROISSANT->"Cailler"}

  val prices: Map[Brand, Int] = Map{
    BOXER -> 1
    FARMER -> 1
    WITTEKOP -> 2
    PUNKIPA -> 3
    JACKHAMMER -> 3
    TENEBREUSE -> 4
    MAISON -> 2
    CAILLER -> 2
  }

  def defaultPrices(product: Product): Int = product match {
    case BIERE => 1
    case CROISSANT => 2
  }



}
