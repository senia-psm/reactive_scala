package info.senia.reactive.async_errors.services

import info.senia.reactive.model.Ticket

import scala.concurrent.Future

sealed trait TicketServiceError
class TicketService {
  def byUserId(userId: Long): Seq[Ticket] fthrows TicketServiceError = Future.successful(Right(Seq(
    Ticket(userId * 100 + 1, "It`s my first ticket!"),
    Ticket(userId * 100 + 2, "It`s my second ticket!")
  )))
}
