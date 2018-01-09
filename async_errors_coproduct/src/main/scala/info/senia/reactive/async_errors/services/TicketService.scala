package info.senia.reactive.async_errors.services

import info.senia.reactive.async_errors.services.TicketService.TicketServiceError
import info.senia.reactive.model.Ticket
import shapeless.{:+:, CNil}

import scala.concurrent.Future

class TicketService {
  def byUserId(userId: Long): Seq[Ticket] fthrows TicketServiceError = Future.successful(Right(Seq(
    Ticket(userId * 100 + 1, "It`s my first ticket!"),
    Ticket(userId * 100 + 2, "It`s my second ticket!")
  )))
}

object TicketService {
  case class UserNotFound(userId: Long)
  type TicketServiceError = UserNotFound :+: CNil
}