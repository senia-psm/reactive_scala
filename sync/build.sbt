name := "sync"
version := "0.0.1"
scalaVersion := "2.12.4"

lazy val akkaVersion = "10.0.11"
lazy val circleVersion = "0.9.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % Test,
  "de.heikoseeberger" %% "akka-http-circe" % "1.19.0",
  "com.softwaremill.macwire" %% "macros" % "2.3.0",
  "io.circe" %% "circe-generic" % circleVersion,
  "io.circe" %% "circe-java8" % circleVersion
)

