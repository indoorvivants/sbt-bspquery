val pluginVersion = System.getProperty("plugin.version")

if (pluginVersion == null)
  throw new RuntimeException(
    """|The system property 'plugin.version' is not defined.
                                |Specify this property using the scriptedLaunchOpts -D.""".stripMargin
  )
else addSbtPlugin("com.indoorvivants" % """sbt-bspquery""" % pluginVersion)


addSbtPlugin("com.eed3si9n" % "sbt-projectmatrix" % "0.9.2")
addSbtPlugin("org.scala-js"     % "sbt-scalajs"      % "1.17.0")
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.5.5")
