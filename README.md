## Usage

**Make sure you run sbt with JRebel agent enabled**

Add to project/plugins/Plugins.scala and mix into your project definition:

    import fi.jawsy.sbtplugins.jrebel.JRebelWebPlugin
    import sbt._

    class SomeProject(info: ProjectInfo) extends DefaultWebProject(info) with JRebelWebPlugin {
    }

When you use `jetty-run`, rebel.xml is automatically generated and added to classpath.
For non-web projects you can mix JRebelPlugin and manually use the generate-rebel-xml task when appropriate.

## TODO

 - Improve configurability
