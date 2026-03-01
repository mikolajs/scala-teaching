package eu.brosbit

object VariousActionData:
  def getChristmasTreeInfo: Int = 
    val s = DBConnect.selectChristmasTree().headOption.getOrElse(1)
    MainLogger.infoLog(s"get christmas tree info, state = $s")
    s
  
  def setChristmasTreeState(state:Int):Boolean = {
    MainLogger.infoLog(s"set christmas tree state to $state")
    DBConnect.insertChristmastTree(state) > 0
  }
