package eu.brosbit
import org.mongodb.scala._
import scala.concurrent.Future
import org.mongodb.scala.model.Filters._

case class Player(_id:String, name:String, password:String, points:Int)
case class NewUserCreate(info: String, name:String)

object MongoDataBase {

  val mongoClient: MongoClient = MongoClient("mongodb://localhost")
  val database: MongoDatabase = mongoClient.getDatabase("gameonline")
  val collection: MongoCollection[Document] = database.getCollection("UserPlayers")

  import scala.concurrent.ExecutionContext.Implicits.global
  def setNewUser(name:String, password:String):Future[NewUserCreate] = {
    val existUser = findUser(name)
    existUser.flatMap(userOpt =>
      if(userOpt.isDefined) {
        Future { NewUserCreate("Error", name) }
      }  else {
        val doc = Document("name" -> name, "passwd" -> password, "points" -> 0)
        val ins = collection.insertOne(doc).toFuture()
          .map(_ => NewUserCreate("OK", name))

        ins
      }
    )
  }

  def getUser(name:String, password:String):Future[Option[Player]] = {
     collection.find(equal("name", name)).toFuture().map(_.headOption)
       .map(docOpt => docOpt.map( doc => {
         //println("PASSWORD: " + doc("passwd").asString().getValue)
         if(doc("passwd").asString().getValue == password.trim)
           Player(doc("_id").asObjectId().getValue.toString, doc("name").asString().getValue, doc("passwd").asString().getValue,
             doc("points").asInt32().getValue)
         else  Player("","","",0) }))
       .map(docOpt => if(docOpt.isDefined) {
          if(docOpt.get.name == "") None else docOpt
       } else docOpt )
  }

  def findUser(name:String):Future[Option[Player]] = {
    val look = collection.find(equal("name", name)).headOption()
    look.map(optDoc => {
      optDoc.map(doc =>
        Player(doc("_id").asObjectId().getValue.toString, doc("name").asString().getValue, doc("passwd").asString().getValue,
          doc("points").asInt32().getValue))
    })
  }


}
