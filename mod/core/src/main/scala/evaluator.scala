package bspquery.core

import scala.util._
import QueryAST._

object Evaluator {
  def evaluate(query: QueryAST, params: Map[String, String]): Try[Boolean] = {

    def go(q: QueryAST): Boolean =
      q match {
        case OR(left, right) => go(left) || go(right)
        case Empty           => true
        case NOT(what)       => !go(what)
        case KV(name, value) =>
          params.get(name) match {
            case None => sys.error(s"Attribute [$name] missing in $params")
            case Some(paramValue) => value == paramValue
          }
        case AND(left, right) => go(left) && go(right)
      }

    Try(go(query))
  }

}
