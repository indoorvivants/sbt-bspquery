val V = new {
  val supportedScalaVersions = 
    Seq("2.13.12", "2.12.20", "3.3.4")
}

lazy val core = 
  projectMatrix
    .jvmPlatform(scalaVersions = V.supportedScalaVersions)
    .jsPlatform(scalaVersions = V.supportedScalaVersions)
    .nativePlatform(scalaVersions = V.supportedScalaVersions)

lazy val lib = 
  projectMatrix
    .jvmPlatform(scalaVersions = V.supportedScalaVersions)
    .jsPlatform(scalaVersions = V.supportedScalaVersions)
    .nativePlatform(scalaVersions = V.supportedScalaVersions)
