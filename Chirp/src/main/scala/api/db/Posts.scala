package api.db

import objects.nodes.Post
import objects.nodes.parsePosts
import objects.{ReactionType, reactionTypeToString}

object Posts {
  def reactToPost(userName: String, postId: String, reactionType: ReactionType) = {
    val createReactionQuery =
      s"MATCH (post:POST), (user:USER {name: \"${userName}\"}) " +
        s"WHERE ID(post) = ${postId} " +
        s"CREATE (user)-[r:REACTED_TO {reaction_type: \"${reactionTypeToString(reactionType)}\"}]->(post);";
    DbManager.executeRequest(createReactionQuery);
  }

  def createPost(userName: String, postContent: String, tags: List[String]) = {
    if Users.readUser(userName).isEmpty then throw RuntimeException("Unrecognised user!");

    val createPostQuery = s"CREATE (post:POST {content: \"${postContent}\"}) RETURN post;";
    val response = DbManager.executeRequest(createPostQuery);

    val createdPost = parsePosts(response) match
      case List(post) => post;
      case _else => throw Exception("Failed to created post!");

    val createdQuery = s"MATCH (post:POST), (user:USER {name: \"${userName}\"}) " +
      s"WHERE ID(post)=${createdPost.id} " +
      s"CREATE (user)-[r:CREATED]->(post);"
    DbManager.executeRequest(createdQuery);

    tags.foreach(tagName =>
      Tags.createTag(tagName);
      Tags.tagPost(createdPost.id, tagName);
    );
  }

  def getAllPosts(): List[Post] = {
    val getAllPostsQuery = s"MATCH (post:POST) RETURN post;";
    parsePosts(DbManager.executeRequest(getAllPostsQuery))
  }

  def getUserPosts(userName: String): List[Post] = {
    val getUserPostsQuery = s"MATCH (user:USER {name: \"${userName}\"})-[:CREATED]->(post:POST) RETURN post;"
    parsePosts(DbManager.executeRequest(getUserPostsQuery))
  }

//  def getPostReactions(postId: String): List[ReactionType] = {
//
//  }
}
