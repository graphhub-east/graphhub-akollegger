package org.neo4j.contrib.github.importer

import scala.collection.JavaConversions._

import org.eclipse.egit.github.core.client.GitHubClient
import org.eclipse.egit.github.core.service.UserService
import org.eclipse.egit.github.core.User
import collection.immutable.Queue

object GitHubGuide {

  def apply(github:GitHubClient) = new GitHubGuide(github)
  def apply(github:GitHubClient, maxDepth: Int) = new GitHubGuide(github, maxDepth)

}

class GitHubGuide(github:GitHubClient, maxDepth:Int = 3) {

  val users = new UserService(github)
  val visitedUsers = collection.mutable.BitSet()
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

