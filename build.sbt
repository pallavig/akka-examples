import scalariform.formatter.preferences._

name := "akka-stream-scala"

version := "1.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "io.reactivex" % "rxscala_2.11" % "0.25.0",
  "com.typesafe.akka" %% "akka-http-core-experimental" % "1.0-RC4",
  "com.typesafe.akka" %% "akka-http-experimental" % "1.0-RC4",
  "org.monifu" %%% "monifu" % "1.0-M1",
  "org.scala-js" % "scalajs-dom_sjs0.6_2.11" % "0.8.1",
  "org.scalatest" % "scalatest_2.11" % "3.0.0-M7"
)

//scalaJSStage in Global := FastOptStage

scalariformSettings
//enablePlugins(ScalaJSPlugin)

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)

fork in run := true
