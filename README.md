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

When you use `jetty-run`, rebel.xml is automatically generated and added to classpath.
For non-web projects you can mix JRebelPlugin and manually use the generate-rebel-xml task when appropriate.
