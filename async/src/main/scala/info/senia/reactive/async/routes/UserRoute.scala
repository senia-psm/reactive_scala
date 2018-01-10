package info.senia.reactive.async.routes

import info.senia.reactive.async.services._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.generic.auto._
import io.circe.java8.time._
import info.senia.reactive.model.{Bonus, User}
import com.thoughtworks.each.Monadic._

import scalaz.std.scalaFuture._
import scala.concurrent.{ExecutionContext, Future}
import scala.async.Async.{async, await}

case class UserData(user: User, bonuses: Seq[Bonus])

class UserRoute(
    userService: UserService,
    ticketService: TicketService,
    marketingService: MarketingService)
               (implicit ec: ExecutionContext) {

  def route: Route =
    (get & path("user" / "data") & parameter('emailAddress)) { emailAddress =>
      complete(
        for {
         user <- userService.byEmailAddress(emailAddress)
         tickets <- ticketService.byUserId(user.id)
         bonuses <- marketingService.getBonuses(tickets)
        } yield UserData(user, bonuses)
      )
    }

  def routeAsync: Route =
    (get & path("user" / "data") & parameter('emailAddress)) { emailAddress =>
      complete{async{
        val user = await(userService.byEmailAddress(emailAddress))
        val tickets = await(ticketService.byUserId(user.id))
        val bonuses = await(marketingService.getBonuses(tickets))

        UserData(user, bonuses)
      }}
    }

  def routeEach: Route =
    (get & path("user" / "data") & parameter('emailAddress)) { emailAddress =>
      complete{monadic[Future]{
        val user = userService.byEmailAddress(emailAddress).each
        val tickets = ticketService.byUserId(user.id).each
        val bonuses = marketingService.getBonuses(tickets).each

        UserData(user, bonuses)
      }}
    }
}
