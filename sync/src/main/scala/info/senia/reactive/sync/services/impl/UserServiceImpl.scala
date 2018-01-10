package info.senia.reactive.sync.services.impl

import java.time.{LocalDate, Month}

import info.senia.reactive.model.User
import info.senia.reactive.sync.services.UserService

class UserServiceImpl extends UserService {
  def byEmailAddress(emailAddress: String): User = emailAddress match {
    case "John_Doe@ma.il" => User(
      id = 1,
      firstName = "John",
      lastName = "Doe",
      dateOfBirth = LocalDate.of(1985, Month.NOVEMBER, 24),
      emailAddress = "John_Doe@ma.il"
    )
    case _ => sys.error(s"Unknown email address: $emailAddress")
  }
}
