package eu.brosbit

object CameraData:

  def saveFileInfo(camName:String, createTime:Long):Boolean =
    DBConnect.insertCameraImageInfo(camName, createTime) > 0

  def getCameraImages(camName: String, t:Long): String = {
    val cii = DBConnect.getCameraImages(camName, t)
    val content = cii.map(ci =>
      s""" "/cameras/${ci.cameraName}/${ImageFileNameCreator.getName(ci.createTime)}" """
    ).mkString(",")
    s"""{"images":[$content]} """
  }

  def getNames: String =
    val names:List[String] = DBConnect.getCameraNames
    val content = names.map(n =>
      s""" "$n" """
    ).mkString(",")
    s"""{"cameraNames":[$content]}"""