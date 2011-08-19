package fi.jawsy.sbtplugins.jrebel

import sbt._
import sbt.CommandSupport.logger
import sbt.Keys._
import sbt.Scope.GlobalScope
import scala.xml._

object JRebelPlugin extends Plugin {
  val jrebelEnabled = SettingKey[Boolean]("jrebel-enabled")
  val jrebelClasspath = SettingKey[Seq[File]]("jrebel-classpath")
  val jrebelGenerate = TaskKey[Seq[File]]("jrebel-generate")
  val jrebelXml = SettingKey[File]("jrebel-xml")
  val jrebelWebLinks = SettingKey[Seq[File]]("jrebel-web-links")

  lazy val jrebelSettings: Seq[Project.Setting[_]] = Seq[Setting[_]](
    jrebelEnabled := List("jrebel.lic", "javarebel.lic").exists(this.getClass.getClassLoader.getResource(_) != null),
    jrebelClasspath <<= Seq(Keys.classDirectory in Compile, Keys.classDirectory in Test).join,
    jrebelWebLinks := Seq(),
    jrebelXml <<= (resourceManaged in Compile) { _ / "rebel.xml" },
    jrebelGenerate <<= jrebelXmlTask,
    resourceGenerators in Compile <+= jrebelGenerate.identity
  )

  private def toXml(dir: File) = <dir name={ dir.absolutePath } />

  private def webLinkXml(link: File) =
    <web>
      <link>
      { toXml(link) }
      </link>
    </web>

  private def jrebelXmlTask: Project.Initialize[Task[Seq[File]]] =
    (jrebelEnabled, jrebelClasspath, jrebelXml, jrebelWebLinks, state) map {
      (jrebelEnabled, jrebelClasspath, jrebelXml, jrebelWebLinks, state) =>
        if (!jrebelEnabled) Nil
        else {
          println(jrebelWebLinks)
          val xml =
            <application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.zeroturnaround.com" xsi:schemaLocation="http://www.zeroturnaround.com/alderaan/rebel-2_0.xsd">
              <classpath>
               { jrebelClasspath.map(toXml) }
              </classpath>
              {
                jrebelWebLinks.map(webLinkXml)
              }
            </application>

          IO.touch(jrebelXml)
          XML.save(jrebelXml.absolutePath, xml, "UTF-8", true)

          logger(state).info("Wrote rebel.xml to %s".format(jrebelXml.absolutePath))

          jrebelXml :: Nil
        }
    }
}
