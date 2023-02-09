package eu.brosbit

import zio.*

object MainApp extends ZIOAppDefault {

  val action: ZIO[Any, Throwable, Unit] =
    for { 
      now  <- zio.ZIO.attempt {new java.util.Date()}
      _ <- Console.printLine(now)
    } yield ()

  val  repeated = action.repeat(Schedule.fixed(10.seconds)).fork


  def run = 
     repeated.zip( 
     myAppLogic.exitCode)

  val myAppLogic =
    for {
      _ <- Console.printLine("Hello! What is your name?")
      name <- Console.readLine
      _ <- Console.printLine(s"Hello, ${name}, welcome to ZIO!")
    } yield ()
}
