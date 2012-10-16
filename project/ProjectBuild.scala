import sbt._
import Keys._

object ProjectBuild extends Build {

  val githubUser     = SettingKey[String]("github-user", "User for authenticated access to github.")
  val githubPassword = SettingKey[String]("github-password", "Password for authenticated access to github.")

  val demo = InputKey[Unit]("demo")
  val gitin = InputKey[Unit]("gitin")

  lazy val root = Project(id = "graph-github",
    base = file(".")
  ).settings(
    demo <<= inputTask { (argTask: TaskKey[Seq[String]]) =>
    // Here, we map the argument task `argTask`
    // and a normal setting `scalaVersion`
      (argTask, scalaVersion) map { (args: Seq[String], sv: String) =>

        println("The current Scala version is " + sv)
        println("The arguments to demo were:")
        args foreach println
      }
    },
    gitin <<= runInputTask(Compile, "org.neo4j.contrib.github.importer.GitHubGuide")

  )

}

