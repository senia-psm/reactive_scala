package info.senia.reactive.sync_errors.services

import info.senia.reactive.model.{Bonus, Ticket}

sealed trait MarketingServiceError
class MarketingService {
  def getBonuses(tickets: Seq[Ticket]): Seq[Bonus] throws MarketingServiceError = Right(Seq(
    Bonus(1, "New year 10% bonus for tickets " + tickets),
    Bonus(2, "Birthday 15% bonus for tickets " + tickets),
    Bonus(3, "Random 5% bonus for tickets " + tickets)
  ))
}
