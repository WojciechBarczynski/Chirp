package api.db

import api.db.DbManager.executeRequest
import objects.nodes.Tag
import objects.nodes.parseTags

object Tags {
  def createTag(tagName: String) = {
    if readTag(tagName).isEmpty then {
      val tagQuery = s"CREATE (tag:TAG {name: \"${tagName}\"});"
      executeRequest(tagQuery);
    }
  }

  def subscribeTag(userName: String, tagName: String) = {
    val subscribeQuery = s"MATCH (user:USER {name: \"${userName}\"}), (tag:TAG {name: \"${tagName}\"}) " +
      s"CREATE (user)-[r:SUBSCRIBE]->(tag);"
    DbManager.executeRequest(subscribeQuery);
  }

  def tagPost(postId: String, tagName: String) = {
    val tagPostQuery = s"MATCH (post:POST), (tag:TAG {name: \"${tagName}\"}) " +
      s"WHERE ID(post) = ${postId} " +
      s"CREATE (post)-[r:TAGGED]->(tag);"
    executeRequest(tagPostQuery);
  }

  private def readTag(tagName: String): Option[Tag] = {
    val tagMatch = s"MATCH (n:TAG {name: \"${tagName}\"}) RETURN n;";
    val response = executeRequest(tagMatch);

    parseTags(response) match
      case List(tag) => Some(tag)
      case List() => None
      case _other => throw RuntimeException("Failed to read tag!")
  }
}