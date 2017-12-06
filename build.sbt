name := "ScalaSnippets"

version := "1.0"

scalaVersion := "2.12.3"

// https://tpolecat.github.io/2014/04/11/scalac-flags.html
// http://blog.codacy.com/2016/08/04/make-scala-compiler-review-code/
scalacOptions in ThisBuild ++= Seq(
  "-deprecation",
  "-explaintypes",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xmigration:2.11.0",
  "-Ywarn-dead-code",
  "-Ywarn-value-discard", // http://underscore.io/blog/posts/2016/11/24/value-discard.html
  "-Xlint:missing-interpolator",
  "-Yrangepos" // Enables better Goggles error messages
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.7",
  "org.scalaz" %% "scalaz-core" % "7.2.14",
  "org.typelevel" %% "cats-core" % "1.0.0-MF",
  "com.chuusai" %% "shapeless" % "2.3.2",
  "com.github.julien-truffaut" %%  "monocle-core"  % "1.4.0",
  "com.github.kenbot" %% "goggles-macros" % "1.0",
  "com.github.kenbot" %% "goggles-dsl" % "1.0",
  "com.lihaoyi" %% "fastparse" % "0.4.4",
  "joda-time" % "joda-time" % "2.9.9",
  "org.joda" % "joda-convert" % "1.8.3",
  "com.github.nscala-time" % "nscala-time_2.12" % "2.16.0",
  "org.scalatest" %% "scalatest" % "3.0.1"
)

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
)
