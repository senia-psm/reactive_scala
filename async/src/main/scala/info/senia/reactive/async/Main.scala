package info.senia.reactive.async

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import info.senia.reactive.async.routes.UserRoute
import info.senia.reactive.async.services.{MarketingService, TicketService, UserService}

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("system")
    implicit val materializer = ActorMaterializer()
    import system.dispatcher

    class DI {
      import com.softwaremill.macwire._
      lazy val ticketService = wire[TicketService]
      lazy val userService = wire[UserService]
      lazy val marketingModule = wire[MarketingService]
      lazy val userRoute = wire[UserRoute]
    }

    Http().bindAndHandle(new DI().userRoute.route, "localhost", 8080)
  }

}
