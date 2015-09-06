import scalariform.formatter.preferences._

name := """doce_tentacao_scala"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala).enablePlugins(SbtTwirl).enablePlugins(SbtWeb)


pipelineStages in Assets := Seq()

pipelineStages := Seq(uglify, digest, gzip)

DigestKeys.algorithms += "sha1"

UglifyKeys.uglifyOps := { js =>
  Seq((js.sortBy(_._2), "concat.min.js"))
}

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  filters,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.6.play24"
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
  