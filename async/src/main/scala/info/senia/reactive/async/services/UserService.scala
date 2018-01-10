package info.senia.reactive.async.services

import java.time.{LocalDate, Month}

import info.senia.reactive.model.User

import scala.concurrent.Future

trait UserService {
  def byEmailAddress(emailAddress: String): Future[User]
}
