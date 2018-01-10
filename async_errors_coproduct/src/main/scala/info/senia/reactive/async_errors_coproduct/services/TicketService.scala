package info.senia.reactive.async_errors_coproduct.services

import info.senia.reactive.async_errors_coproduct.services.TicketService.TicketServiceError
import info.senia.reactive.model.Ticket
import shapeless.{:+:, CNil}

trait TicketService {
  def byUserId(userId: Long): Seq[Ticket] fthrows TicketServiceError
}

object TicketService {
  case class UserNotFound(userId: Long)
  type TicketServiceError = UserNotFound :+: CNil
}