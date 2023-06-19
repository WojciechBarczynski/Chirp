package objects.nodes

import io.circe.{Decoder, parser}

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
