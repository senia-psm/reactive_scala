package info.senia.reactive.sync.routes

import info.senia.reactive.sync.services.{MarketingService, TicketService, UserService}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.generic.auto._
import io.circe.java8.time._
import info.senia.reactive.model.{Bonus, User}

import scala.concurrent.ExecutionContext
case class UserData(user: User, bonuses: Seq[Bonus])

class UserRoute(
    userService: UserService,
    ticketService: TicketService,
    marketingService: MarketingService)
               (implicit ec: ExecutionContext) {

  def route: Route =
    (get & path("user" / "data") & parameter('emailAddress.as[String])) { emailAddress =>
      complete{
        val user = userService.byEmailAddress(emailAddress)
        val tickets = ticketService.byUserId(user.id)
        val bonuses = marketingService.getBonuses(tickets)

        UserData(user, bonuses)
      }
    }
}
