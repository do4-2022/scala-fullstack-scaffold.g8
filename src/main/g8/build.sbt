// give the user a nice default project!
ThisBuild / organization := "com.do"
ThisBuild / scalaVersion := "2.12.8"

lazy val root = (project in file(".")).
  settings(
    name := "Fullstack Scaffhold",
    mainClass := Some("com.do.Main")
  )
