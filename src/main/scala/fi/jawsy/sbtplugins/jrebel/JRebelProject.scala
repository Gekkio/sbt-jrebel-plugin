package fi.jawsy.sbtplugins.jrebel

import scala.xml._
import sbt._

trait JRebelProject extends DefaultWebProject {

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

  def autogenerateRebelXml = rebelInUse

  def rebelXmlPath = rebelOutputPath / "rebel.xml"

  def rebelOutputPath = outputPath / "jrebel"

  def rebelXml: NodeSeq = {
    <classpath>
      <dir name={mainCompilePath.absolutePath} />
      <dir name={mainResourcesOutputPath.absolutePath} />
    </classpath>
    <web>
      <link target="/">
        <dir name={webappPath.absolutePath} />
      </link>
    </web>
  }

  override def scanDirectories = if (rebelInUse) Nil else super.scanDirectories

  override def prepareWebappAction = {
    if (autogenerateRebelXml) super.prepareWebappAction dependsOn(generateRebelXml)
    else super.prepareWebappAction
  }

  override def webappClasspath = super.webappClasspath +++ rebelOutputPath

}
