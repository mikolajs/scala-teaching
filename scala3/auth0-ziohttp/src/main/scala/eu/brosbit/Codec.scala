package eu.brosbit

import pdi.jwt.{Jwt, JwtAlgorithm}

//https://jwt-scala.github.io/jwt-scala/jwt-core-jwt.html

case class UserInfo(nick:String, email:String,  roles:List[String])



object Codec:
  def code(secretKey:String) = Jwt.encode("""{"user":1}""", secretKey, JwtAlgorithm.HS256)
  def decode(secretKey:String, token:String) =
    Jwt.decodeRawAll(token, secretKey, Seq(JwtAlgorithm.HS256))
  def code2 = Jwt.encode(JwtClaim("""{"user":1}""").expiresIn(10), "secretKey", JwtAlgorithm.HS256)
  def isValid(token:String) = Jwt.isValid(token, "secretKey", Seq(JwtAlgorithm.HS256))
