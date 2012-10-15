package org.neo4j.contrib.github

import org.specs2.mutable._

import scala.collection.JavaConversions._

import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.{UserService, RepositoryService, CollaboratorService}
import org.eclipse.egit.github.core.Repository
import org.specs2.main.Arguments

/**
 * "Tests" for exploring the Github API library.
 */
class GithubApiSpec(args: Arguments) extends Specification {

  val username = args.commandLine.value("github.user")
  val password = args.commandLine.value("github.password")

  "The Github API library" should {
    "authenticate using user and password" in {
      //Basic authentication
      val github = new GitHubClient()
      github.setCredentials(username.get, password.get)
      github.getUser mustEqual(username.get)
    }
  }
  "The Github API RepositoryService" should {
    "find a well-known user repository" in {
      val repoService = new RepositoryService()
      repoService.getRepositories("akollegger").toList must have(r => r.getName == "community")
    }

  }
  "The Github API UserService" should {
    "know the full name of a user" in {
      val userService = new UserService()
      userService.getUser("akollegger").getName mustEqual("Andreas Kollegger")
    }
    "know who a user is following, when authenticated" in {
      val github = new GitHubClient()
      github.setCredentials(username.get, password.get)
      val userService = new UserService(github)
      userService.getFollowing.toList must have(u => u.getLogin == "jakewins")
    }
  }
}
