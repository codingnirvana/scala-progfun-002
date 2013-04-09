package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  
  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }
  
  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   * 
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   * 
   *   val s1 = singletonSet(1)
   * 
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   * 
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   * 
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)

    val s1s2 = union (s1, s2)
    val s1s3 = union (s1, s3)
    val s1s2s3 = union(union (s1, s2), s3)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   * 
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {
    
    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersection should contain only common elements") {
    new TestSets {
      val c = intersect(s1s2, s1s3)
      assert(contains(c, 1), "Intersection 1")
      assert(!contains(c, 2), "Intersection 2")
      assert(!contains(c, 3), "Intersection 3")
    }
  }

  test("diff should return all elements that are in set s that are not in set t") {
    new TestSets {
      val c = diff(s1s2, s1s3)
      assert(contains(c, 2), "diff 2")
      assert(!contains(c, 1), "diff 1")
      assert(!contains(c, 3), "diff 3")
    }
  }

  test("should filter elements that match the predicate") {
    new TestSets  {
      val c = filter(s1s2s3, (x) => x == 1)
      assert(contains(c, 1), "Filter 1")
      assert(!contains(c, 2), "Filter 2")
      assert(!contains(c, 3), "Filter 3")
    }
  }

  test("should filter elements that match the predicate all") {
    new TestSets  {
      val c = filter(s1s2s3, (x) => x == 1 || x == 2 || x == 3)
      assert(contains(c, 1), "Filter 1")
      assert(contains(c, 2), "Filter 2")
      assert(contains(c, 3), "Filter 3")
    }
  }

  test("should filter according to the test case") {
    new TestSets {
      val s4 = singletonSet(4)
      val s5 = singletonSet(4)
      val s7 = singletonSet(7)
      val s1000 = singletonSet(1000)
      val a = union(union(s1,s3), s4)
      val b = union(union(s4,s5), s1000)
      val c = filter(union(a,b), _ < 5)
      printSet(c)
      assert(!contains(c,-1000))
      assert(contains(c,1))
      assert(contains(c,3))
      assert(contains(c,4))
      assert(!contains(c,5))
      assert(!contains(c,1000))
    }
  }

  test("should test forall") {
    new TestSets {
      assert(forall(s1s2s3, (x) => x < 5), "Forall")
      assert(forall(s1, (x) => x > 0), "Forall")
      assert(!forall(s1s2s3, (x) => x < 2), "Forall")
      val c = singletonSet(1001)
      assert(forall(c, _ < 0), "Forall")
      val d = singletonSet(999)
      assert(!forall(d, _ < 0), "Forall")
    }
  }

  test("should test exists") {
    new TestSets {
      assert(exists(s1s2s3, (x) => x < 2), "Exists")
      assert(exists(s1, (x) => x > 0), "Exists")
      assert(!exists(s1s2s3, (x) => x < 1), "Exists")
    }
  }

  test("should test map") {
    new TestSets {
      val a = map(s1s2s3, (x: Int) => x * x)
      assert(contains(a, 1), "Map")
      assert(contains(a, 4), "Map")
      assert(contains(a, 9), "Map")
      assert(!contains(a, 2), "Map")
      assert(!contains(a, 3), "Map")
    }
  }



}
