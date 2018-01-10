package info.senia.reactive.sync.services

import info.senia.reactive.model.Ticket

trait TicketService {
  def byUserId(userId: Long): Seq[Ticket]
}
