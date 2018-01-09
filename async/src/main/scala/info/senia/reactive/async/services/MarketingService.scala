package info.senia.reactive.async.services

import info.senia.reactive.model.{Bonus, Ticket}

import scala.concurrent.Future

class MarketingService {
  def getBonuses(tickets: Seq[Ticket]): Future[Seq[Bonus]] = Future.successful(Seq(
    Bonus(1, "New year 10% bonus for tickets " + tickets),
    Bonus(2, "Birthday 15% bonus for tickets " + tickets),
    Bonus(3, "Random 5% bonus for tickets " + tickets)
  ))
}
