package api.db

import objects.nodes.User
import objects.nodes.parseUsers

object Users {
  def logUser(userName: String): Unit = {
    if readUser(userName).isEmpty then createUser(userName)
  }

  def followUser(followerUserName: String, followedUserName: String): Unit = {
    if !isFollowed(followerUserName, followedUserName) then
      val followUserQuery = s"MATCH (follower:USER {name: \"${followerUserName}\"}), " +
        s"(followed:USER {name: \"${followedUserName}\"}) " +
        s"CREATE (follower)-[r:FOLLOW]->(followed);"
      DbManager.executeRequest(followUserQuery)
  }

  private def isFollowed(followerUserName: String, followedUserName: String): Boolean = {
    val followUserQuery = s"MATCH (follower:USER {name: \"${followerUserName}\"})" +
      s"-[r:FOLLOW]->(followed:USER {name: \"${followedUserName}\"}) " +
      s"RETURN r;"
    if DbManager.executeRequest(followUserQuery) == "[]\n" then
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
    val userMatch = s"MATCH (n:USER {name: \"${userName}\"}) RETURN n;"
    val response = DbManager.executeRequest(userMatch)

    parseUsers(response) match
      case List(user) => Some(user)
      case List() => None
      case _else => throw RuntimeException("Failed to read user!")
  }

  def updateBio(userName: String, bio: String): Unit = {
    val userUpdateBioQuery = s"MATCH (user:USER {name: \"${userName}\"}) SET user.bio = \"${bio}\" RETURN user;"
    DbManager.executeRequest(userUpdateBioQuery)
  }

  private def createUser(userName: String) = {
    val userCreateQuery = s"CREATE (user:USER {name: \"${userName}\", bio: \"Empty bio\"});"
    DbManager.executeRequest(userCreateQuery)
  }

  def getAllUsers: List[User] = {
    val getAllUsersQuery = s"MATCH (user:USER) RETURN user;"
    parseUsers(DbManager.executeRequest(getAllUsersQuery))
  }

  def getContentUsers(userContent: String): List[User] = {
    val contentUserQuery = s"MATCH (user:USER) WHERE toLower(user.name) CONTAINS \"${userContent.toLowerCase}\" RETURN user"
    parseUsers(DbManager.executeRequest(contentUserQuery))
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

  def getCommonFollowed(userName1: String, userName2: String): List[User] = {
    val getCommonFollowedQuery =
      s"MATCH (they:USER {name: \"${userName1}\"})-[r:FOLLOW]->(common:USER) " +
      s"WITH common " +
      s"MATCH(me: USER {name: \"${userName2}\"}) -[r: FOLLOW]->(common: USER) " +
      s"RETURN common"
    parseUsers(DbManager.executeRequest(getCommonFollowedQuery))
  }

  def getAlsoFollowedBy(myUserName: String, checkedUserName: String): List[User] = {
    val getAlsoFollowedByQuery =
      s"MATCH (me:USER {name: \"${myUserName}\"})-[r:FOLLOW]->(them:USER) " +
      s"WITH them " +
      s"MATCH (them:USER ) -[r: FOLLOW]->(checked:USER {name: \"${checkedUserName}\"}) " +
      s"RETURN them"
    parseUsers(DbManager.executeRequest(getAlsoFollowedByQuery))
  }
}

