organization := "fi.jawsy.sbtplugins"

name := "sbt-jrebel-plugin"

version := "0.9.0-SNAPSHOT"

sbtPlugin := true

publishTo <<= (version) { version: String =>
  val nexus = "https://jawsy.fi/nexus/content/repositories/"
	if (version.trim.endsWith("SNAPSHOT"))
		Some("Jawsy.fi M2 snapshots" at nexus + "snapshots")
	else
		Some("Jawsy.fi M2 releases" at nexus + "releases")
}
