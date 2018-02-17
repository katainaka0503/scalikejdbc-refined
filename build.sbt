name := "scalikejdbc-refined"

version := "0.1"

crossScalaVersions := Seq("2.12.4", "2.11.12")

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "3.2.1",
  "eu.timepit" %% "refined" % "0.8.7",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.scalamock" %% "scalamock" % "4.0.0" % Test
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture"
)

addCommandAlias("ci-check", "; clean; scalafmtCheck; test:scalafmtCheck; test")
