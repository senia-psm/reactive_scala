package info.senia.reactive.sync_errors.services

import info.senia.reactive.model.{Bonus, Ticket}

sealed trait MarketingServiceError
trait MarketingService {
  def getBonuses(tickets: Seq[Ticket]): Seq[Bonus] throws MarketingServiceError
}
