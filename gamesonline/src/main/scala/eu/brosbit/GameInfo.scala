package eu.brosbit

case class UserInfo(id:Long, name:String)

class GameInfo(id:Long, gamer1:UserInfo, gamer2:UserInfo) {
  var pointsGamer1 = 0
  var pointsGamer2 = 0


  def showWiner = {
    if(pointsGamer1 > pointsGamer2)
      s"${gamer1.name} with ${pointsGamer1}  won against ${gamer2.name} with $pointsGamer2"
    else
      s"${gamer2.name} with ${pointsGamer2}  won against ${gamer1.name} with $pointsGamer1"

  }
}


