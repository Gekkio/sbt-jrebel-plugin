sbt-jrebel-plugin
=================

## Introduction

sbt-jrebel-plugin is a plugin for [Simple Build Tool](http://code.google.com/p/simple-build-tool/) that generates configuration files (rebel.xml) for [JRebel](http://www.zeroturnaround.com/jrebel/). A rebel.xml is not always required but is recommended because if you don't have one, JRebel cannot understand the layout of your project and might fail to reload changes. You also cannot reload changes from separate projects.

## Features

+ Generates rebel.xml, so it's similar to javarebel-maven-plugin in the Maven world
+ Cross-project change reloading

_Default behaviour_

+ Disables itself if SBT isn't run with JRebel agent enabled
+ Writes rebel.xml as a managed resource which is automatically added to classpath *and artifacts*

__You should always disable sbt-jrebel-plugin when publishing artifacts somewhere else than locally. Otherwise your artifacts will include rebel.xml files__

## Usage

**Make sure you run sbt with JRebel agent enabled**

First, add the maven repository and the plugin declaration to project/plugins.sbt:

	resolvers += "Jawsy.fi M2 releases" at "http://oss.jawsy.fi/maven2/releases"

	addSbtPlugin("fi.jawsy.sbtplugins" %% "sbt-jrebel-plugin" % "0.9.0")

Then include the plugin settings in your project definition:

	seq(jrebelSettings: _*)

If you are using [xsbt-web-plugin](https://github.com/siasia/xsbt-web-plugin) and want to reload web resources, also add this:

version 0.1.x:

	jrebel.webLinks <+= temporaryWarPath

version 0.2.x:

	jrebel.webLinks <++= webappResources in Compile

### How do I ...?

#### Disable rebel.xml generation

Project definition:
`jrebel.enabled := false`

or in SBT console:
`set jrebel.enabled := false`

#### Force rebel.xml generation (regardless of whether you run SBT with JRebel enabled)

Project definition:
`jrebel.enabled := true`

or in SBT console:
`set jrebel.enabled := true`

## Cross-project change reloading

Let's say you have two projects, MyLib which is a library project and MyWebApp which is a webapp. These two are completely separate and differently versioned projects. If you have sbt-jrebel-plugin enabled for both projects, you can make changes in MyLib while developing MyWebApp and have them reloaded instantly.

+ Deploy MyLib with `sbt publish-local`. The package includes a rebel.xml file that contains the absolute paths to your MyLib project directories.
+ Update MyWebApp dependencies with `sbt update`. MyWebApp now uses the MyLib package that has a rebel.xml.
+ Run MyWebApp and enable continuous webapp compilation (for example I use `sbt jetty-run "~  prepare-webapp"`)
+ In a separate terminal, start continuous compilation for MyLib (for example, `sbt ~ compile`).
+ Change a class in MyLib
+ Notice that the change is visible in MyWebApp (if the change is reloadable using JRebel). Voilá!

*Do not share rebel.xml files because by default they contain absolute paths which are computer-specific!*

## TODO

Nothing at the moment! _Please report any bugs you might find_
