package api.db

import objects.ReactionType
import objects.reactionTypeToString

object Posts {
  def reactToPost(userName: String, postId: String, reactionType: ReactionType) = {
    val createReactionQuery =
      s"MATCH (user:User {userName:\"${userName}\", post:POST) " +
        s"WHERE ID(post)=${postId}" +
        s"CREATE (user)-[r:REACTED_TO {reaction_type: \"${reactionTypeToString(reactionType)}\"]->(post)";
    DbManager.executeRequest(createReactionQuery);
  }
  
  def createPost(userName: String, postContent: String, tags: List[String]) = {
    val createPostQuery = s"CREATE (post:POST {content: \"${postContent}\"}) RETURN post;";
    val response = DbManager.executeRequest(createPostQuery);
    tags.map(tagName =>
      Tags.createTag(tagName);
    )

    println(response.body)
    // TODO add created by relation here
  }
}
