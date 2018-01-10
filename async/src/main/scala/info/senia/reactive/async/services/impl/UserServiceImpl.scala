package info.senia.reactive.async.services.impl

import java.time.{LocalDate, Month}

import info.senia.reactive.async.services.UserService
import info.senia.reactive.model.User

import scala.concurrent.Future

class UserServiceImpl extends UserService {
  def byEmailAddress(emailAddress: String): Future[User] = Future.successful(emailAddress match {
    case "John_Doe@ma.il" => User(
      id = 1,
      firstName = "John",
      lastName = "Doe",
      dateOfBirth = LocalDate.of(1985, Month.NOVEMBER, 24),
      emailAddress = "John_Doe@ma.il"
    )
    case _ => sys.error(s"Unknown email address: $emailAddress")
  })
}
