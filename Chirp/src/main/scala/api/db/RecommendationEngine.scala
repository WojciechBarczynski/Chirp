package api.db

import objects.nodes.{Post, Tag, parsePosts, parseTags}
import api.db.PostInfo
import api.db.postInfo

import scala.collection.mutable
import scala.collection.mutable.HashMap;

object RecommendationEngine {
  def getUserRecommendedPosts(userName: String): List[PostInfo] = {
    var scores: mutable.HashMap[Post, Double] = new mutable.HashMap();
    val allPosts = Posts.getAllPosts;

    allPosts.foreach(post => scores.put(post, 0.0));

    addFollowedUsersScore(scores, userName);
    addSubscribedTagsScore(scores, userName);
    addReactionsScore(scores);

    scores
      .toSeq
      .sortBy(_._2)
      .map((post, score) => post)
      .toList
      .map(post => postInfo(post))
  }

  def getRecommendedTags(): List[Tag] = {
    val recommendedTagsQuery = "MATCH (post:POST)-[r:TAGGED]->(tag:TAG) RETURN tag, count(r) ORDER BY count(r) DESC LIMIT 10;";
    parseTags(DbManager.executeRequest(recommendedTagsQuery))
  }

  private def addFollowedUsersScore(scores: mutable.HashMap[Post, Double], userName: String): Unit = {
    val followedUsersPostsQuery =
      s"MATCH (follower:USER {name:\"${userName}\"})-[:FOLLOW]->(followed:USER)-[:CREATED]->(post:POST) RETURN post;";

    val followedPosts = parsePosts(DbManager.executeRequest(followedUsersPostsQuery));

    followedPosts.foreach(
      post => updateScore(scores, post)
    );
  }

  private def addSubscribedTagsScore(scores: mutable.HashMap[Post, Double], userName: String): Unit = {
    val subscribedTagsPosts =
      s"MATCH (user:USER {name: \"${userName}\"})-[:SUBSCRIBE]->(tag:TAG)<-[:TAGGED]-(post:POST) RETURN post;"

    val posts = parsePosts(DbManager.executeRequest(subscribedTagsPosts));

    posts.foreach(post => updateScore(scores, post));
  }
  
  private def addReactionsScore(scores: mutable.HashMap[Post, Double]): Unit = {
    val postReactions: mutable.HashMap[Post, Int] = new mutable.HashMap();

    scores.keys.foreach(post => Posts.getPostReactions(post.id));
  }
  
  private def updateScore(scores: mutable.HashMap[Post, Double], post: Post): Unit = {
    val score = scores.get(post);
    score match
      case Some(value) => scores.put(post, value + 1);
      case None => throw RuntimeException("Post not found!");
  }
}
