package objects.nodes

import io.circe._
import io.circe.syntax._

case class Tag(id: String, name: String)

def parseTags(tagsJsonString: String): List[Tag] = {

  implicit val tagDecoder: Decoder[Tag] = Decoder.forProduct2("id", "name")(Tag.apply)

  parser.decode[List[Tag]](tagsJsonString) match {
    case Right(tags) => tags
    case Left(ex) =>
      println(s"Error: $ex")
      List[Tag]()
  }
}