## Features

+ Generates rebel.xml, so it's similar to javarebel-maven-plugin in the Maven world
+ Support for jetty-run (JRebelWebPlugin trait)
+ Fully configurable through method overrides

_Default behaviour_

+ Disables itself if SBT isn't run with JRebel agent enabled
+ Writes rebel.xml to target/scala_xx/jrebel/rebel.xml (or target/jrebel/rebel.xml if cross building is disabled)
+ Regenerates rebel.xml always before prepare-webapp is run
+ Includes rebel.xml only in Jetty classpath, so it doesn't end up in any artifacts

## Usage

**Make sure you run sbt with JRebel agent enabled**

First, add the maven repository and the plugin declaration to project/plugins/Plugins.scala:

    import sbt._

    class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
      val jawsyMavenReleases = "Jawsy.fi M2 releases" at "http://oss.jawsy.fi/maven2/releases"
      val jrebelPlugin = "fi.jawsy" % "sbt-jrebel-plugin" % "0.1.0"
    }


Then mix the plugin into your project definition:

    import fi.jawsy.sbtplugins.jrebel.JRebelWebPlugin
    import sbt._

    class SomeProject(info: ProjectInfo) extends DefaultWebProject(info) with JRebelWebPlugin {
    }
