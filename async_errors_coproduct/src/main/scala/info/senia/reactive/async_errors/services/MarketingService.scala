package info.senia.reactive.async_errors.services

import info.senia.reactive.async_errors.services.MarketingService.MarketingServiceError
import info.senia.reactive.model.{Bonus, Ticket}
import shapeless.{:+:, CNil}

import scala.concurrent.Future

class MarketingService {
  def getBonuses(tickets: Seq[Ticket]): Seq[Bonus] fthrows MarketingServiceError = Future.successful(Right(Seq(
    Bonus(1, "New year 10% bonus for tickets " + tickets),
    Bonus(2, "Birthday 15% bonus for tickets " + tickets),
    Bonus(3, "Random 5% bonus for tickets " + tickets)
  )))
}

object MarketingService {
  type MarketingServiceError = /*UnexpectedError :+:*/ CNil
//  case class UnexpectedError(msg: String)
}