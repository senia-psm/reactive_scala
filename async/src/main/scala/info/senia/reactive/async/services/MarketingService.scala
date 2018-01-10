package info.senia.reactive.async.services

import info.senia.reactive.model.{Bonus, Ticket}

import scala.concurrent.Future

trait MarketingService {
  def getBonuses(tickets: Seq[Ticket]): Future[Seq[Bonus]]
}
