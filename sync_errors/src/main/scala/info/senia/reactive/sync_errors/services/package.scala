package info.senia.reactive.sync_errors

package object services {
  type throws[R, E] = Either[E, R]
}
