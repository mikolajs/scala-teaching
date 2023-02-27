package eu.brosbit.lshziohttp

import zio.test._
import zio.test.Assertion._
import zhttp.http._

object LshziohttpSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[Environment, Failure] = suite("""LshziohttpSpec""")(
    testM("200 ok") {
      checkAllM(Gen.fromIterable(List("text", "json"))) { uri =>
        val request = Request(Method.GET, URL(!! / uri))
        assertM(Lshziohttp.app(request).map(_.status))(equalTo(Status.OK))
      }
    },
  )
}
