name := """play-java"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
   "mysql" % "mysql-connector-java" % "5.1.18",
  javaEbean,
  cache,
  javaWs,
   "com.twilio.sdk" % "twilio-java-sdk" % "3.4.1"
)
