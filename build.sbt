import scalariform.formatter.preferences._

name := """doce_tentacao_scala"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala).enablePlugins(SbtTwirl).enablePlugins(SbtWeb)


pipelineStages in Assets := Seq()

pipelineStages := Seq(uglify, digest, gzip)

DigestKeys.algorithms += "sha1"


libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  filters,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.7.play24",
  "org.webjars" % "bootstrap" % "3.1.1",
  "com.adrianhurt" %% "play-bootstrap3" % "0.4.4-P24",
  "org.apache.commons" % "commons-email" % "1.3.3",
  "com.typesafe.play" %% "play-mailer" % "3.0.1"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers += "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


//********************************************************
// Scalariform settings
//********************************************************

defaultScalariformSettings

// disable documentation generation
sources in(Compile, doc) := Seq.empty
// avoid to publish the documentation artifact
publishArtifact in(Compile, packageDoc) := false
parallelExecution in Test := true
fork in Test := false

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(FormatXml, false)
  .setPreference(DoubleIndentClassDeclaration, false)
  .setPreference(PreserveDanglingCloseParenthesis, true)
