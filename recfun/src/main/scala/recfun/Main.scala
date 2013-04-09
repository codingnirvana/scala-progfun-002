package recfun
import common._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 20) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c < 0 || r < 0)
      0
    else if (c == 0 && r == 0)
      1
    else
      pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

//  case class Balance(xs:List[Char], nrOfOpenBrackets: Int)


  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {

//    def _balance2(b: Balance) :Boolean = {
//      b match {
//        case Balance(xs, n) if (xs.isEmpty) => n == 0
//        case Balance(_,n) if (n < 0) => false
//        case Balance('('::tail,n) => _balance2(Balance(tail, n + 1))
//        case Balance(')'::tail,n) => _balance2(Balance(tail, n - 1))
//        case c => _balance2(c)
//      }
//    }

    def _balance(xs: List[Char], nrOfOpenBrackets: Int): Boolean = {
      if (xs.isEmpty)
        nrOfOpenBrackets == 0
      else if (nrOfOpenBrackets < 0)
        false
      else if (xs.head == '(')
        _balance(xs.tail, nrOfOpenBrackets + 1)
      else if (xs.head == ')')
        _balance(xs.tail, nrOfOpenBrackets - 1)
      else
        _balance(xs.tail, nrOfOpenBrackets)
    }

    _balance(chars, 0)
//  _balance2(Balance(chars, 0))
  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (money == 0)
        1
    else if (money < 0 || coins.isEmpty)
      0
    else
      countChange(money - coins.head, coins) + countChange(money, coins.tail)
  }
}
