package info.senia.reactive.async.services.impl

import info.senia.reactive.async.services.TicketService
import info.senia.reactive.model.Ticket

import scala.concurrent.Future

class TicketServiceImpl extends TicketService {
  def byUserId(userId: Long): Future[Seq[Ticket]] = Future.successful(Seq(
    Ticket(userId * 100 + 1, "It`s my first ticket!"),
    Ticket(userId * 100 + 2, "It`s my second ticket!")
  ))
}
