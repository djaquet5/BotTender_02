package Chat

object Tokens {
  type Token = Int

  // Terms
  val BONJOUR: Token     = 0
  val JE: Token          = 1

  // Actions
  val ETRE: Token        = 2
  val VOULOIR: Token     = 3

  // Operators
  val ET: Token          = 4
  val OU: Token          = 5

  // Products
  val BIERE: Token       = 6
  val CROISSANT: Token   = 7

  // Utils
  val PSEUDO: Token      = 9
  val NUM: Token         = 10
  val UNKNOWN: Token     = 11
  val EOL: Token         = 12

  // State of mind
  val ASSOIFFE : Token   = 13
  val AFFAME : Token     = 14

  // Solde
  val CONNAITRE : Token  = 15
  val MON : Token        = 16
  val SOLDE : Token      = 17

  // Price
  val QUEL : Token       = 18
  val LE : Token         = 19
  val PRIX : Token       = 20
  val DE : Token         = 21
  val COMBIEN : Token    = 22
  val COUTER : Token     = 23

  // Order
  val COMMANDER : Token  = 24

  // Brands
  val BOXER: Token       = 25
  val FARMER: Token      = 26
  val WITTEKOP: Token    = 27
  val PUNKIPA: Token     = 28
  val JACKHAMMER: Token  = 29
  val TENEBREUSE: Token  = 30
  val MAISON: Token      = 31
  val CAILLER: Token     = 32
}
