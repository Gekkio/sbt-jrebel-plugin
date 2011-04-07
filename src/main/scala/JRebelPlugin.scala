package fi.jawsy.sbtplugins.jrebel

import sbt._
import sbt.CommandSupport.logger
import scala.xml._

object JRebelPlugin extends Plugin {

  override lazy val settings =
    (Seq(Keys.commands += generateRebelXml))

  lazy val jrebelInUse = List("jrebel.lic", "javarebel.lic").exists(this.getClass.getClassLoader.getResource(_) != null)
  lazy val generateRebelXml =
    Command.command("generate-rebel-xml") { state =>
      val extracted = Project.extract(state)
      import extracted._

      def setting[A](key: SettingKey[A], configuration: Configuration = Compile): Option[A] = key in (currentRef, configuration) get
        structure.data

      def xml(dir: File) = if (dir.exists) <dir name={ dir.absolutePath } /> else NodeSeq.Empty

      val compileClasses = setting(Keys.classDirectory).map(xml)
      val testClasses = setting(Keys.classDirectory, Test).map(xml)

      val outputDirectory = setting(Keys.generatedResourceDirectory)

      (for (outputDirectory <- setting(Keys.generatedResourceDirectory)) yield {
        val xml =
          <application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.zeroturnaround.com" xsi:schemaLocation="http://www.zeroturnaround.com/alderaan/rebel-2_0.xsd">
            <classpath>
             { compileClasses.toList ::: testClasses.toList }
            </classpath>
          </application>

        val rebelXml = outputDirectory / "rebel.xml"

        IO.touch(rebelXml)
        XML.save(rebelXml.absolutePath, xml, "UTF-8", true)

        logger(state).info("Wrote rebel.xml to %s".format(rebelXml.absolutePath))

        state
      }).getOrElse(state.fail)
    }
}
