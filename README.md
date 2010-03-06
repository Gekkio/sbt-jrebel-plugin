## Usage

**Make sure you run sbt with JRebel agent enabled**

Add to project/plugins/Plugins.scala and mix into your project definition:

    import fi.jawsy.sbtplugins.jrebel.JRebelProject
    import sbt._

    class SomeProject(info: ProjectInfo) extends DefaultWebProject(info) with JRebelProject {
    }

When you use `jetty-run`, rebel.xml is automatically generated and added to classpath.

## TODO

 - Split into separate parts for JAR/WAR projects
 - Improve configurability
