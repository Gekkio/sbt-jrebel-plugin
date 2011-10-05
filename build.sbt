organization := "fi.jawsy.sbtplugins"

organizationName := "Jawsy Solutions"

organizationHomepage := Some(new URL("http://jawsy.fi"))

name := "sbt-jrebel-plugin"

version := "0.9.0"

sbtPlugin := true

homepage := Some(new URL("http://github.com/Gekkio/sbt-jrebel-plugin"))

licenses += ("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))

publishTo <<= (version) { version: String =>
  val nexus = "https://jawsy.fi/nexus/content/repositories/"
	if (version.trim.endsWith("SNAPSHOT"))
		Some("Jawsy.fi M2 snapshots" at nexus + "snapshots")
	else
		Some("Jawsy.fi M2 releases" at nexus + "releases")
}
