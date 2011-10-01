package fi.jawsy.sbtplugins.jrebel

import sbt._
import sbt.CommandSupport.logger
import sbt.Keys._
import sbt.Scope.GlobalScope
import scala.xml._

object JRebelPlugin extends Plugin {
  object jrebel {
    val classpath = SettingKey[Seq[File]]("jrebel-classpath")
    val enabled = SettingKey[Boolean]("jrebel-enabled")
    val rebelXml = SettingKey[File]("jrebel-rebel-xml")
    val webLinks = SettingKey[Seq[File]]("jrebel-web-links")
  }

  val jrebelGenerate = TaskKey[Seq[File]]("jrebel-generate")

  val jrebelSettings: Seq[Project.Setting[_]] = Seq[Setting[_]](
    jrebel.classpath <<= Seq(Keys.classDirectory in Compile, Keys.classDirectory in Test).join,
    jrebel.enabled := (java.lang.Package.getPackage("com.zeroturnaround.javarebel") != null),
    jrebel.rebelXml <<= (resourceManaged in Compile) { _ / "rebel.xml" },
    jrebel.webLinks := Seq(),
    jrebelGenerate <<= rebelXmlTask,
    resourceGenerators in Compile <+= jrebelGenerate.identity
  )

  private def dirXml(dir: File) = <dir name={ dir.absolutePath } />

  private def webLinkXml(link: File) =
    <web>
      <link>
      { dirXml(link) }
      </link>
    </web>

  private def rebelXmlTask: Project.Initialize[Task[Seq[File]]] =
    (jrebel.enabled, jrebel.classpath, jrebel.rebelXml, jrebel.webLinks, state) map {
      (enabled, classpath, rebelXml, webLinks, state) =>
        if (!enabled) Nil
        else {
          val xml =
            <application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.zeroturnaround.com" xsi:schemaLocation="http://www.zeroturnaround.com/alderaan/rebel-2_0.xsd">
              <classpath>
               { classpath.map(dirXml) }
              </classpath>
              {
                webLinks.map(webLinkXml)
              }
            </application>

          IO.touch(rebelXml)
          XML.save(rebelXml.absolutePath, xml, "UTF-8", true)

          logger(state).info("Wrote rebel.xml to %s".format(rebelXml.absolutePath))

          rebelXml :: Nil
        }
    }
}
