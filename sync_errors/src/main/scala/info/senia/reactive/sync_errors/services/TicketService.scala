package info.senia.reactive.sync_errors.services

import info.senia.reactive.model.Ticket

sealed trait TicketServiceError
trait TicketService {
  def byUserId(userId: Long): Seq[Ticket] throws TicketServiceError
}
