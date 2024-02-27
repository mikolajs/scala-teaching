import java.io.*
import java.net.*

class PongClinet:
  var client:Socket = NULL
  var in:BufferReader = NULL

  def startConnection(ip:String, port:Int) = 
    client = new Socket(ip, port)
    in = new BufferedReader(new InputStreamReader(client.

class PongServer:
  var in:BufferedReader = NULL
  var serverSocket:serverSocket = NULL
  def start(port:Int) = 
    serverSocket = new ServerSocket(port)
    val ping = "Ping"
    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
    val inputString = in.readLine()
    if inputString = "PING" then 
      println("found")
    else 
      print("Wrong")

    serverSocket

  def stop() = 
    in.close()
    serverSocket.close()
    

@main
def main() =
  val pongServer = new PongServer
