package fi.jawsy.sbtplugins.jrebel

import sbt._
import sbt.CommandSupport.logger
import sbt.Keys._
import scala.xml._

object JRebelPlugin extends Plugin {

  lazy val jrebelDirectory = SettingKey[File]("jrebel-directory", "Output directory for rebel.xml")
  lazy val jrebelXml = SettingKey[File]("jrebel-xml", "Path for generated rebel.xml")
  lazy val jrebelClasspath = SettingKey[Seq[File]]("jrebel-classpath", "Reloadable JRebel classpath")

  lazy val jrebelConfig: Seq[Setting[_]] = Seq(
    jrebelDirectory <<= resourceManaged / "jrebel",
    jrebelXml <<= jrebelDirectory / "rebel.xml",
    jrebelClasspath <<= Seq(classDirectory in Compile, classDirectory in Test).join
  )

  override lazy val settings =
    (Seq(commands += generateRebelXml)) ++ jrebelConfig

  lazy val generateRebelXml =
    Command.command("generate-rebel-xml") { state =>
      val extracted = Project.extract(state)
      import extracted._

      def setting[A](key: SettingKey[A], configuration: Configuration = Compile): Option[A] = key in (currentRef, configuration) get
        structure.data

      def dirXml(dir: File) = <dir name={ dir.absolutePath } />

      (for (rebelXml <- setting(jrebelXml);
            jrebelClasspath <- setting(jrebelClasspath)) yield {
        val xml =
          <application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.zeroturnaround.com" xsi:schemaLocation="http://www.zeroturnaround.com/alderaan/rebel-2_0.xsd">
            <classpath>
             { jrebelClasspath.map(dirXml).toList }
            </classpath>
          </application>

        IO.touch(rebelXml)
        XML.save(rebelXml.absolutePath, xml, "UTF-8", true)

        logger(state).info("Wrote rebel.xml to %s".format(rebelXml.absolutePath))

        state
      }).getOrElse(state.fail)
    }
}
