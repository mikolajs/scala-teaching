package eu.brosbit
import zio.*

object ParallelTask:
  val rightNow: zio.Task[java.util.Date] =
    for
      now <- zio.ZIO.attempt { new java.util.Date() }
      _ <- zio.ZIO.attempt { println(now) }
    yield now

  val nowNowNowNow: zio.ZIO[Any, Nothing, zio.Fiber.Runtime[
    Throwable,
    (Long, Long)
  ]] =
      rightNow
      .repeat(
        zio.Schedule.spaced(2.seconds) &&
          zio.Schedule.recurs(10)
      )
      .fork 
