package api.db

import cask.Response
import objects.nodes.User
import objects.ReactionType
import objects.reactionTypeToString
import sttp.client4.quick.*

object DbManager {
  private val dbServiceUrl = "http://127.0.0.1:5000/"

  def logUser(userName: String): Unit = {
    if readUser(userName).isEmpty then createUser(userName);
  }

  def createPost(userName: String, postContent:String) = {
    val createPostQuery = s"CREATE (post:POST {content: \"${postContent}\"})";
    val response = writeRequest(createPostQuery);
    println(response.body)
    // TODO add created by relation here
  }

  def reactToPost(userName: String, postId: String, reactionType: ReactionType) = {
    val createReactionQuery =
      s"MATCH (user:User {userName:${userName}, post:POST) " +
        s"WHERE ID(post)=${postId} " +
        s"CREATE (user)-[r:REACTED_TO {reaction_type: \"${reactionTypeToString(reactionType)}\"]->(post)";
    val response = writeRequest(createReactionQuery);
  }

  private def readUser(userName: String): Option[User] = {
    val userMatch = s"MATCH (n:USER {name: \"${userName}\"}) RETURN n;";
    val response = readRequest(userMatch);
    // TODO parse response here
    return None;
  }

  private def createUser(userName: String) = {
    val userCreateQuery = s"CREATE (user:USER {name: \"${userName}\", bio: \"Empty bio\"});"
    val response = writeRequest(userCreateQuery);
  }

  private def writeRequest(query: String) = {
    val request = s"${dbServiceUrl}write?query=\"${query}\"";
    quickRequest.get(uri"$request").send()
  }

  private def readRequest(query: String) = {
    val request = s"${dbServiceUrl}read?query=\"${query}\"";
    quickRequest.get(uri"$request").send()
  }
}
