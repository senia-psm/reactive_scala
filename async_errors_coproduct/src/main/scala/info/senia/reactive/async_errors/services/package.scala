package info.senia.reactive.async_errors

import scala.concurrent.Future

package object services {
  type throws[R, E] = Either[E, R]
  type fthrows[R, E] = Future[R throws E]
}
