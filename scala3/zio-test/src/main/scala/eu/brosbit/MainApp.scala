package eu.brosbit

import zio.*

object MainApp extends ZIOAppDefault {

  def run = myAppLogic.exitCode

  val myAppLogic =
    for {
      _ <- Console.printLine("Hello! What is your name?")
      name <- Console.readLine
      _ <- Console.printLine(s"Hello, ${name}, welcome to ZIO!")
    } yield ()
}
