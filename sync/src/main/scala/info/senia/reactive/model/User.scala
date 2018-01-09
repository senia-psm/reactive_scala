package info.senia.reactive.model

import java.time.LocalDate

case class User(
    id: Long,
    firstName: String,
    lastName: String,
    dateOfBirth: LocalDate,
    emailAddress: String
)

case class Ticket(id: Long, description: String)
case class Bonus(id: Long, description: String)