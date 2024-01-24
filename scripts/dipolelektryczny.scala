

class Force():
  val kq2 = 900000 //q = 0.01C
  def count(r:Double, d:Double) = 
    kq2/((r+d)*(r+d))+kq2/((r-d)*(r-d))-2*kq2/(r*r)

  def fromTo(from:Double, to:Double, d:Double) =
    var R = from
    var l:List[(Double, Double)] = Nil
    while R <= to do
      l = (R, count(R, d))::l
      R += 1
    l
      
end Force

@main
def main() =
  val force = Force()
  val list = force.fromTo(1, 10, 0.01)
  for t <- list do
    println(s"${t._1} : ${t._2}")
