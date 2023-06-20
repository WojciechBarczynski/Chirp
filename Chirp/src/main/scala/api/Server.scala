package api

import api.db.{DbManager, PostInfo, Posts, RecommendationEngine, Tags, Users}
import cask.Request
import objects.parseReaction


object Server extends cask.MainRoutes {
  @cask.post("/login")
  def login(userName: String): Unit = {
    val strippedUserName = stripString(userName);
    Users.logUser(strippedUserName);
  }

  @cask.post("/post/create")
  def createPost(userName: String, postContent: String, tags: String): Unit = {
    val strippedUserName = stripString(userName);
    val strippedPostContent = stripString(postContent);
    val strippedTags: List[String] = stripString(tags)
      .split(",")
      .map(str => str.trim)
      .map(str => stripString(str))
      .toList;

    Posts.createPost(strippedUserName, strippedPostContent, strippedTags);
  }

  @cask.post("/post/react")
  def react(userName: String, postId: String, reactionType: String): Unit = {
    val strippedUserName = stripString(userName);
    val strippedPostId = stripString(postId);
    val strippedReactionType = stripString(reactionType);
    parseReaction(strippedReactionType) match
      case Some(reaction) => Posts.reactToPost(strippedUserName, strippedPostId, reaction);
      case None => throw RuntimeException(s"Unrecognised reaction ${reactionType}!")
  }

  @cask.post("/tag/subscribe")
  def subscribe(userName: String, tagName: String): Unit = {
    val strippedUserName = stripString(userName);
    val strippedTagName = stripString(tagName);
    Tags.subscribeTag(strippedUserName, strippedTagName);
  }

  @cask.post("tag/recommended")
  def recommendedTags(): Unit = {
    val recommendedTags: List[String] = Tags.getRecommendedTags();
    // TODO add return cask.response here
  }

  @cask.post("/users/follow")
  def followUser(followerUserName: String, followedUserName: String): Unit = {
    Users.followUser(stripString(followerUserName), stripString(followedUserName));
  }

  @cask.post("/users/recommendedPosts")
  def recommendedPosts(userName: String): Unit = {
    val recommendedPosts: List[PostInfo] = RecommendationEngine.getUserRecommendedPosts(stripString(userName));
    println(recommendedPosts);
    // TODO add return cask.response here
  }

  @cask.post("/users/updateBio")
  def updateBio(userName: String, bio: String): Unit = {
    Users.updateBio(stripString(userName), stripString(bio));
  }

  initialize()
}

private def stripString(string: String): String = {
  if string.length < 2 then "" else string.substring(1, string.length() - 1)
}