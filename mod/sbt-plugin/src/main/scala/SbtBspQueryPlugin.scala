package bspquery.sbtplugin

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin
import sbtprojectmatrix.ProjectMatrixPlugin

object SbtBspQueryPlugin extends AutoPlugin {
  override def trigger = allRequirements

  override def requires = ProjectMatrixPlugin

  object autoImport {
    val bspQuery = settingKey[String]("bsp query")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    bspQuery := {
      import java.nio.file.Files
      val path = (ThisBuild / baseDirectory).value / ".bspquery"
      if (Files.exists(path.toPath)) {
        IO.read(path)
      } else ""
    },
    bspEnabled := {
      val query = bspquery.core.QueryAST.parse(bspQuery.value.trim()).get
      ProjectMatrixPlugin.autoImport.virtualAxes.?.value match {
        case Some(axes) =>
          val platform =
            if (axes.contains(VirtualAxis.native)) "sn"
            else if (axes.contains(VirtualAxis.js)) "sjs"
            else "jvm"
          val attrs = Map(
            "scalaBinary" -> scalaBinaryVersion.value,
            "scala" -> scalaVersion.value,
            "platform" -> platform
          )
          bspquery.core.Evaluator.evaluate(query, attrs).get
        case None => bspEnabled.value
      }

    }
  )

  override lazy val buildSettings = Seq()

  override lazy val globalSettings = Seq(
  )
}
