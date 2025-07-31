package eu.brosbit

import io.vertx.sqlclient.{Row, RowSet}
import scala.jdk.CollectionConverters.*


trait GetArrayDB[A <: Product]:
  val a:A
  def length:Int = a.getClass.getDeclaredFields.length
  def createObj(params:List[Any]):A

given GetArrayDB[CameraImg] with {
  val a: CameraImg = CameraImg(0, "")
  override def createObj(params:List[Any]): CameraImg =
    CameraImg(params(0).asInstanceOf[Long], params(1).asInstanceOf[String])
}


object DataCreator:
  def createLongArray(rows:RowSet[Row]):List[Long] =
    val it = rows.iterator()
    var data:List[Long] = Nil
    if it.hasNext then data = it.next().getLong(0)::data
    while it.hasNext do
      data = it.next().getLong(0)::data
    data.reverse


  def createStringArray(rows: RowSet[Row]): List[String] =
    val it = rows.iterator()
    var data: List[String] = Nil
    if it.hasNext then data = it.next().getString(0) :: data
    while it.hasNext do
      data = it.next().getString(0) :: data
    data.reverse

  def createJsonArray(rows:RowSet[Row], objName:String, pools:Int):String =
    val it = rows.iterator()
    var list:List[String] = Nil
    //println(rows.columnDescriptors().asScala.map(d => d.typeName()).mkString(", "))
    for i <- 0 until rows.rowCount() do
      if it.hasNext then
        val row = it.next()
        list = row.toJson.toString::list

    s"""{"$objName":[${list.reverse.mkString(",")}]}"""


  def createImagesCameraList[A <: Product](rows: RowSet[Row])(using getArrayDB: GetArrayDB[A]):List[A] =
    val cSize = getArrayDB.length
    val size = rows.rowCount()
    val it = rows.iterator()
    val types = rows.columnDescriptors().asScala.map(d => d.typeName()).toArray
    var list:List[A]= Nil
    for i <- 0 until size yield
      if it.hasNext then
        val row = it.next()
        var params:List[Any] = Nil
        //println(row.toJson.toString)
        for j <- 0 until cSize do {
            types(j) match {
              case "INT8" => params = row.getLong(j)::params
              case _ => params = row.getString(j)::params
            }
            //params = row.getString(j)::params
        }
        //println(params)

        list = getArrayDB.createObj(params.reverse)::list

    list
