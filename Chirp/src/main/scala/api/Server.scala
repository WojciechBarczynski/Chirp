package api

import api.db.{DbManager, PostInfo, Posts, RecommendationEngine, Tags, Users, postInfosToJsonString}
import cask.Request
import objects.nodes.{User, usersToJsonString, tagsToJsonString}
import objects.parseReaction


object Server extends cask.MainRoutes {
  @cask.post("/login")
  def login(userName: String): Unit = {
    val strippedUserName = stripString(userName)
    Users.logUser(strippedUserName)
  }

  @cask.post("/post/create")
  def createPost(userName: String, postContent: String, tags: String): Unit = {
    val strippedUserName = stripString(userName)
    val strippedPostContent = stripString(postContent)
    val strippedTags: List[String] = stripString(tags)
      .split(",")
      .map(str => str.trim)
      .map(str => stripString(str))
      .toList

    Posts.createPost(strippedUserName, strippedPostContent, strippedTags)
  }

  @cask.post("/post/comment")
  def createComment(userName: String, commentContent: String, tags: String, postId: String): Unit = {
    val strippedUserName = stripString(userName)
    val strippedCommentContent = stripString(commentContent)
    val strippedTags: List[String] = stripString(tags)
      .split(",")
      .map(str => str.trim)
      .map(str => stripString(str))
      .toList
    val strippedPostId = stripString(postId)

    Posts.commentPost(strippedUserName, strippedCommentContent, strippedTags, strippedPostId)
  }

  @cask.get("/post/comments")
  def getComments(postId: String): String = {
    val postComments: List[PostInfo] = Posts.getPostComments(stripString(postId))
    postInfosToJsonString(postComments)
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
    val strippedUserName = stripString(userName)
    val strippedTagName = stripString(tagName)
    Tags.subscribeTag(strippedUserName, strippedTagName)
  }

  @cask.get("tag/recommended")
  def recommendedTags(): String = {
    val recommendedTags: List[String] = Tags.getRecommendedTags;
    tagsToJsonString(recommendedTags)
  }

  @cask.post("/users/follow")
  def followUser(followerUserName: String, followedUserName: String): Unit = {
    Users.followUser(stripString(followerUserName), stripString(followedUserName))
  }

  @cask.get("/users/recommendedPosts")
  def recommendedPosts(userName: String): String = {
    val recommendedPosts: List[PostInfo] = RecommendationEngine.getUserRecommendedPosts(stripString(userName))
    postInfosToJsonString(recommendedPosts)
  }

  @cask.post("/users/updateBio")
  def updateBio(userName: String, bio: String): Unit = {
    Users.updateBio(stripString(userName), stripString(bio))
  }

  @cask.post("/users/followed")
  def followed(userName: String): String = {
    val followedUsers: List[User] = Users.getFollowedUsers(stripString(userName))
    usersToJsonString(followedUsers)
  }

  @cask.post("/users/followers")
  def followers(userName: String): String = {
    val followersUsers: List[User] = Users.getFollowersUsers(stripString(userName))
    usersToJsonString(followersUsers)
  }

  initialize()
}

private def stripString(string: String): String = {
  if string.length < 2 then "" else string.substring(1, string.length() - 1)
}