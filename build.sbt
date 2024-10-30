inThisBuild(
  List(
    homepage := Some(url("https://github.com/indoorvivants/sbt-bsp-query")),
    licenses := List(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    developers := List(
      Developer(
        "indoorvivants",
        "Anton Sviridov",
        "contact@indoorvivants.com",
        url("https://blog.indoorvivants.com")
      )
    ),
    version := (if (!sys.env.contains("CI")) "dev" else version.value)
  )
)

lazy val publishing = Seq(
  organization := "com.indoorvivants",
  sonatypeProfileName := "com.indoorvivants"
)

val V = new {
  val scala213 = "2.13.12"
  val scala212 = "2.12.18"
  val munit = "1.0.2"
}

lazy val root =
  project
    .in(file("."))
    .aggregate(core.projectRefs *)
    .aggregate(`sbt-plugin`)
    .settings(publish / skip := true, publishLocal / skip := true)

lazy val core = projectMatrix
  .in(file("mod/core"))
  .jvmPlatform(scalaVersions = Seq(V.scala212, V.scala213))
  .settings(publishing)
  .settings(
    name := "bspquery-core",
    libraryDependencies += "org.scalameta" %% "munit" % V.munit % Test
  )

lazy val `sbt-plugin` = project
  .in(file("mod/sbt-plugin"))
  .dependsOn(core.jvm(V.scala212))
  .enablePlugins(ScriptedPlugin, SbtPlugin)
  .settings(publishing)
  .settings(
    addSbtPlugin("com.eed3si9n" % "sbt-projectmatrix" % "0.9.2"),
    name := "sbt-bspquery",
    sbtPlugin := true,
    // set up 'scripted; sbt plugin for testing sbt plugins
    scriptedLaunchOpts ++= Seq(
      "-Xmx1024M",
      "-Dplugin.version=" + version.value
    ),
    scriptedBufferLog := false
  )

addCommandAlias("ci", "test;scripted;scalafmtCheckAll")
