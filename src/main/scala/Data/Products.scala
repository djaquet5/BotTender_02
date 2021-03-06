package Data

object Products {
  type Product = String
  type Brand = Map[Product,String]

  val BIERE: Product    = "biere"
  val BOXER: Brand      = Map{BIERE->"boxer"}
  val FARMER: Brand     = Map{BIERE->"farmer"}
  val WITTEKOP: Brand   = Map{BIERE->"wittekop"}
  val PUNKIPA: Brand    = Map{BIERE->"punkipa"}
  val JACKHAMMER: Brand = Map{BIERE->"jackhammer"}
  val TENEBREUSE: Brand = Map{BIERE->"tenebreuse"}

  val CROISSANT: Product  = "croissant"
  val MAISON: Brand       = Map{CROISSANT->"maison"}
  val CAILLER: Brand      = Map{CROISSANT->"cailler"}

  def  brandPrice(brand: Brand): Int = brand match {
    case BOXER      => 1
    case FARMER     => 1
    case WITTEKOP   => 2
    case PUNKIPA    => 3
    case JACKHAMMER => 3
    case TENEBREUSE => 4
    case MAISON     => 2
    case CAILLER    => 2
  }

  def defaultPrice(product: Product): Int = product match {
    case BIERE      => 1
    case CROISSANT  => 2
  }
}
