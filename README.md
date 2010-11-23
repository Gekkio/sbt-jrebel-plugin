sbt-jrebel-plugin
=================

## Introduction

sbt-jrebel-plugin is a plugin for [Simple Build Tool](http://code.google.com/p/simple-build-tool/) that generates configuration files (rebel.xml) for [JRebel](http://www.zeroturnaround.com/jrebel/). A rebel.xml is not always required but is recommended because if you don't have one, JRebel cannot understand the layout of your project and might fail to reload changes. You also cannot reload changes from separate projects.

## Features

+ Generates rebel.xml, so it's similar to javarebel-maven-plugin in the Maven world
+ Support for jetty-run (JRebelWebPlugin trait)
+ Fully configurable through method overrides
+ Cross-project change reloading

_Default behaviour_

+ Disables itself if SBT isn't run with JRebel agent enabled
+ Writes rebel.xml to target/scala\_xx/jrebel/rebel.xml (or target/jrebel/rebel.xml if cross building is disabled)
+ (Web projects) Regenerates rebel.xml always before prepare-webapp is run
+ (JAR projects) Regenerates rebel.xml always before copy-resources is run
+ (Web projects) Includes rebel.xml only in Jetty classpath, so it doesn't end up in any artifacts

## Usage

**Make sure you run sbt with JRebel agent enabled**

First, add the maven repository and the plugin declaration to project/plugins/Plugins.scala:

    import sbt._

    class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
      val jawsyMavenReleases = "Jawsy.fi M2 releases" at "http://oss.jawsy.fi/maven2/releases"
      val jrebelPlugin = "fi.jawsy" % "sbt-jrebel-plugin" % "0.2.1"
    }


Then mix the plugin into your project definition:

_Web projects:_

    import fi.jawsy.sbtplugins.jrebel.JRebelWebPlugin
    import sbt._

    class SomeProject(info: ProjectInfo) extends DefaultWebProject(info) with JRebelWebPlugin {
    }

_JAR projects:_

    import fi.jawsy.sbtplugins.jrebel.JRebelJarPlugin
    import sbt._

    class SomeProject(info: ProjectInfo) extends DefaultProject(info) with JRebelJarPlugin {
    }

### How do I ...?

#### Disable rebel.xml generation

`override autogenerateRebelXml = false`

#### Force rebel.xml generation (regardless of whether you run SBT with JRebel enabled)

`override autogenerateRebelXml = true`

#### Package rebel.xml in JAR/WAR

`override packageRebelXml = true`

#### Write my own rebel.xml

`override rebelXml = <classpath> <!-- This is just an example --> </classpath> <web> <!-- Refer to JRebel documentation --> </web>`

## Cross-project change reloading

Let's say you have two projects, MyLib which is a library project and MyWebApp which is a webapp. These two are completely separate and differently versioned projects. If you have sbt-jrebel-plugin enabled for both projects, you can make changes in MyLib while developing MyWebApp and have them reloaded instantly.

+ Make sure at least MyLib has packageRebelXml enabled (`override def packageRebelXml = true`)
+ Deploy MyLib with `sbt publish-local`. The package includes a rebel.xml file that contains the absolute paths to your MyLib project directories.
+ Update MyWebApp dependencies with `sbt update`. MyWebApp now uses the MyLib package that has a rebel.xml.
+ Run MyWebApp and enable continuous webapp compilation (for example I use `sbt jetty-run "~  prepare-webapp"`)
+ In a separate terminal, start continuous compilation for MyLib (for example, `sbt ~ compile`).
+ Change a class in MyLib
+ Notice that the change is visible in MyWebApp (if the change is reloadable using JRebel). Voilá!

*Do not share rebel.xml files because by default they contain absolute paths which are computer-specific!*

## TODO

Nothing at the moment! _Please report any bugs you might find_
