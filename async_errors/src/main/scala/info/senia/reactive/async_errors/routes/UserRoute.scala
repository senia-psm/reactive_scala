package info.senia.reactive.async_errors.routes

import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.model.StatusCodes
import info.senia.reactive.async_errors.services._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.generic.auto._
import io.circe.java8.time._
import info.senia.reactive.model.{Bonus, User}
import com.thoughtworks.each.Monadic._

import scalaz.std.either._
import scalaz.std.scalaFuture._
import scala.concurrent.{ExecutionContext, Future}
import scalaz.{EitherT, \/}

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

  def result[T: ToResponseMarshaller](r: EitherT[Future, RouteError, T]): Route = onSuccess(r.run.map(_.toEither)) {
    case Left(UserServiceRouteError(UserNotFound(address))) =>
      complete(StatusCodes.NotFound -> s"Can't find used by email address `$address'")
    case Left(UserServiceRouteError(_) | TicketServiceRouteError(_) | MarketingServiceRouteError(_)) =>
      complete(StatusCodes.InternalServerError)
    case Right(r) => complete(r)
  }

  implicit class EitherOps[L, R](val e: Future[Either[L, R]]) {
    def deep(leftMapper: L => RouteError): EitherT[Future, RouteError, R] = EitherT[Future, RouteError, R]{
      e.map(_.left.map(leftMapper)).map(\/.fromEither)
    }
  }

  def route: Route =
    (get & path("user" / "data") & parameter('emailAddress.as[String])) { emailAddress =>
      result{
        for {
         user <- userService.byEmailAddress(emailAddress).deep(UserServiceRouteError)
         tickets <- ticketService.byUserId(user.id).deep(TicketServiceRouteError)
         bonuses <- marketingService.getBonuses(tickets).deep(MarketingServiceRouteError)
        } yield UserData(user, bonuses)
      }
    }

  type Result[T] = EitherT[Future, RouteError, T]
  def routeEach: Route =
    (get & path("user" / "data") & parameter('emailAddress.as[String])) { emailAddress =>
      result{monadic[Result]{
        val user = userService.byEmailAddress(emailAddress).deep(UserServiceRouteError).each
        val tickets = ticketService.byUserId(user.id).deep(TicketServiceRouteError).each
        val bonuses = marketingService.getBonuses(tickets).deep(MarketingServiceRouteError).each

        UserData(user, bonuses)
      }}
    }
}
