package objects.nodes

import io.circe.{Decoder, parser}

case class Post(id: String, content: String)
def parsePosts(postsJsonString: String): List[Post] = {

  implicit val postDecoder: Decoder[Post] = Decoder.forProduct2("id", "content")(Post.apply)

  parser.decode[List[Post]](postsJsonString) match {
    case Right(posts) =>  posts
    case Left(ex) =>
      println(s"Error: $ex")
      List[Post]()
  }
}
