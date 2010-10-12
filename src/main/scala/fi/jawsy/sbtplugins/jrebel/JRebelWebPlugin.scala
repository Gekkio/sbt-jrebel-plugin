package fi.jawsy.sbtplugins.jrebel

import sbt._
import scala.xml._

trait JRebelWebPlugin extends DefaultWebProject with JRebelPlugin {

  override def scanDirectories = if (rebelInUse) Nil else super.scanDirectories

  override def prepareWebappAction = {
    if (autogenerateRebelXml) super.prepareWebappAction dependsOn(generateRebelXml)
    else super.prepareWebappAction
  }

  override def jettyRunClasspath = super.jettyRunClasspath +++ rebelOutputPath

  override def rebelXml: NodeSeq = super.rebelXml ++
    <web>
      <link target="/">
        { JRebelPlugin.directory(webappPath) }
      </link>
    </web>

}
