package info.senia.reactive.sync_errors.routes

import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.model.StatusCodes
import info.senia.reactive.sync_errors.services._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.generic.auto._
import io.circe.java8.time._
import info.senia.reactive.model.{Bonus, User}
import com.thoughtworks.each.Monadic._

import scala.concurrent.ExecutionContext
import Routes._

case class UserData(user: User, bonuses: Seq[Bonus])
sealed trait RouteError
case class UserServiceRouteError(e: UserServiceError) extends RouteError
case class TicketServiceRouteError(e: TicketServiceError) extends RouteError
case class MarketingServiceRouteError(e: MarketingServiceError) extends RouteError
//case class UnexpectedError(e: String) extends RouteError

class UserRoute(
    userService: UserService,
    ticketService: TicketService,
    marketingService: MarketingService)
               (implicit ec: ExecutionContext) {

  def route: Route =
    (get & path("user" / "data") & parameter('emailAddress.as[String])) { emailAddress =>
      result{
        for {
         user <- userService.byEmailAddress(emailAddress).left.map(UserServiceRouteError)
         tickets <- ticketService.byUserId(user.id).left.map(TicketServiceRouteError)
         bonuses <- marketingService.getBonuses(tickets).left.map(MarketingServiceRouteError)
        } yield UserData(user, bonuses)
      }
    }

  type Result[T] = Either[RouteError, T]
  import scalaz.std.either._
  def routeEach: Route =
    (get & path("user" / "data") & parameter('emailAddress.as[String])) { emailAddress =>
      result{monadic[Result]{
        val user = userService.byEmailAddress(emailAddress).left.map(UserServiceRouteError).each
        val tickets = ticketService.byUserId(user.id).left.map(TicketServiceRouteError).each
        val bonuses = marketingService.getBonuses(tickets).left.map(MarketingServiceRouteError).each

        UserData(user, bonuses)
      }}
    }
}

object Routes {
  def result[T: ToResponseMarshaller](r: Either[RouteError, T]): Route = r match {
    case Left(UserServiceRouteError(UserNotFound(address))) =>
      complete(StatusCodes.NotFound -> s"Can't find used by email address `$address'")
    case Left(UserServiceRouteError(_) | TicketServiceRouteError(_) | MarketingServiceRouteError(_)) =>
      complete(StatusCodes.InternalServerError)
    case Right(r) => complete(r)
  }
}
