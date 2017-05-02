package pl.edu.osp


import akka.actor.{ ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Separator, Button}
import scalafx.scene.control.Spinner
import scalafx.scene.layout.{FlowPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.paint.{Stops, LinearGradient}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text


object Boot extends JFXApp {

  val buttonCount = new Button {
    text = "Licz!"
    style = "-fx-font-size: 24pt"
    minHeight = 30
    minWidth = 100
    onAction = handle {countPrimes()}
  }
  val canvas = new Canvas(750, 400)
  val gc = canvas.graphicsContext2D
  gc.fill = Color.DARKKHAKI
  gc.fillRect(0, 0, canvas.width.get, canvas.height.get)

  println("Start Application")
  stage = new PrimaryStage {
    title.value = "Primary View"
    width = 800
    height = 600
    scene = new Scene {
      fill = Color.LIGHTGREEN
      content = new VBox {
        fill = Color.WHITE
        padding = Insets(10)
        children = new VBox {
          fill = Color.WHITE
          padding = Insets(10)
          children = Seq(
            new FlowPane {
              children = List(
                new Text {
                  text = "Liczby pierwsze"
                  style = "-fx-font-size: 24pt"
                  padding = Insets(10)
                  fill = new LinearGradient(
                    endX = 0,
                    stops = Stops(SEAGREEN, DARKGREEN))
                },
                new Separator {
                  minWidth = 50
                },
                new Spinner[Int](1000, 50000, 1000, 1000) {
                  styleClass += Spinner.StyleClassArrowsOnRightHorizontal
                  minWidth = 200
                  prefHeight = 30
                },
                new Separator {
                  minWidth = 50
                },
                buttonCount)
              minHeight = 100
              prefHeight = 100
              minWidth = 400
              prefWidth = 400
            },
            canvas
          )
        }
      }
    }
  }
  def countPrimes():Unit = {

    val system = ActorSystem("MySystem")
    val many = 20000
    val mainActor = system.actorOf(Props(classOf[PrimeMainActor], many ), name = "primeMActor")
    var j = 3
    val max = many*15
    while (j < max){
      mainActor ! j
      j += 2
    }
    mainActor ! 999983
    implicit val timeout = Timeout(20 seconds)
    val future: Future[Array[Int]] = ask(mainActor, Result).mapTo[Array[Int]]
    val result = Await.result(future, 10 second)
    println("Boot receive data: " + result.mkString(", "))
    val colors = Array(Color.RED, Color.BLUE, Color.SILVER, Color.FUCHSIA, Color.GREEN,
    Color.ROYALBLUE, Color.CHOCOLATE, Color.CYAN, Color.WHEAT, Color.AQUA,
    Color.BLUEVIOLET, Color.YELLOW, Color.BROWN, Color.HOTPINK, Color.AZURE)
    var i = 0
    for(r <- result) {
      gc.fill = colors(i)
      gc.fillRect(i*50, canvas.height.get - 250, 50, 250)
      gc.fillPath()
      i += 1
    }
  }
}


 




