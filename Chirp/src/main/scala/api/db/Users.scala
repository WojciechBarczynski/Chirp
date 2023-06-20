package api.db

import api.{stripString, db}
import objects.nodes.User
import objects.nodes.parseUsers

object Users {
  def logUser(userName: String): Unit = {
    if readUser(userName).isEmpty then createUser(userName);
  }

  def followUser(followerUserName: String, followedUserName: String): Unit = {
    val followUserQuery = s"MATCH (follower:USER {name: \"${followerUserName}\"}), " +
      s"(followed:USER {name: \"${followedUserName}\"}) " +
      s"CREATE (follower)-[r:FOLLOW]->(followed);"
    DbManager.executeRequest(followUserQuery);
  }

  def readUser(userName: String): Option[User] = {
    val userMatch = s"MATCH (n:USER {name: \"${userName}\"}) RETURN n;";
    val response = DbManager.executeRequest(userMatch);

    parseUsers(response) match
      case List(user) => Some(user)
      case List() => None
      case _else => throw RuntimeException("Failed to read user!")
  }

  def updateBio(userName: String, bio: String) = {
    val userUpdateBioQuery = s"MATCH (user:USER {name: \"${userName}\"}) SET user.bio = \"${bio}\" RETURN user;"
    DbManager.executeRequest(userUpdateBioQuery);
  }

  private def createUser(userName: String) = {
    val userCreateQuery = s"CREATE (user:USER {name: \"${userName}\", bio: \"Empty bio\"});"
    DbManager.executeRequest(userCreateQuery)
  }
}

