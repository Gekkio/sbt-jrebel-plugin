# sbt-jrebel-plugin changelog

## 0.10.0 (Sep 2, 2013)

First release for SBT 0.13.

+ Changed groupId/organization to "fi.gekkio.sbtplugins"
+ Releases are deployed to Maven central

## 0.9.0 (Oct 1, 2011)

First release for SBT 0.9+.

This version targets SBT 0.11.0. Please note that SBT 0.9+ is radically different and this plugin has been rewritten.

Bug fixes:

+ JRebel detection works with 4.5.0 release

## 0.2.1 (Nov 23, 2010)

This release fixes a minor bug.

Bug fixes:

+ Automatic rebel.xml generation now works for JAR projects with no files in src/main/resources

## 0.2.0 (Oct 12, 2010)

This release improves the plugin greatly and fixes all known bugs.

New features:

+ Restructured project into separate JAR/WAR parts
+ Configurable rebel.xml -inclusion for JAR/WAR projects

Bug fixes:

+ Nonexistent directories are no longer added to rebel.xml

## 0.1.0 (Mar 29, 2010)

This is the first release.

Features:

+ Basic rebel.xml generation for WAR projects
+ Generates rebel.xml by default only if SBT is run with JRebel
