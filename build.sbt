name := "ScalaSnippets"

version := "1.0"

scalaVersion := "2.13.1"

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
  "-Xlint:missing-interpolator"
)

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-lang3" % "3.10",
  "org.apache.commons" % "commons-text" % "1.8",
  "commons-io" % "commons-io" % "2.6",
  "com.typesafe.play" %% "play-json" % "2.8.1",
  "org.apache.commons" % "commons-rng-simple" % "1.3",
  "com.typesafe.akka" %% "akka-actor" % "2.6.4",
  "com.typesafe.akka" %% "akka-stream" % "2.6.4",
  "com.typesafe.akka" %% "akka-testkit" % "2.6.4",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.6.4",
  "org.typelevel" %% "cats-core" % "2.1.1",
  "org.typelevel" %% "cats-effect" % "2.1.2",
  "dev.zio" %% "zio" % "1.0.0-RC18-2",
  "dev.zio" %% "zio-test" % "1.0.0-RC18-2",
  "dev.zio" %% "zio-test-sbt" % "1.0.0-RC18-2",
  "dev.zio" %% "zio-interop-cats" % "2.0.0.0-RC11",
  "com.chuusai" %% "shapeless" % "2.3.3",
  "com.github.julien-truffaut" %%  "monocle-core"  % "2.0.4",
  "com.github.julien-truffaut" %%  "monocle-macro" % "2.0.4",
  "com.lihaoyi" %% "fastparse" % "2.2.4",
  "joda-time" % "joda-time" % "2.10.5",
  "com.softwaremill.sttp.client" %% "core" % "2.0.7",
  "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % "2.0.7",
  "com.softwaremill.sttp.client" %% "async-http-client-backend-zio-streams" % "2.0.7",
  "com.softwaremill.quicklens" %% "quicklens" % "1.5.0",
  "org.scalatest" %% "scalatest" % "3.0.8"
)

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")