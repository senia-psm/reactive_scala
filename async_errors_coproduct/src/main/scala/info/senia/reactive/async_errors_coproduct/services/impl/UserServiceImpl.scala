package info.senia.reactive.async_errors_coproduct.services.impl

import java.time.{LocalDate, Month}

import info.senia.reactive.async_errors_coproduct.services.UserService.{UserNotFound, UserServiceError}
import info.senia.reactive.async_errors_coproduct.services._
import info.senia.reactive.model.User
import shapeless.Coproduct

import scala.concurrent.Future

class UserServiceImpl extends UserService {
  def byEmailAddress(emailAddress: String): User fthrows UserServiceError = Future.successful(emailAddress match {
    case "John_Doe@ma.il" => Right(User(
      id = 1,
      firstName = "John",
      lastName = "Doe",
      dateOfBirth = LocalDate.of(1985, Month.NOVEMBER, 24),
      emailAddress = "John_Doe@ma.il"
    ))
    case _ => Left(Coproduct(UserNotFound(emailAddress)))
  })
}
