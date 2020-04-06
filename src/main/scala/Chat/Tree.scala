package Chat

import Data.Products
import Data.Products.Brand

// TODO - step 3
object Tree {

  /**
    * This sealed trait represents a node of the tree and contains methods to compute it and write its output text in console.
    */
  sealed trait ExprTree {
    /**
      * Compute the price of the current node, then returns it. If the node is not a computational node, the method
      * returns 0.0.
      * For example if we had a "+" node, we would add the values of its two children, then return the result.
      * @return the result of the computation
      */
    def computePrice: Double = ExprTree.this match {
      case AskProductPrice(product: Products.Product, amount: Int) => amount*Products.defaultPrices(product)
      case AskBrandPrice(brand: Brand, amount: Int) => amount*Products.prices(brand)
      case _ => 0.0
    }

    /**
      * Return the output text of the current node, in order to write it in console.
      * @return the output text of the current node
      */
    def reply: String = this match {
      // Example cases
      case Thirsty() => "Eh bien, la chance est de votre côté, car nous offrons les meilleures bières de la région !"
      case Hungry() => "Pas de soucis, nous pouvons notamment vous offrir des croissants faits maisons !"
      case Authentication(userName) => "Bonjour, " + userName + " !"
      case NewAmount(amount) => "votre nouveau" + Amount(amount)
      case CurrentAmount(amount) => "Le montant actuel de votre" + Amount(amount)
      case Amount(amount) => " solde est de CHF " + amount
      case And() => " et "
      case AskPrice() => "Cela coûte CHF " + AskPrice().computePrice
    }
  }

  /**
    * Declarations of the nodes' types.
    */
  // Example cases
  case class Thirsty() extends ExprTree
  case class Hungry() extends ExprTree
  case class And() extends ExprTree
  case class Authentication(userName: String) extends ExprTree
  case class Amount(amount: Double) extends ExprTree
  case class NewAmount(override val amount: Double) extends Amount(amount)
  case class CurrentAmount(override val amount: Double) extends Amount(amount)
  case class AskPrice() extends ExprTree
  case class AskProductPrice(product: Products.Product, amount: Int) extends AskPrice
  case class AskBrandPrice(brand: Brand, amount: Int) extends AskPrice
}
