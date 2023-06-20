package api.db

import io.circe._
import io.circe.syntax._
import api.db.DbManager.executeRequest
import objects.nodes.Tag
import objects.nodes.parseTags

object Tags {
  def createTag(tagName: String): String = {
    val createdTag = readTag(tagName) match
      case Some(tag) => tag
      case None => createNewTag(tagName)
    createdTag.id
  }

  private def createNewTag(tagName: String): Tag = {
    val tagQuery = s"CREATE (tag:TAG {name: \"${tagName}\"}) RETURN tag;"
    val response = executeRequest(tagQuery)
    val createdTag = parseTags(response) match
      case List(tag) => tag;
      case _else => throw Exception("Failed to created post!");
    createdTag
  }

  def subscribeTag(userName: String, tagName: String): Unit = {
    val subscribeQuery = s"MATCH (user:USER {name: \"${userName}\"}), (tag:TAG {name: \"${tagName}\"}) " +
      s"CREATE (user)-[r:SUBSCRIBE]->(tag);"
    DbManager.executeRequest(subscribeQuery)
  }

  def tagPost(postId: String, tagName: String): Unit = {
    val tagId = createTag(tagName)
    val tagPostQuery = s"MATCH (post:POST), (tag:TAG) " +
      s"WHERE ID(post) = ${postId} AND ID(tag) = ${tagId} " +
      s"CREATE (post)-[r:TAGGED]->(tag);"
    executeRequest(tagPostQuery)
  }

  def getRecommendedTags: List[String] = {
    RecommendationEngine.getRecommendedTags().map(tag => tag.name)
  }

  private def readTag(tagName: String): Option[Tag] = {
    val tagMatch = s"MATCH (n:TAG {name: \"${tagName}\"}) RETURN n;"
    val response = executeRequest(tagMatch)

    parseTags(response) match
      case List(tag) => Some(tag)
      case List() => None
      case _other => throw RuntimeException("Failed to read tag!")
  }

  def getAllTags: List[Tag] = {
    val getAllTagsQuery = s"MATCH (tag:TAG) RETURN tag;"
    parseTags(DbManager.executeRequest(getAllTagsQuery))
  }

  def getContentTags(tagContent: String): List[Tag] = {
    getAllTags.filter(tag => tag.name.toLowerCase.contains(tagContent.toLowerCase))
  }

  def getSubscribedTags(userName: String): List[Tag] = {
    val getSubscribedTagsQuery = s"MATCH (user:USER {name: \"${userName}\"})-[:SUBSCRIBED]->(tag:TAG) RETURN tag;"
    parseTags(DbManager.executeRequest(getSubscribedTagsQuery))
  }

  def tagsToJsonString(tags: List[String]): String = {
    tags.asJson.spaces2
  }
}
