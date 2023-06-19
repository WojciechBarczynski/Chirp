package api.db

import cask.Response
import objects.nodes.User
import objects.nodes.Tag
import objects.ReactionType
import objects.reactionTypeToString
import sttp.client4.quick.*

import javax.management.Query

object DbManager {
  private val dbServiceUrl = "http://127.0.0.1:5000/"

  def logUser(userName: String): Unit = {
    if readUser(userName).isEmpty then createUser(userName);
  }

  def createPost(userName: String, postContent: String, tags: List[String]) = {
    val createPostQuery = s"CREATE (post:POST {content: \"${postContent}\"}) RETURN post;";
    val response = executeRequest(createPostQuery);
    tags.map(tagName =>
      createTag(tagName);
    )

    println(response.body)
    // TODO add created by relation here
  }

  def reactToPost(userName: String, postId: String, reactionType: ReactionType) = {
    val createReactionQuery =
      s"MATCH (user:User {userName:\"${userName}\", post:POST) " +
        s"WHERE ID(post)=${postId}" +
        s"CREATE (user)-[r:REACTED_TO {reaction_type: \"${reactionTypeToString(reactionType)}\"]->(post)";
    executeRequest(createReactionQuery);
  }

  private def readUser(userName: String): Option[User] = {
    val userMatch = s"MATCH (n:USER {name: \"${userName}\"}) RETURN n;";
    val response = executeRequest(userMatch);


    return None;
  }

  private def readTag(tagName: String): Option[Tag] = {
    val tagMatch = s"MATCH (n:TAG {name: \"${tagName}\"}) RETURN n;";
    val response = executeRequest(tagMatch);
    // TODO parse resopne here
    return None
  }

  private def createTag(tagName: String) = {
    if readTag(tagName).isEmpty then {
      val tagQuery = s"CREATE (tag:TAG {name: \"${tagName}\"});"
      executeRequest(tagQuery);
    }
  }

  private def tagPost(postId: String, tagName: String) = {
    val tagPostQuery = s"MATCH (post:POST, tag:TAG {name: \"${tagName}\"}}) " +
      s"WHERE ID(post) = postId " +
      s"CREATE (user)-[r:TAGGED]->(post);"
    executeRequest(tagPostQuery);
  }

  private def createUser(userName: String) = {
    val userCreateQuery = s"CREATE (user:USER {name: \"${userName}\", bio: \"Empty bio\"});"
    executeRequest(userCreateQuery);
  }

  private def executeRequest(query: String) = {
    val request = s"${dbServiceUrl}execute?query=\"${query}\"";
    quickRequest.post(uri"$request").send()
  }
}
