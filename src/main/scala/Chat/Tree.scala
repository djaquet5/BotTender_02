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
      case ProductPrice(product: Products.Product, amount: Int) => amount*Products.defaultPrice(product)
      case BrandPrice(brand: Brand, amount: Int) => amount*Products.brandPrice(brand)
      case Plus(leftNode: ExprTree, rightNode: ExprTree) => leftNode.computePrice + rightNode.computePrice
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
      case NewAmount(amount) => "votre nouveau" + Amount(amount).reply
      case CurrentAmount(amount) => "Le montant actuel de votre" + Amount(amount).reply
      case Amount(amount) => " solde est de CHF " + amount
      case And() => " et "
      case AskPrice() => "Cela coûte CHF "
      case BrandPrice(brand, amount) => AskPrice().reply + BrandPrice(brand, amount).computePrice
      case ProductPrice(product, amount) => AskPrice().reply + ProductPrice(product, amount).computePrice
      case Plus(leftNode, rightNode) => AskPrice().reply + Plus(leftNode, rightNode).computePrice
      case InactiveUser() => "Vous devez vous authentifier!"
      case End() => "."
      case PurchaseStart() => "Voici donc "
      case Purchase(leftNode, rightNode) => leftNode.reply + rightNode.reply //+ Plus(leftNode,rightNode) + And().reply + NewAmount(Plus(leftNode,rightNode).computePrice).reply
      case PurchaseCroissantBrand(amount: Int, brand: String) => PurchaseCroissant(amount).reply + brand
      case PurchaseCroissant(amount: Int) => amount + " croissants "
      case PurchaseBiere(amount: Int) => amount + " bières"
      case PurchaseBiereBrand(amount: Int, brand: String) => amount + " " + brand
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
  case class NewAmount(amount: Double) extends ExprTree
  case class CurrentAmount(amount: Double) extends ExprTree
  case class AskPrice() extends ExprTree
  case class ProductPrice(product: Products.Product, amount: Int) extends ExprTree
  case class BrandPrice(brand: Brand, amount: Int) extends ExprTree
  case class Plus(leftNode: ExprTree, rightNode: ExprTree) extends ExprTree
  case class End() extends ExprTree
  case class InactiveUser() extends ExprTree
  case class Purchase(leftNode: ExprTree, rightNode: ExprTree) extends ExprTree
  case class PurchaseCroissantBrand(amount: Int, brand: String) extends ExprTree
  case class PurchaseCroissant(amount: Int) extends ExprTree
  case class PurchaseBiereBrand(amount: Int, brand: String) extends ExprTree
  case class PurchaseBiere(amount: Int) extends ExprTree
  case class PurchaseStart() extends ExprTree
}
