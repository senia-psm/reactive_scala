package info.senia.reactive.async_errors_coproduct.routes

import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.model.StatusCodes
import info.senia.reactive.async_errors_coproduct.services._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.generic.auto._
import io.circe.java8.time._
import info.senia.reactive.model.{Bonus, User}
import com.thoughtworks.each.Monadic._
import info.senia.reactive.async_errors_coproduct.services.MarketingService.MarketingServiceError
import info.senia.reactive.async_errors_coproduct.services.TicketService.TicketServiceError
import info.senia.reactive.async_errors_coproduct.services.UserService.UserServiceError
import shapeless.ops.adjoin.Adjoin
import shapeless.{:+:, CNil, Coproduct, Poly, Poly1}
import shapeless.ops.coproduct.{Basis, Folder}

import scalaz.std.either._
import scalaz.std.scalaFuture._
import scala.concurrent.{ExecutionContext, Future}
import scalaz.{EitherT, \/}
import Routes.{errorHandler, _}

case class UserData(user: User, bonuses: Seq[Bonus])

class UserRoute(
                 userService: UserService,
                 ticketService: TicketService,
                 marketingService: MarketingService)
               (implicit ec: ExecutionContext) {

  val adjoin = Adjoin[UserServiceError :+: TicketServiceError :+: MarketingServiceError :+: CNil]

  type UserError = adjoin.Out

  def route: Route =
    (get & path("user" / "data") & parameter('emailAddress.as[String])) { emailAddress =>
      result(errorHandler){
        for {
         user <- userService.byEmailAddress(emailAddress).deep[UserError]
         tickets <- ticketService.byUserId(user.id).deep[UserError]
         bonuses <- marketingService.getBonuses(tickets).deep[UserError]
        } yield UserData(user, bonuses)
      }
    }
}

object Routes {
  object errorHandler extends Poly1 {
    type At[T] = Case.Aux[T, Route]
    implicit def atTicketUser: At[TicketService.UserNotFound] = at{ e =>
      complete(StatusCodes.NotFound -> s"Can't find user with id ${e.userId}")
    }
    implicit def atUser: At[UserService.UserNotFound] = at{ e =>
      complete(StatusCodes.NotFound -> s"Can't find user with email address ${e.emailAddress}")
    }
    implicit def atAnother: At[UserService.AnotherError] = at{ _ =>
      complete(StatusCodes.InternalServerError)
    }
  }

  def result[T: ToResponseMarshaller, E <: Coproduct, P <: Poly](p: P)(r: EitherT[Future, E, T])
                                                                (implicit f: Folder.Aux[P, E, Route],
                                                                 ec: ExecutionContext): Route =
    onSuccess(r.run.map(_.toEither)) {
      case Left(e) => f(e)
      case Right(r) => complete(r)
    }

  implicit class EitherOps[L <: Coproduct, R](val e: Future[Either[L, R]]) extends AnyVal {
    def deep[E <: Coproduct](implicit b: Basis[E, L], ec: ExecutionContext): EitherT[Future, E, R] = EitherT[Future, E, R]{
      e.map(_.left.map(l => b.inverse(Right(l)))).map(\/.fromEither)
    }
  }
}