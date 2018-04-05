scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  //  "-Ywarn-unused",
  "-Ywarn-value-discard"
)

lazy val commonSettings = Seq(
  organization := "strex",
  scalaVersion := "2.12.2"
)

lazy val commonDependencies = Seq(
  "org.scalaz" %% "scalaz-core" % "7.3.0-M13",
  "org.scalacheck" %% "scalacheck" % "1.13.5" % "test",
  "org.scalatest" %% "scalatest" % "3.0.3" % "test",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6"
)

lazy val ids = project.in(file("ids"))
  .settings(
    name := "ids",
    version := "0.1",
    commonSettings7 911.12" % "1.10.3",
      "org.scodec" % "scodec-bits_2.12" % "1.1.4"
    )
  )
lazy val probe = project.in(file("probe"))
  .settings(
    name := "probe",
    version := "0.1",
    commonSettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      "org.scodec" % "scodec-core_2.12" % "1.10.3",
      "org.scodec" % "scodec-bits_2.12" % "1.1.4"
    )
  )
