package objects.nodes

import io.circe._
import io.circe.syntax._

case class User(id: String, name: String, bio: String) {}
def parseUsers(usersJsonString: String): List[User] = {

  implicit val userDecoder: Decoder[User] = Decoder.forProduct3("id", "name", "bio")(User.apply)

  parser.decode[List[User]](usersJsonString) match {
    case Right(users) => users
    case Left(ex) =>
      println(s"Error: $ex")
      List[User]()
  }
}

def usersToJsonString(users: List[User]): String = {
  implicit val userEncoder: Encoder[User] = userJson => Json.obj(
    "id" -> userJson.id.asJson,
    "name" -> userJson.name.asJson,
    "bio" -> userJson.bio.asJson,
  )
  users.asJson.spaces2
}
