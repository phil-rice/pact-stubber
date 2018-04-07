import sbt.Keys.publishArtifact

//crossScalaVersions := Seq("2.12.4", "2.11.11")
//
val scalaTestVersion = "3.0.3"
val scalaPactVersion = "2.2.3"
val mockitoVersion = "1.10.19"
val http4sVersion = "0.16.6a"
val wiremockVersion = "1.56"
val typeSafeConfigVersion = "1.3.1"


lazy val settings = Seq(
  organization := "one.xingyi",
  //  scalaVersion := "2.10.6",
  sbtVersion := "1.1.0",
  scalaVersion := "2.12.4",
  //scalaVersion := "2.11.11"
  //  sbtPlugin := true,
  libraryDependencies ++= Seq(
    //  "jline" % "jline" % "2.11"

    "com.itv" %% "scalapact-http4s-0-16-2a" % scalaPactVersion,
    //    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    //    "org.http4s" %% "http4s-blaze-client" % http4sVersion,
    //    "org.http4s" %% "http4s-dsl" % http4sVersion,
    // https://mvnrepository.com/artifact/com.itv/scalapact-argonaut-6-2
    "com.itv" %% "scalapact-argonaut-6-2" % scalaPactVersion,
    "com.itv" %% "scalapact-shared" % scalaPactVersion,
    "com.itv" %% "scalapact-scalatest" % scalaPactVersion,
    "com.github.tomakehurst" % "wiremock" % wiremockVersion,
    "com.typesafe" % "config" % typeSafeConfigVersion
  ),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
    "org.mockito" % "mockito-all" % mockitoVersion % Test
  ))

lazy val publishSettings = settings ++ Seq(
  pomIncludeRepository := { _ => false },
  publishMavenStyle := true,
  publishArtifact in Test := false,
  licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php")),
  homepage := Some(url("http://example.com")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/phil-rice/pact-stubber"),
      "scm:git@github.com/phil-rice/pact-stubber.git"
    )
  ),
  developers := List(
    Developer(
      id = "phil",
      name = "Phil Rice",
      email = "phil.rice@iee.org",
      url = url("https://www.linkedin.com/in/phil-rice-53959460")
    )
  ),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  })

lazy val pactStubber = (project in file("modules/pactStubber")).settings(publishSettings: _*)
lazy val pactStubberPlugin = (project in file("modules/pactStubberPlugin")).settings(publishSettings: _*).settings(name := "pact-stubber-plugin", sbtPlugin := true).dependsOn(pactStubber).aggregate(pactStubber)
lazy val pactStubberRoot = (project in file(".")).settings(publishSettings: _*).settings(name := "pact-stubber-root", publishArtifact := false).aggregate(pactStubber, pactStubberPlugin)
//    name := "my-sbt-plugin",
//    version := "0.2.0",
//    organization := "com.github.miloszpp",
//    scalaVersion := "2.10.6",
//    sbtVersion := "0.13.11"
//    sbtPlugin := true,
//  )