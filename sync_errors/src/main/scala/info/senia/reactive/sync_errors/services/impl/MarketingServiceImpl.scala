package info.senia.reactive.sync_errors.services.impl

import info.senia.reactive.model.{Bonus, Ticket}
import info.senia.reactive.sync_errors.services._

class MarketingServiceImpl extends MarketingService {
  def getBonuses(tickets: Seq[Ticket]): Seq[Bonus] throws MarketingServiceError = Right(Seq(
    Bonus(1, "New year 10% bonus for tickets " + tickets),
    Bonus(2, "Birthday 15% bonus for tickets " + tickets),
    Bonus(3, "Random 5% bonus for tickets " + tickets)
  ))
}
