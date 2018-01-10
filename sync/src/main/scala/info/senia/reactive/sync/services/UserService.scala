package info.senia.reactive.sync.services

import java.time.{LocalDate, Month}

import info.senia.reactive.model.User

trait UserService {
  def byEmailAddress(emailAddress: String): User
}
