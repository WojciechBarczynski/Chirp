package api.db

import cask.Response
import objects.nodes.User
import objects.nodes.Tag
import objects.ReactionType
import objects.reactionTypeToString
import sttp.client4.quick.*

import javax.management.Query

object DbManager {
  private val dbServiceUrl = "http://127.0.0.1:5000/"

  def executeRequest(query: String) = {
    val request = s"${dbServiceUrl}execute?query=\"${query}\"";
    quickRequest.post(uri"$request").send().body
  }
}
