import sbt._
import Keys._

object ProjectBuild extends Build {

  val githubUser     = SettingKey[String]("github-user", "User for authenticated access to github.")
  val githubPassword = SettingKey[String]("github-password", "Password for authenticated access to github.")

  lazy val root = Project(id = "graph-github",
    base = file(".")
  )

}

