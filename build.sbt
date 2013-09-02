organization := "fi.gekkio.sbtplugins"

name := "sbt-jrebel-plugin"

version := "0.10.0-SNAPSHOT"

sbtPlugin := true

homepage := Some(url("http://github.com/Gekkio/sbt-jrebel-plugin"))

licenses += ("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

pomExtra := (
  <scm>
    <connection>scm:git:git@github.com:gekkio/sbt-jrebel-plugin.git</connection>
    <developerConnection>scm:git:git@github.com:gekkio/sbt-jrebel-plugin.git</developerConnection>
    <url>git@github.com:gekkio/sbt-jrebel-plugin.git</url>
  </scm>
  <developers>
    <developer>
      <id>gekkio</id>
      <name>Joonas Javanainen</name>
      <email>joonas.javanainen@gmail.com</email>
      <timezone>+2</timezone>
    </developer>
  </developers>
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
