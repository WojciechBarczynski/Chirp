package api.db

import api.db.DbManager
import objects.nodes.User

object Users {
  def logUser(userName: String): Unit = {
    if readUser(userName).isEmpty then createUser(userName);
  }

  private def readUser(userName: String): Option[User] = {
    val userMatch = s"MATCH (n:USER {name: \"${userName}\"}) RETURN n;";
    val response = DbManager.executeRequest(userMatch);

    // TODO parse response here
    return None;
  }

  private def createUser(userName: String) = {
    val userCreateQuery = s"CREATE (user:USER {name: \"${userName}\", bio: \"Empty bio\"});"
    DbManager.executeRequest(userCreateQuery)
  }
}
