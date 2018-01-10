package info.senia.reactive.sync

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import info.senia.reactive.sync.routes.UserRoute
import info.senia.reactive.sync.services.impl.{MarketingServiceImpl, TicketServiceImpl, UserServiceImpl}

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("system")
    implicit val materializer = ActorMaterializer()
    import system.dispatcher

    class DI {
      import com.softwaremill.macwire._
      lazy val ticketService = wire[TicketServiceImpl]
      lazy val userService = wire[UserServiceImpl]
      lazy val marketingModule = wire[MarketingServiceImpl]
      lazy val userRoute = wire[UserRoute]
    }

    Http().bindAndHandle(new DI().userRoute.route, "localhost", 8080)
  }

}
