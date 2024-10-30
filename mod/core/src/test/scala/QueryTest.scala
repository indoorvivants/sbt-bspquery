import munit._
import bspquery.core.QueryAST._
class QueryTest extends FunSuite {
  test("hello") {
    assertEquals(parseOrThrow("platform=sjs"), KV("platform", "sjs"))
    assertEquals(
      parseOrThrow("platform=sjs || (platform=jvm && scalaBinary=3)"),
      OR(
        KV("platform", "sjs"),
        AND(KV("platform", "jvm"), KV("scalaBinary", "3"))
      )
    )
    assertEquals(
      parseOrThrow("platform=jvm && scalaBinary=2.13"),
      AND(KV("platform", "jvm"), KV("scalaBinary", "2.13"))
    )
  }

  def parseOrThrow(string: String) =
    bspquery.core.QueryAST.parse(string).get
}
