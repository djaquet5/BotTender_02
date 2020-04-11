package Chat

import Chat.Tokens._
import Tree._
import Data.{Products, UsersInfo}

// TODO - step 4
class Parser(tokenizer: Tokenizer) {

  import tokenizer._

  var curTuple: (String, Token) = ("unknown", UNKNOWN)

  var numberTmp: Int = _

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
          if (!Data.UsersInfo.exists(curValue)) {
            Data.UsersInfo.addUser(curValue)
          }
          Data.UsersInfo.setActiveUser(curValue)
          Authentication(curValue.tail.head.toUpper.toString ++ curValue.tail.tail)
        } else {
          expected(ASSOIFFE, AFFAME, PSEUDO)
        }
      } else if (curToken == VOULOIR) {
        if (!Data.UsersInfo.isAActiveUser()) {
          while(nextToken()._2 != EOL){
            readToken()
          }
          readToken()
          InactiveUser()
        }else {
          eat(VOULOIR)
          // If the user wants to know the balance
          if (curToken == CONNAITRE) {
            eat(CONNAITRE)
            eat(MON)
            CurrentAmount()
          }else {
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


  // Start the process by reading the first token.
  readToken()

  def parsePhrasesAskPricesHelper(): ExprTree = curToken match {
    case NUM => {
      numberTmp = curValue.toInt
      eat(NUM)
      parsePhrasesAskPricesHelper()
    }
    case BIERE => {
      eat(BIERE)
      // on regarde si on a affaire à une brand ou juste le produit générique
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
      // on regarde si on a affaire à une marque ou juste au produit générique
      if (curToken >= BOXER) {
        Plus(BrandPrice(Map {
          Products.CROISSANT -> curValue
        }, numberTmp), parsePhrasesAskPricesHelper())
      } else {
        //if it's a purchase
        if(UsersInfo.isAActiveUser()){
          UsersInfo.addProduct(Products.CROISSANT, numberTmp)
        }
        Plus(ProductPrice(
          Products.CROISSANT, numberTmp),
          parsePhrasesAskPricesHelper())
      }
    }
    case ET => {
      // on mange le ET et on continue de parser notre phrase
      eat(ET)
      parsePhrasesAskPricesHelper()
    }
    case EOL =>
      // on mange le EOL et on finit notre phrase
      eat(EOL)
      End()
    case x if x >= BOXER => {
      // on a affaire à une marque, comme on a pas pu manger le token avant, on le fait ici
      eat(curToken)
      parsePhrasesAskPricesHelper()
    }
    case _ => expected(BIERE, CROISSANT, NUM, ET, EOL)
  }

  def parsePhrasesPurchaseHelper(): ExprTree = curToken match {
    case NUM => {
      numberTmp = curValue.toInt
      eat(NUM)
      parsePhrasesPurchaseHelper()
    }
    case BIERE => {
      eat(BIERE)
      // on regarde si on a affaire à une brand ou juste le produit générique
      if (curToken >= BOXER) {
        UsersInfo.addBrand(Map{Data.Products.BIERE -> curValue}, numberTmp)
        Purchase(PurchaseBiereBrand(numberTmp, curValue), parsePhrasesPurchaseHelper())
      } else {
        UsersInfo.addProduct(Data.Products.BIERE, numberTmp)
        Purchase(PurchaseBiere(numberTmp), parsePhrasesPurchaseHelper())
      }
    }
    case CROISSANT => {
      eat(CROISSANT)
      // on regarde si on a affaire à une brand ou juste le produit générique
      if (curToken >= BOXER) {
        UsersInfo.addBrand(Map{Data.Products.CROISSANT -> curValue}, numberTmp)
        Purchase(PurchaseCroissantBrand(numberTmp, curValue), parsePhrasesPurchaseHelper())
      } else {
        UsersInfo.addProduct(Data.Products.CROISSANT, numberTmp)
        Purchase(PurchaseCroissant(numberTmp), parsePhrasesPurchaseHelper())
      }
    }
    case ET => {
      // on mange le ET et on continue de parser notre phrase
      eat(ET)
      Purchase(And(), parsePhrasesPurchaseHelper())
    }
    case EOL =>
      // on mange le EOL et on finit notre phrase
      eat(EOL)
      End()
    case x if x >= BOXER => {
      // on a affaire à une marque, comme on a pas pu manger le token avant, on le fait ici
      eat(curToken)
      parsePhrasesPurchaseHelper()
    }
    case _ => expected(BIERE, CROISSANT, NUM, ET, EOL)
  }

}
