package lila.friend

import lila.user.User

import org.joda.time.DateTime

case class Request(
    id: String,
    user: String,
    friend: String,
    date: DateTime) {

  def users = List(user, friend)

  def by(userId: String) = userId == user
}

object Request {

  def makeId(u1: String, u2: String) = List(u1, u2).sorted mkString "@"

  def make(user: String, friend: String): Request = new Request(
    id = makeId(user, friend),
    user = user,
    friend = friend,
    date = DateTime.now)

  import lila.db.Tube
  import Tube.Helpers._
  import play.api.libs.json._

  private[friend] lazy val tube = Tube[Request](
    __.json update readDate('date) andThen Json.reads[Request],
    Json.writes[Request] andThen (__.json update writeDate('date))
  )
}

case class RequestWithUser(request: Request, user: User) {
  def id = request.id
  def date = request.date
}
