package org.neo4j.contrib.github.importer

import scala.collection.JavaConversions._

import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.{RepositoryService, UserService}
import org.eclipse.egit.github.core.{Repository, User}
import collection.immutable.Queue
import org.neo4j.kernel.EmbeddedGraphDatabase

object GitHubGuide {

  def apply(github:GitHubClient) = new GitHubGuide(github)
  def apply(github:GitHubClient, maxDepth: Int) = new GitHubGuide(github, maxDepth)

  def main(args:Array[String]) {
    tour(args(0), args(1))
  }

  def tour(username:String, password:String, graphdbLocation:String = "github.graphdb") {
    val graphdb = new EmbeddedGraphDatabase(graphdbLocation)
    val github = new GitHubClient()
    github.setCredentials(username, password)

  }
}

class GitHubGuide(github:GitHubClient, maxDepth:Int = 3) {

  val users = new UserService(github)
  val repos = new RepositoryService(github)

  val visitedUsers = collection.mutable.BitSet()
  val visitedRepos = collection.mutable.HashSet()

  val userQ = collection.mutable.Queue[User]()
  val followQ = collection.mutable.Queue[(User,User)]()

  def guide(visitor:GitHubVisitor) {
    userQ.enqueue(users.getUser)

    visitor.prepare()

    tourUsers(visitor)

    visitor.finish()
  }

  var visitedUserDepth = 0

  def tourUsers(visitor:GitHubVisitor) {
    while (!userQ.isEmpty) {
      val user = userQ.dequeue
      visitor.visit(user)
      visitedUsers.add(user.getId)
      for (followee <- users.getFollowing(user.getLogin)) {
        followQ.enqueue((user,followee))
        if (shouldVisit(followee)) {
          userQ.enqueue(followee)
        }
      }
      tourFollowers(visitor)
      for (repo <- repos.getRepositories(user.getLogin)) {
          visitor.visit(repo)
      }
      visitedUserDepth += 1
    }
  }

  def tourFollowers(visitor:GitHubVisitor) {
    followQ.dequeueAll( possibleFollows =>
      (visited(possibleFollows._1) && visited(possibleFollows._2))
    ).foreach( readyFollows =>
      visitor.follows(readyFollows._1, readyFollows._2)
    )
  }

  def visited(user:User) = {
    visitedUsers.contains(user.getId)
  }

  def shouldVisit(user:User) = {
    (visitedUserDepth < maxDepth) && !visited(user)
  }

}

