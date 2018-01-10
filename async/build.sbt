name := "async"
version := "0.0.1"
scalaVersion := "2.12.4"

scalacOptions += "-Xfatal-warnings"

lazy val akkaVersion = "10.0.11"
lazy val circleVersion = "0.9.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % Test,
  "de.heikoseeberger" %% "akka-http-circe" % "1.19.0",
  "com.softwaremill.macwire" %% "macros" % "2.3.0",
  "io.circe" %% "circe-generic" % circleVersion,
  "io.circe" %% "circe-java8" % circleVersion,
  "com.thoughtworks.each" %% "each" % "3.3.1",
  "org.scala-lang.modules" % "scala-async_2.12" % "0.9.7"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
