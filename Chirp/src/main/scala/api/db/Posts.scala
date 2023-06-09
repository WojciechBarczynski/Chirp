package api.db

import objects.nodes.Post
import objects.nodes.parsePosts
import objects.relations.{ReactedTo, parseReactedTo}
import objects.{ReactionType, reactionTypeToString}

object Posts {
  def reactToPost(userName: String, postId: String, reactionType: ReactionType): Unit = {
    val createReactionQuery =
      s"MATCH (post:POST), (user:USER {name: \"${userName}\"}) " +
        s"WHERE ID(post) = ${postId} " +
        s"CREATE (user)-[r:REACTED_TO {reactionType: \"${reactionTypeToString(reactionType)}\"}]->(post);";
    DbManager.executeRequest(createReactionQuery)
  }

  def createPost(userName: String, postContent: String, tags: List[String]): String = {
    if Users.readUser(userName).isEmpty then throw RuntimeException("Unrecognised user!")

    val createPostQuery = s"CREATE (post:POST {content: \"${postContent}\"}) RETURN post;"
    val response = DbManager.executeRequest(createPostQuery)

    val createdPost = parsePosts(response) match
      case List(post) => post;
      case _else => throw Exception("Failed to created post!");

    val createdQuery = s"MATCH (post:POST), (user:USER {name: \"${userName}\"}) " +
      s"WHERE ID(post)=${createdPost.id} " +
      s"CREATE (user)-[r:CREATED]->(post);"
    DbManager.executeRequest(createdQuery)

    tags.foreach(tagName =>
      Tags.createTag(tagName)
      Tags.tagPost(createdPost.id, tagName);
    )
    createdPost.id
  }

  def commentPost(userName: String, commentContent: String, tags: List[String], postId: String): Unit = {
    val commentId: String = createPost(userName, commentContent, tags)

    val commentedQuery = s"MATCH (post:POST), (comment:POST) " +
      s"WHERE ID(post)=${postId} AND ID(comment)=${commentId} " +
      s"CREATE (comment)-[r:COMMENTED]->(post);"
    DbManager.executeRequest(commentedQuery)
  }

  def sharePost(userName: String, postId: String): Unit = {
    val sharedQuery = s"MATCH (post:POST), (user:USER {name: \"${userName}\"}) " +
      s"WHERE ID(post)=${postId} " +
      s"CREATE (user)-[r:SHARED]->(post);"
    DbManager.executeRequest(sharedQuery)
  }

  def getAllPosts: List[Post] = {
    val getAllPostsQuery = s"MATCH (post:POST) RETURN post;"
    parsePosts(DbManager.executeRequest(getAllPostsQuery))
  }

  def getUserPosts(userName: String): List[Post] = {
    val getUserPostsQuery = s"MATCH (user:USER {name: \"${userName}\"})-[:CREATED]->(post:POST) RETURN post;"
    parsePosts(DbManager.executeRequest(getUserPostsQuery))
  }

  def getTagPosts(tagName: String): List[Post] = {
    val getUserPostsQuery = s"MATCH (post:POST)-[:TAGGED]->(tag:TAG {name: \"${tagName}\"}) RETURN post;"
    parsePosts(DbManager.executeRequest(getUserPostsQuery))
  }

  def getPostReactions(postId: String): List[ReactedTo] = {
    val getPostReactionsQuery = s"MATCH (user:USER)-[p:REACTED_TO]->(post:POST) WHERE ID(post) = ${postId} RETURN p;"
    parseReactedTo(DbManager.executeRequest(getPostReactionsQuery))
  }

  def getContentPosts(postContent: String): List[Post] = {
    val getContentPostsQuery = s"MATCH (post:POST) WHERE toLower(post.content) CONTAINS \"${postContent.toLowerCase}\" RETURN post"
    parsePosts(DbManager.executeRequest(getContentPostsQuery))
  }

  def getSharedPosts(userName: String): List[Post] = {
    val getSharedPostsQuery = s"MATCH (user:USER {name: \"${userName}\"})-[:SHARED]->(post:POST) RETURN post;"
    parsePosts(DbManager.executeRequest(getSharedPostsQuery))
  }

  def getPostSharesCount(postId: String): Int = {
    val getSharesCountQuery = s"MATCH (user:USER)-[:SHARED]->(post:POST) WHERE ID(post)=${postId} RETURN COUNT(post);"
    DbManager.executeCountRequest(getSharesCountQuery).toInt
  }

  def getPostComments(postId: String): List[PostInfo] = {
    val getPostCommentSQuery = s"MATCH (comment:POST)-[:COMMENTED]->(post:POST) WHERE ID(post)=${postId} RETURN comment;"
    parsePosts(DbManager.executeRequest(getPostCommentSQuery)).map(post => postInfo(post))
  }

  def getPostCommentCount(postId: String): Int = {
    val getCommentCountQuery = s"MATCH (comment:POST)-[:COMMENTED]->(post:POST) WHERE ID(post)=${postId} RETURN COUNT(comment);"
    DbManager.executeCountRequest(getCommentCountQuery).toInt
  }

  def getPostByDistance(userName: String, maxDistance: Int): List[Post] = {
    val getPostByDistanceQuery =
      s"MATCH (user:USER {name: \"${userName}\"}), (post:POST), " +
        s"path = shortestPath((user)-[*]-(post)) " +
        s"WITH path, post " +
        s"WHERE length (path) <= ${maxDistance} " +
        s"RETURN post"
    parsePosts(DbManager.executeRequest(getPostByDistanceQuery))
  }

  def deletePost(postId: String): Unit = {
    val deletePostQuery = s"MATCH (post:POST) WHERE ID(post)=${postId} DETACH DELETE post"
    DbManager.executeRequest(deletePostQuery)
  }

  def isPostAuthor(userName: String, postId: String): Boolean = {
    val isPostAuthorQuery = s"MATCH (user:USER {name: \"${userName}\"})-[r:CREATED]->(post:POST) WHERE ID(post)=${postId} RETURN post"
    parsePosts(DbManager.executeRequest(isPostAuthorQuery)).nonEmpty
  }

  def updatePostContent(postId: String, postContent: String): Unit = {
    val updatePostContentQuery = s"MATCH (post:POST) WHERE ID(post)=${postId} SET post.content=\"${postContent}\""
    DbManager.executeRequest(updatePostContentQuery)
  }

  def updatePost(userName: String, postId: String, postContent: String): Unit = {
    if isPostAuthor(userName, postId) then
      updatePostContent(postId, postContent)
  }
}
