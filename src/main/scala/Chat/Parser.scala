package Chat

import Chat.Tokens._
import Tree._
import Data.{Products, UsersInfo}
import scala.collection.mutable

class Parser(tokenizer: Tokenizer) {
  import tokenizer._

  var curTuple: (String, Token) = ("unknown", UNKNOWN)
  var numberTmp: Int = _

  var tmpBrandCommand: mutable.Map[Products.Brand, Int] = mutable.Map()
  var tmpProductCommand: mutable.Map[Products.Product, Int] = mutable.Map()

  def curValue: String = curTuple._1
  def curToken: Token = curTuple._2

  /** Reads the next token and assigns it into the global variable curTuple */
  def readToken(): Unit = curTuple = nextToken()

  /** "Eats" the expected token, or terminates with an error. */
  private def eat(token: Token): Unit = if (token == curToken) readToken() else expected(token)

  /** Complains that what was found was not expected. The method accepts arbitrarily many arguments of type TokenClass */
  // TODO (BONUS): find a way to display the string value of the tokens (e.g. "BIERE") instead of their integer value (e.g. 6).
  private def expected(token: Token, more: Token*): Nothing =
    fatalError(" expected: " +
      (token :: more.toList).mkString(" or ") +
      ", found: " + curToken)

  def fatalError(msg: String): Nothing = {
    println("Fatal error", msg)
    new Exception().printStackTrace()
    sys.exit(1)
  }

  /** the root method of the parser: parses an entry phrase */
  def parsePhrases(): ExprTree = {
    if (curToken == BONJOUR) eat(BONJOUR)

    if (curToken == JE) {
      eat(JE)

      if (curToken == ETRE) {
        eat(ETRE)

        if (curToken == ASSOIFFE) {
          // Here we do not "eat" the token, because we want to have a custom 2-parameters "expected" if the user gave a wrong token.
          readToken()
          Thirsty()
        } else if (curToken == AFFAME) {
          readToken()
          Hungry()
        } else if (curToken == PSEUDO) {
          if (!UsersInfo.exists(curValue)) {
            UsersInfo.addUser(curValue)
          }

          UsersInfo.setActiveUser(curValue)
          Authentication(curValue.tail.head.toUpper.toString ++ curValue.tail.tail)
        } else expected(ASSOIFFE, AFFAME, PSEUDO)

      } else if (curToken == VOULOIR) {
        if (!UsersInfo.isAActiveUser()) {
          while(nextToken()._2 != EOL){
            readToken()
          }

          readToken()
          InactiveUser()
        } else {
          eat(VOULOIR)

          // If the user wants to know the balance
          if (curToken == CONNAITRE) {
            eat(CONNAITRE)
            eat(MON)

            CurrentAmount()
          } else {
            if(curToken == COMMANDER){
              eat(COMMANDER)
            }

            Total(PurchaseStart(), parsePhrasesPurchaseHelper())
          }
        }
      }
      else expected(ETRE, VOULOIR)
    }
    else if (curToken == QUEL) {
      eat(QUEL)
      eat(ETRE)
      eat(LE)
      eat(PRIX)
      eat(DE)

      parsePhrasesAskPricesHelper()
    } else if (curToken == COMBIEN) {
      eat(COMBIEN)
      eat(COUTER)

      parsePhrasesAskPricesHelper()
    }
    else expected(BONJOUR, JE, QUEL, COMBIEN)
  }

  /** Manage the requests to ask some prices */
  def parsePhrasesAskPricesHelper(): ExprTree = curToken match {
    case NUM => {
      numberTmp = curValue.toInt
      eat(NUM)

      parsePhrasesAskPricesHelper()
    }

    case BIERE => {
      eat(BIERE)

      // Check if the token is a brand or the title of the product (e.g. "bière")
      if (curToken >= BOXER) {
        Plus(BrandPrice(Map {Products.BIERE -> curValue}, numberTmp), parsePhrasesAskPricesHelper())
      } else {
        Plus(ProductPrice(
          Products.BIERE, numberTmp),
          parsePhrasesAskPricesHelper())
      }
    }

    case CROISSANT => {
      eat(CROISSANT)

      // Check if the token is a brand or the title of the product (e.g. "croissant")
      if (curToken >= CROISSANT) {
        Plus(BrandPrice(Map {
          Products.CROISSANT -> curValue
        }, numberTmp), parsePhrasesAskPricesHelper())
      } else {

        // If it's a purchase
        if(UsersInfo.isAActiveUser()){
          UsersInfo.addProduct(Products.CROISSANT, numberTmp)
        }
        Plus(ProductPrice(
          Products.CROISSANT, numberTmp),
          parsePhrasesAskPricesHelper())
      }
    }

    case ET => {
      eat(ET)
      parsePhrasesAskPricesHelper()
    }

    case EOL =>
      eat(EOL)
      End()

    case x if x >= BOXER => {
      // The token is a brand. We couldn't eat it before, so we'll eat it now
      // The user can write a query "Je veux 2 PunkIPAs"
      eat(curToken)
      parsePhrasesAskPricesHelper()
    }

    case _ => expected(BIERE, CROISSANT, NUM, ET, EOL)
  }

  /** Manage the requests to purchase somethine */
  def parsePhrasesPurchaseHelper(): ExprTree = curToken match {
    case NUM => {
      numberTmp = curValue.toInt
      eat(NUM)

      parsePhrasesPurchaseHelper()
    }

    case BIERE => {
      eat(BIERE)

      // Check if the token is a brand or the title of the product (e.g. "bière")
      if (curToken >= BOXER) {
        UsersInfo.addBrand(Map{Products.BIERE -> curValue}, numberTmp)
        Purchase(PurchaseBiereBrand(numberTmp, curValue), parsePhrasesPurchaseHelper())
      } else {
        UsersInfo.addProduct(Products.BIERE, numberTmp)
        Purchase(PurchaseBiere(numberTmp), parsePhrasesPurchaseHelper())
      }
    }

    case CROISSANT => {
      eat(CROISSANT)

      // Check if the token is a brand or the title of the product (e.g. "croissant")
      if (curToken >= CROISSANT) {
        UsersInfo.addBrand(Map{Products.CROISSANT -> curValue}, numberTmp)
        Purchase(PurchaseCroissantBrand(numberTmp, curValue), parsePhrasesPurchaseHelper())
      } else {
        UsersInfo.addProduct(Products.CROISSANT, numberTmp)
        Purchase(PurchaseCroissant(numberTmp), parsePhrasesPurchaseHelper())
      }
    }

    case ET => {
      eat(ET)
      Purchase(And(), parsePhrasesPurchaseHelper())
    }

    case EOL =>
      eat(EOL)
      End()

    case x if x >= BOXER => {
      // The token is a brand. We couldn't eat it before, so we'll eat it now
      // The user can write a query "Je veux 2 PunkIPAs"
      eat(curToken)
      parsePhrasesPurchaseHelper()
    }

    case _ => expected(BIERE, CROISSANT, NUM, ET, EOL)
  }

  // Start the process by reading the first token.
  readToken()
}
