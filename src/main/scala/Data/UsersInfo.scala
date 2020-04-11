package Data

import scala.collection.mutable

object UsersInfo {

  // Will contain the name of the currently active user; default value is null.
  private var _activeUser: String = null

  // TODO: step 2 - create an attribute that will contain each user and its current balance.
  private var accounts: scala.collection.mutable.Map[String, Double] = scala.collection.mutable.Map()

  private var _activeProductCommand: scala.collection.mutable.Map[Data.Products.Product, Int] = scala.collection.mutable.Map()

  private var _activeBrandCommand: scala.collection.mutable.Map[Data.Products.Brand, Int] = scala.collection.mutable.Map()

  /**
    * Update an account by decreasing its balance.
    * @param user the user whose account will be updated
    * @param amount the amount to decrease
    * @return the new balance
    */
  // TODO: step 2
  def purchase(user: String, amount: Double): Double = {
    accounts(user) -= amount
    accounts(user)
  }

  def addUser(user: String, amount: Double): Unit = accounts = accounts ++ Map(user -> amount)

  def addUser(user: String): Unit = accounts = accounts ++ Map(user -> 30.0)

  /**
    * Updates the current active user
    * @param user The new active user
    */
  def userIsActive(user: String): Unit = _activeUser = user

  def getAmount(): Double = accounts(_activeUser)

  def userIsActive(): Boolean = _activeUser != null

  def activeUser(): String = _activeUser

  /**
    * Check if the user is already registered
    * @param user The user we want to check
    * @return True if the user is registered
    */
  def exists(user: String): Boolean = accounts.exists(pair => user.equals(pair._1))

  def addProduct(product: Data.Products.Product, num: Int) = _activeProductCommand = _activeProductCommand ++ Map(product -> num)
  def getProducts(): scala.collection.mutable.Map[Data.Products.Product, Int] = _activeProductCommand

  def addBrand(brand: Data.Products.Brand, num: Int) = _activeBrandCommand = _activeBrandCommand ++ Map(brand -> num)
  def getBrands(): scala.collection.mutable.Map[Data.Products.Brand, Int] = _activeBrandCommand

  def flushCommand() = {
    _activeBrandCommand = scala.collection.mutable.Map()
    _activeProductCommand = scala.collection.mutable.Map()
  }
}
