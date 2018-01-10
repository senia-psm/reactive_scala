package info.senia.reactive.sync.services

import info.senia.reactive.model.{Bonus, Ticket}

trait MarketingService {
  def getBonuses(tickets: Seq[Ticket]): Seq[Bonus]
}
