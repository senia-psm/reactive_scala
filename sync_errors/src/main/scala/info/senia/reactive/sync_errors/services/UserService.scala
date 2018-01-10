package info.senia.reactive.sync_errors.services

import java.time.{LocalDate, Month}

import info.senia.reactive.model.User

sealed trait UserServiceError
case class UserNotFound(emailAddress: String) extends UserServiceError
trait UserService {
  def byEmailAddress(emailAddress: String): User throws UserServiceError
}
