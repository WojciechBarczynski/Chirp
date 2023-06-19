package objects.relations

import io.circe.{Decoder, parser}

case class ReactedTo(id: String, reactionType: String) 

def parseReactedTo(postsJsonString: String): List[ReactedTo] = {

  implicit val postDecoder: Decoder[ReactedTo] = Decoder.forProduct2("id", "reactionType")(ReactedTo.apply)

  parser.decode[List[ReactedTo]](postsJsonString) match {
    case Right(reactedTo) => reactedTo
    case Left(ex) =>
      println(s"Error: $ex")
      List[ReactedTo]()
  }
}