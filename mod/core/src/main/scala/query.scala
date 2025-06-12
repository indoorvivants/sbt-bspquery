package bspquery.core

import scala.util._

sealed trait QueryAST extends Product with Serializable
object QueryAST {
  case class KV(name: String, value: String) extends QueryAST
  case class AND(left: QueryAST, right: QueryAST) extends QueryAST
  case class OR(left: QueryAST, right: QueryAST) extends QueryAST
  case class NOT(what: QueryAST) extends QueryAST
  case object Empty extends QueryAST

  def parse(query: String): Try[QueryAST] = {
    var index = 0
    val length = query.length()

    def _currentChar() = query(index)

    def _consume(expected: String): Boolean = {
      while (index < length && _currentChar().isWhitespace)
        index += 1

      if (index >= length)
        false
      else if (query.startsWith(expected, index)) {
        index += expected.length()
        true
      } else
        false
    }

    def _consumeWhile(c: Char => Boolean): String = {
      val sb = new java.lang.StringBuilder
      while (index < length && c(_currentChar())) {
        sb.append(_currentChar())
        index += 1
      }

      sb.toString()
    }

    def _shouldConsume(expected: String) = {
      val startPosition = index
      if (!_consume(expected))
        _err(s"Expected `$expected` at position $startPosition")
    }

    def keyName() =
      _consumeWhile(_.isLetter)

    def keyValue() =
      _consumeWhile(c => !(c.isWhitespace || c == ')' || c == '('))

    def _reachedEnd() = {
      val lastIndex = index
      while (index < length && _currentChar().isWhitespace)
        index += 1

      if (index == length) true
      else {
        index = lastIndex
        false
      }
    }

    def _skipWS() = {
      while (index < length && _currentChar().isWhitespace) {
        index += 1
      }
    }

    def _err(msg: String) = {
      val pointer = " " * index + "^" + " " * (length - index - 1)
      sys.error(s"Parsing error:\n$query\n$pointer\n[$msg]")
    }

    def expression(): QueryAST = orExpression()

    def orExpression(): QueryAST = {
      var left = andExpression()
      while (_consume("||")) {
        left = OR(left, andExpression())
      }
      left
    }

    def andExpression(): QueryAST = {
      var left = primaryExpression()
      while (_consume("&&")) {
        left = AND(left, primaryExpression())
      }
      left
    }

    def primaryExpression(): QueryAST = {
      _skipWS()
      if (_reachedEnd()) Empty
      else if (_currentChar().isLetter) { // KV pair
        val key = keyName()
        _shouldConsume("=")
        val value = keyValue()
        KV(key, value)
      } else if (_currentChar() == '(') {
        _shouldConsume("(")
        val expr = orExpression()
        _shouldConsume(")")
        expr
      } else _err(s"Unexpected character `${_currentChar()}`")
    }

    Try(expression())
  }
}
