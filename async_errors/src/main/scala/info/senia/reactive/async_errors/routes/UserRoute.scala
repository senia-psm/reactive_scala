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
      result(handleError){
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
      result(handleError){monadic[Result]{
        val user = userService.byEmailAddress(emailAddress).deep(UserServiceRouteError).each
        val tickets = ticketService.byUserId(user.id).deep(TicketServiceRouteError).each
        val bonuses = marketingService.getBonuses(tickets).deep(MarketingServiceRouteError).each

        UserData(user, bonuses)
      }}
    }
}

object Routes {
  def handleError(e: RouteError): Route = e match {
    case UserServiceRouteError(UserNotFound(address)) =>
      complete(StatusCodes.NotFound -> s"Can't find used by email address `$address'")
    case UserServiceRouteError(_) | TicketServiceRouteError(_) | MarketingServiceRouteError(_) =>
      complete(StatusCodes.InternalServerError)
  }

  def result[T: ToResponseMarshaller](errorHandler: RouteError => Route)(r: EitherT[Future, RouteError, T])
                                     (implicit ec: ExecutionContext): Route =
    onSuccess(r.run.map(_.toEither)) {
      case Left(e) => errorHandler(e)
      case Right(r) => complete(r)
    }

  implicit class EitherOps[L, R](val e: Future[Either[L, R]]) {
    def deep(leftMapper: L => RouteError)
            (implicit ec: ExecutionContext): EitherT[Future, RouteError, R] = EitherT[Future, RouteError, R]{
      e.map(_.left.map(leftMapper)).map(\/.fromEither)
    }
  }
}
