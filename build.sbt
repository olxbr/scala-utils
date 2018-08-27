name := "scala-utils"
version := "1.1.0"
scalaVersion := "2.12.6"
organization := "br.com.gzvr"
crossScalaVersions := Seq("2.11.8", "2.12.6")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/grupozap/scala-utils"))

scmInfo := Some(ScmInfo(
  url("https://github.com/grupozap/scala-utils"),
  "git@github.com:grupozap/scala-utils.git")
)

developers ++= List(
  Developer(
    "thiagoandrade6",
    "Thiago Pereira",
    "thiagoandrade6@gmail.com",
    url("https://github.com/thiagoandrade6")
  ),
  Developer(
    "resilva87",
    "Renato Silva",
    "resilva87@outlook.com",
    url("https://github.com/resilva87")
  )
)

pomIncludeRepository := (_ => false)

bintrayPackage := "scala-utils"
bintrayOrganization := Some("gzvr")

libraryDependencies ++= {
  Seq(
    "com.typesafe" % "config" % "1.3.2",
    "joda-time" % "joda-time" % "2.10",
    "org.json4s" %% "json4s-native" % "3.6.0",
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
  )
}
