package fi.jawsy.sbtplugins.jrebel

import scala.xml._
import sbt._

object JRebelPlugin {
  def directory(path: Path) = if (path.exists) <dir name={path.absolutePath} /> else NodeSeq.Empty
}

trait JRebelPlugin extends Project {
  self: MavenStyleScalaPaths =>

  lazy val rebelInUse = List("jrebel.lic", "javarebel.lic").exists(this.getClass.getClassLoader.getResource(_) != null)

  lazy val generateRebelXml = task {
    val xml =
      <application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.zeroturnaround.com" xsi:schemaLocation="http://www.zeroturnaround.com/alderaan/rebel-2_0.xsd">
        { rebelXml }
      </application>
    FileUtilities.touch(rebelXmlPath, log)
    XML.saveFull(rebelXmlPath.absolutePath, xml, "UTF-8", true, null)
    None
  }

  def packageRebelXml = false

  def autogenerateRebelXml = rebelInUse

  def rebelXmlPath = rebelOutputPath / "rebel.xml"

  def rebelOutputPath = outputPath / "jrebel"

  def rebelXml: NodeSeq = {
    <classpath>
      { JRebelPlugin.directory(mainCompilePath) }
      { JRebelPlugin.directory(mainResourcesOutputPath) }
    </classpath>
  }

}
