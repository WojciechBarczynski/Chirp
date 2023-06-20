package api.db

import objects.nodes.User
import objects.nodes.parseUsers

object Users {
  def logUser(userName: String): Unit = {
    if readUser(userName).isEmpty then createUser(userName);
  }

  def followUser(followerUserName: String, followedUserName: String): Unit = {
    if (!isFollowed(followerUserName, followedUserName)) then
      val followUserQuery = s"MATCH (follower:USER {name: \"${followerUserName}\"}), " +
        s"(followed:USER {name: \"${followedUserName}\"}) " +
        s"CREATE (follower)-[r:FOLLOW]->(followed);"
      DbManager.executeRequest(followUserQuery)
  }

  private def isFollowed(followerUserName: String, followedUserName: String): Boolean = {
    val followUserQuery = s"MATCH (follower:USER {name: \"${followerUserName}\"})" +
      s"-[r:FOLLOW]->(followed:USER {name: \"${followedUserName}\"}) " +
      s"RETURN r;"
    if (DbManager.executeRequest(followUserQuery) == "[]\n") then
      false
    else
      true
  }
  def unfollowUser(followerUserName: String, followedUserName: String): Unit = {
    val unfollowUserQuery = s"MATCH (follower:USER {name: \"${followerUserName}\"})" +
      s"-[r:FOLLOW]-(followed:USER {name: \"${followedUserName}\"}) " +
      s"DELETE r"
    DbManager.executeRequest(unfollowUserQuery)
  }

  def readUser(userName: String): Option[User] = {
    val userMatch = s"MATCH (n:USER {name: \"${userName}\"}) RETURN n;";
    val response = DbManager.executeRequest(userMatch);

    parseUsers(response) match
      case List(user) => Some(user)
      case List() => None
      case _else => throw RuntimeException("Failed to read user!")
  }

  def updateBio(userName: String, bio: String): Unit = {
    val userUpdateBioQuery = s"MATCH (user:USER {name: \"${userName}\"}) SET user.bio = \"${bio}\" RETURN user;"
    DbManager.executeRequest(userUpdateBioQuery);
  }

  private def createUser(userName: String) = {
    val userCreateQuery = s"CREATE (user:USER {name: \"${userName}\", bio: \"Empty bio\"});"
    DbManager.executeRequest(userCreateQuery)
  }

  def getAllUsers: List[User] = {
    val getAllUsersQuery = s"MATCH (user:USER) RETURN user;";
    parseUsers(DbManager.executeRequest(getAllUsersQuery))
  }

  def getContentUsers(userContent: String): List[User] = {
    getAllUsers.filter(user => user.name.toLowerCase.contains(userContent.toLowerCase))
  }

  def getFollowedUsers(userName: String): List[User] = {
    val getFollowedUsersQuery = s"MATCH (user:USER {name: \"${userName}\"})-[:FOLLOW]->(followed_user:USER) RETURN followed_user;"
    parseUsers(DbManager.executeRequest(getFollowedUsersQuery))
  }

  def getFollowersUsers(userName: String): List[User] = {
    val getFollowersUsersQuery = s"MATCH (followers_user:USER)-[:FOLLOW]->(user:USER {name: \"${userName}\"}) RETURN followers_user;"
    parseUsers(DbManager.executeRequest(getFollowersUsersQuery))
  }

  def getFollowersCount(userName: String): Int = {
    getFollowersUsers(userName).length
  }
}

