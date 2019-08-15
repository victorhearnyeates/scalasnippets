name := "ScalaSnippets"

version := "1.0"

scalaVersion := "2.12.8"

// https://tpolecat.github.io/2014/04/11/scalac-flags.html
// http://blog.codacy.com/2016/08/04/make-scala-compiler-review-code/
scalacOptions in ThisBuild ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-explaintypes", // Explain type errors in more detail.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-language:higherKinds", // Allow higher-kinded types
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Xfatal-warnings", // Fail the compilation if there are any warnings.
  "-Xmigration:2.11.0",
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-value-discard", // Annoying or useful?
  "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
  "-Ywarn-unused:imports", // Disabled due to Play unused imports but useful to temp re-enable to check
  "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
  "-Ywarn-unused:locals", // Warn if a local definition is unused.
  "-Ywarn-unused:params", // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates", // Warn if a private member is unused.
  "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
  "-Xlint:missing-interpolator",
  "-Ypartial-unification",
  "-Yrangepos" // Enables better Goggles error messages
)

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-lang3" % "3.9",
  "org.apache.commons" % "commons-text" % "1.7",
  "org.apache.commons" % "commons-rng-simple" % "1.2",
  "com.typesafe.akka" %% "akka-actor" % "2.5.7",
  "com.typesafe.akka" %% "akka-stream" % "2.5.13",
  "org.scalaz" %% "scalaz-core" % "7.2.14",
  "org.typelevel" %% "cats-core" % "1.6.0",
  "com.chuusai" %% "shapeless" % "2.3.3",
  "com.github.julien-truffaut" %%  "monocle-core"  % "1.5.0",
  "com.github.kenbot" %% "goggles-macros" % "1.0",
  "com.github.kenbot" %% "goggles-dsl" % "1.0",
  "com.lihaoyi" %% "fastparse" % "2.1.0",
  "joda-time" % "joda-time" % "2.9.9",
  "org.joda" % "joda-convert" % "1.8.3",
  "com.github.nscala-time" % "nscala-time_2.12" % "2.16.0",
  "org.scalatest" %% "scalatest" % "3.0.5"
)

resolvers ++= Seq(
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
)
