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
  "-Xlint:missing-interpolator"
)

libraryDependencies ++= Seq(  
  "org.scalaz" %% "scalaz-core" % "7.2.14",
  "org.typelevel" %% "cats-core" % "1.0.0-MF",
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.scalatest" %% "scalatest" % "3.0.1"
)

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
)