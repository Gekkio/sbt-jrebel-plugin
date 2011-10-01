organization := "fi.jawsy.sbtplugins"

name := "sbt-jrebel-plugin"

version := "0.9.0-SNAPSHOT"

sbtPlugin := true

publishTo := Some({
  if (version.toString.endsWith("-SNAPSHOT"))
    ("Nexus" at "https://www.jawsy.fi/nexus/content/repositories/snapshots")
    else ("Nexus" at "https://www.jawsy.fi/nexus/content/repositories/releases")
})
