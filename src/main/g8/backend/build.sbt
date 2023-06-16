// give the user a nice default project!
ThisBuild / organization := "com.do"
ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file(".")).settings(
  name := "Fullstack Scaffhold",
  mainClass := Some("com.do.Main"),
  libraryDependencies ++= Seq(
    "dev.zio" %% "zio" % "2.0.14",
    "dev.zio" %% "zio-http" % "3.0.0-RC1",
    "dev.zio" %% "zio-sql" % "0.1.2",
    "dev.zio" %% "zio-sql-postgres" % "0.1.2",
    "dev.zio" %% "zio-streams" % "1.0.12",
    "dev.zio" %% "zio-interop-cats" % "3.1.1.0",
    "org.mongodb.scala" %% "mongo-scala-driver" % "4.9.1",
    "org.mongodb.scala" %% "mongo-scala-bson" % "4.9.1"
  )
)
