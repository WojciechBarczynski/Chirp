ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"
val circeVersion = "0.14.5"

lazy val root = (project in file("."))
  .settings(
    name := "Chirp",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "cask" % "0.9.1",
      "com.softwaremill.sttp.client4" %% "core" % "4.0.0-M1",
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion
    )
  )
