package info.senia.reactive.async_errors_coproduct.services

import info.senia.reactive.async_errors_coproduct.services.UserService.UserServiceError
import info.senia.reactive.model.User
import shapeless.{:+:, CNil}

trait UserService {
  def byEmailAddress(emailAddress: String): User fthrows UserServiceError
}

object UserService {
  type UserServiceError = UserNotFound :+: AnotherError :+: CNil

  case class UserNotFound(emailAddress: String)
  case class AnotherError(msg: String)
}