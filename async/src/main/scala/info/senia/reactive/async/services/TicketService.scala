package info.senia.reactive.async.services

import info.senia.reactive.model.Ticket

import scala.concurrent.Future

trait TicketService {
  def byUserId(userId: Long): Future[Seq[Ticket]]
}
