package info.senia.reactive.sync_errors.services

import info.senia.reactive.model.Ticket

sealed trait TicketServiceError
class TicketService {
  def byUserId(userId: Long): Seq[Ticket] throws TicketServiceError = Right(Seq(
    Ticket(userId * 100 + 1, "It`s my first ticket!"),
    Ticket(userId * 100 + 2, "It`s my second ticket!")
  ))
}
