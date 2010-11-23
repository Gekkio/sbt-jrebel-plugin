package fi.jawsy.sbtplugins.jrebel

import sbt._

trait JRebelJarPlugin extends DefaultProject with JRebelPlugin {

  override def copyResourcesAction = super.copyResourcesAction dependsOn(generateRebelXml)
  override def compileAction = super.compileAction dependsOn(generateRebelXml)

  override def packagePaths = if (packageRebelXml) super.packagePaths +++ ((rebelOutputPath ##) ***) else super.packagePaths

}
