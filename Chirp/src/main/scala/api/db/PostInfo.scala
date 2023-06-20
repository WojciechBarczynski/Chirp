package api.db

import io.circe._
import io.circe.syntax._
import objects.ReactionType
import objects.nodes.parseUsers
import objects.nodes.Post

class PostInfo(val postId: String, val content: String, val authorName: String, val reactions: Map[String, Int])

def postInfo(post: Post): PostInfo = {
  val authorNameQuery = s"MATCH (user:USER)-[:CREATED]->(post:POST) WHERE ID(post) = ${post.id} RETURN user;"
  val authorName = parseUsers(DbManager.executeRequest(authorNameQuery)).last.name;
  val reactions: List[String] = Posts.getPostReactions(post.id).map(reactedTo => reactedTo.reactionType);
  val reactionsCount = reactions
    .foldLeft(Map.empty[String, Int]) {(m: Map[String, Int], x: String) => m + ((x, m.getOrElse(x, 0) + 1))};
  new PostInfo(post.id, post.content, authorName, reactionsCount)
}

def postInfosToJsonString(postInfos: List[PostInfo]): String = {
  implicit val postInfoEncoder: Encoder[PostInfo] = postInfoJson => Json.obj(
    "postId" -> postInfoJson.postId.asJson,
    "content" -> postInfoJson.content.asJson,
    "authorName" -> postInfoJson.authorName.asJson,
    "reactions" -> postInfoJson.reactions.asJson,
  )

  postInfos.asJson.spaces2
}