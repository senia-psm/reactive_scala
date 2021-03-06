package info.senia.reactive.sync_errors.services.impl

import java.time.{LocalDate, Month}

import info.senia.reactive.model.User
import info.senia.reactive.sync_errors.services._

class UserServiceImpl extends UserService {
  def byEmailAddress(emailAddress: String): User throws UserServiceError = emailAddress match {
    case "John_Doe@ma.il" => Right(User(
      id = 1,
      firstName = "John",
      lastName = "Doe",
      dateOfBirth = LocalDate.of(1985, Month.NOVEMBER, 24),
      emailAddress = "John_Doe@ma.il"
    ))
    case _ => Left(UserNotFound(emailAddress))
  }
}
