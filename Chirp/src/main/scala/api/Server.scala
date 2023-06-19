package api

import api.db.DbManager
import cask.Request
import objects.parseReaction


object Server extends cask.MainRoutes {
  @cask.post("/login")
  def login(userName: String): Unit = {
    val strippedUserName = stripString(userName);
    DbManager.logUser(strippedUserName);
  }

  @cask.post("/post/create")
  def createPost(userName: String, postContent: String): Unit = {
    val strippedUserName = stripString(userName);
    val strippedPostContent = stripString(postContent);
    DbManager.createPost(strippedUserName, strippedPostContent);
  }

  @cask.post("/post/react")
  def react(userName: String, postId: String, reactionType: String): Unit = {
    val strippedUserName = stripString(userName);
    val strippedPostId = stripString(postId);
    val strippedReactionType = stripString(reactionType);
    parseReaction(strippedReactionType) match
      case Some(reaction) =>
        DbManager.reactToPost(strippedUserName, strippedPostId, reaction);
        cask.Response(s"OK")
      case None => cask.Response(s"Unsupported reaction type ${strippedReactionType}", 400)
  }

  initialize()
}


private def stripString(string: String): String = {
  string.substring(1, string.length() - 1)
}