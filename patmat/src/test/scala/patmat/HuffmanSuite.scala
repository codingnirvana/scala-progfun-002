package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._
import scala._
import patmat.Huffman.Leaf
import patmat.Huffman.Fork

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
  trait TestTrees {
    val t1 = Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5)
    val t2 = Fork(Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), Leaf('d',4), List('a','b','d'), 9)
  }

  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }

  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a','b','d'))
    }
  }

  test("times on a small list") {
     assert(times(List('a')) === List(('a', 1)))
  }

  test("times on a large list") {
    assert(times(List('a','b','a')) === List(('a', 2), ('b', 1)))
  }

  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }

  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e',1), Leaf('t',2), Leaf('x',3)))
  }

  test("combine of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(combine(leaflist) === List(Fork(Leaf('e',1),Leaf('t',2),List('e', 't'),3), Leaf('x',4)))
  }

  test("codetree of t1") {
    new TestTrees {
      val treeInExample: CodeTree = createCodeTree(List('a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'b', 'b', 'b', 'c', 'd','e', 'f', 'g', 'h'))
      assert(encode(treeInExample)(List('a','b','c')) === List(1,0,1,0,0,0,0,0,0,0))
      assert(encode(treeInExample)(List('b','a','c')) === List(0, 1, 1, 0, 0, 0, 0, 0, 0, 0))
      assert(decode(treeInExample, List(0, 1, 1, 0, 0, 0, 0, 0, 0, 0)) === List('b','a','c'))
    }
  }

  test("should decode secret") {
    assert(decodedSecret === List('h','u', 'f', 'f','m', 'a','n', 'e', 's', 't', 'c', 'o', 'o', 'l'))
  }

  test("should encode, decode secret") {
    assert(encode(frenchCode)(List('h','u', 'f', 'f','m', 'a','n', 'e', 's', 't', 'c', 'o', 'o', 'l')) === secret)
    assert(quickEncode(frenchCode)(List('h','u', 'f', 'f','m', 'a','n', 'e', 's', 't', 'c', 'o', 'o', 'l')) === secret)
  }

  test("should quick encode, decode secret") {
    val treeInExample: CodeTree = createCodeTree(List('a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'b', 'b', 'b', 'c', 'd','e', 'f', 'g', 'h'))
    assert(quickEncode(treeInExample)(List('a','b','c')) === List(1,0,1,0,0,0,0,0,0,0))
  }

  test("decode and encode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
    }
  }
}
