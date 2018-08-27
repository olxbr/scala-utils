name := "scala-utils"
version := "1.0.0"
scalaVersion := "2.12.6"
organization := "br.com.gzvr"
crossScalaVersions := Seq("2.11.8", "2.12.6")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

bintrayPackage := "scala-utils"
bintrayOrganization := Some("gzvr")

libraryDependencies ++= {
  Seq(
    "joda-time" % "joda-time" % "2.10",
    "org.json4s" %% "json4s-native" % "3.6.0",
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
  )
}
