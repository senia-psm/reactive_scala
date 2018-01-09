package info.senia.reactive.async_errors.services

import java.time.{LocalDate, Month}

import info.senia.reactive.async_errors.services.UserService.{UserNotFound, UserServiceError}
import info.senia.reactive.model.User
import shapeless.{:+:, CNil, Coproduct}

import scala.concurrent.Future

class UserService {
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

object UserService {
  type UserServiceError = UserNotFound :+: AnotherError :+: CNil

  case class UserNotFound(emailAddress: String)
  case class AnotherError(msg: String)
}