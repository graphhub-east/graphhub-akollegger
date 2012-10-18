package org.neo4j.contrib.github.importer

import org.specs2.mutable._
import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.UserService
import org.specs2.main.Arguments
import org.neo4j.kernel.EmbeddedGraphDatabase
import org.specs2.specification.AfterExample
import java.io.File
import org.neo4j.kernel.impl.util.FileUtils

class Neo4jImportVisitorSpec(args:Arguments) extends Specification {

  val username = args.commandLine.value("github.user")
  val password = args.commandLine.value("github.password")

  val graphdbLocation = username.get+".graphdb"
  val graphdb = new EmbeddedGraphDatabase(graphdbLocation)
  val github = new GitHubClient()
  github.setCredentials(username.get, password.get)


  "The Neo4jImportVisitor" should {
    "index imported users" in {
      val login = username.get
      val neo4jVisitor = new Neo4jImportVisitor(graphdb)
      GitHubGuide(github,3).guide(neo4jVisitor)
      val foundUser = neo4jVisitor.userIndex.get("login", login).getSingle
      foundUser.getProperty("login").asInstanceOf[String] mustEqual login
    }

  }

  step({
    graphdb.shutdown()
  })
}
