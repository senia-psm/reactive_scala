package info.senia.reactive.async_errors.services

import info.senia.reactive.model.User

sealed trait UserServiceError
case class UserNotFound(emailAddress: String) extends UserServiceError
trait UserService {
  def byEmailAddress(emailAddress: String): User fthrows UserServiceError
}
