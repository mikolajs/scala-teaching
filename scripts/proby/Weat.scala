
class Wheat extends Plant:
  hp = 5
  var harvest:Boolean = false
  override obj = WeatConst
  override def nextDay() =
    if !harvest then
      if hp <= obj.maxHP - obj.growHP then hp += obj.growHP else hp = obj.maxHP
      if hp >= obj.maxHP then 
        if bio < obj.maxBio then
          bio += obj.growBio 
          if bio >= obj.maxBio then 
            bio = maxFood
            harvest = true
    
  override def doHarvest(workPower:Int):Int = 
    if harvest then 





object WheatConst  extends  PlantConst: 
  override val name: String = "Wheat"
  override val shortName: String = "Wh"
  override val symbol: String = "|"
  override val maxBio: Short = 40
  override val growBio: Short = 8 
  override val growHP: Short = 10
  override val maxHP: Short = 200
  override val maxFood: Short = 40
  override val growFood: Short = 5
