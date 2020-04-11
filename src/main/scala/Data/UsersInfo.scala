package Data

import scala.collection.mutable

object UsersInfo {

  // Will contain the name of the currently active user; default value is null.
  private var _activeUser: String = null

  // TODO: step 2 - create an attribute that will contain each user and its current balance.
  private var accounts: mutable.Map[String, Double] = mutable.Map()

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

    // TODO: Besoin de l'afficher ?
    //print(accounts(user))
  }

  // TODO: Remove ?
//  def addUser(user: String, amount: Double): Unit = accounts ++ Map(user->amount)
//  def addUser(user: String): Unit = accounts = accounts ++ Map(user -> 30)

  /**
    * Add an account with a balance to the accounts list
    *
    * src : https://alvinalexander.com/scala/how-to-add-update-remove-mutable-map-elements-scala-cookbook/
    * @param user The user to add
    * @param amount The user's balance
    */
  def addUser(user: String, amount: Double): Unit = accounts += (user -> amount)

  /**
    * Add an account with the default balance. By default, the balance is 30.0
    * @param user The user to add
    */
  def addUser(user: String): Unit = addUser(user, 30.0)

  /**
    * Get the the active user's balance
    * @return The active user's balance
    */
  def getAmount(): Double = accounts(_activeUser)

  /**
    * Updates the current active user
    * @param user The new active user
    */
  def setActiveUser(user: String): Unit = _activeUser = user

  /**
    * Check if a user is active
    * @return True if the user is active, false otherwise
    */
  def isAUserActive(): Boolean = _activeUser != null

  /**
    * Check if the user is already registered
    * @param user The user we want to check
    * @return True if the user is registered
    */
  def exists(user: String): Boolean = accounts.exists(pair => user.equals(pair._1))

}
