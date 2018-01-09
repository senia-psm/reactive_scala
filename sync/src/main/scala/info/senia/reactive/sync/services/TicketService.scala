package info.senia.reactive.sync.services

import info.senia.reactive.model.Ticket

class TicketService {
  def byUserId(userId: Long): Seq[Ticket] = Seq(
    Ticket(userId * 100 + 1, "It`s my first ticket!"),
    Ticket(userId * 100 + 2, "It`s my second ticket!")
  )
}
