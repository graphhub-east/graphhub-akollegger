package org.neo4j.contrib.github.importer

import org.specs2.mutable._
import org.specs2.main.Arguments
import org.neo4j.kernel.EmbeddedGraphDatabase
import org.eclipse.egit.github.core.client.GitHubClient
import org.neo4j.contrib.github.importer.GitHubGuide._
import java.io.{PrintStream, ByteArrayOutputStream}

/**
 */
class GitHubGuideSpec(args:Arguments) extends Specification {

val github = new GitHubClient()
val username = args.commandLine.value("github.user")
val password = args.commandLine.value("github.password")
github.setCredentials(username.get, password.get)

  "As a visitor guide, GitHubGuide" should {
    "visit a known user FOLLOWS relationship" in {
      val baos = new ByteArrayOutputStream()

      val visitor = new GitHubPrinter(new PrintStream(baos))
      GitHubGuide(github,1).guide(visitor)

      baos.toString must contain("(akollegger)-[:FOLLOWS]->(jakewins)")

    }

  }
}
