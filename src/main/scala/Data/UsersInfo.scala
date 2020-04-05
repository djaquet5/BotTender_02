package Data

import scala.collection.mutable

object UsersInfo {

  // Will contain the name of the currently active user; default value is null.
  private var _activeUser: String = _

  // TODO: step 2 - create an attribute that will contain each user and its current balance.
  private var accounts: Map[String, Int] = Map()

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

  def addUser(user: String, amount: Double): Unit = accounts = accounts ++ Map(user->amount)

  def addUser(user: String): Unit = accounts = accounts ++ Map(user -> 30)

  /**
    * Updates the current active user
    * @param user The new active user
    */
  def userIsActive(user: String): Unit = _activeUser = user

}
