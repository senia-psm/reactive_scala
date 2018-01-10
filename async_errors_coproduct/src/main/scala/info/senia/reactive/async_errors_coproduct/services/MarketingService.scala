package info.senia.reactive.async_errors_coproduct.services

import info.senia.reactive.async_errors_coproduct.services.MarketingService.MarketingServiceError
import info.senia.reactive.model.{Bonus, Ticket}
import shapeless.CNil

trait MarketingService {
  def getBonuses(tickets: Seq[Ticket]): Seq[Bonus] fthrows MarketingServiceError
}

object MarketingService {
  type MarketingServiceError = /*UnexpectedError :+:*/ CNil
//  case class UnexpectedError(msg: String)
}