package org.neo4j.contrib.github.importer

import org.eclipse.egit.github.core.{Repository, User}
import java.io.PrintStream
import org.neo4j.graphdb.{DynamicRelationshipType, Transaction, Node, GraphDatabaseService}

/**
 * Creates nodes and relationships when visiting GitHub.
 *
 */
class Neo4jImportVisitor(val graphdb:GraphDatabaseService) extends GitHubVisitor {

  val FOLLOWS = DynamicRelationshipType.withName("FOLLOWS")
  val OWNS = DynamicRelationshipType.withName("OWNS")

  val userIndex = graphdb.index().forNodes("user")

  var tx:Transaction = null


  def prepare() {
    tx = graphdb.beginTx()
  }

  def finish() {
    tx.success()
    tx.finish()
  }

  def visit(u: User) {
    convert(u).foreach(n => index(u, n))
  }


  def visit(r: Repository) {
    convert(r).foreach(n =>
      lookup(r.getOwner) match {
        case Some(owner:Node) => owner.createRelationshipTo(n, OWNS)
        case _ => ;
      }
    )
    r.getOwner
  }

  def follows(follower:User, follows:User) {
    (lookup(follower), lookup(follows)) match {
      case (Some(from:Node), Some(to:Node)) => from.createRelationshipTo(to, FOLLOWS)
      case _ => ;
    }
  }

  def convert(u:User):Option[Node] = {
    var possibleNode:Option[Node] = None
    try {
      val node = graphdb.createNode()
      List("avatar" -> u.getAvatarUrl,
        "blog" -> u.getBlog,
        "company" -> u.getCompany,
        "email" -> u.getEmail,
        "gravatarId" -> u.getGravatarId,
        "htmlUrl" -> u.getHtmlUrl,
        "id" -> u.getId,
        "location" -> u.getLocation,
        "login" -> u.getLogin,
        "name" -> u.getName,
        "type" -> u.getType,
        "url" -> u.getUrl).foreach( p =>
          if (p._2 != null) node.setProperty(p._1, p._2)
      )
      possibleNode = Some(node)
    } catch {
      case e =>
        Console.err.println("User node creation failed, because: " + e)
    }
    possibleNode
  }

  def convert(r:Repository):Option[Node] = {
    var possibleNode:Option[Node] = None
    try {
      val node = graphdb.createNode()
      List("name" -> r.getName,
        "description" -> r.getDescription,
        "id" -> r.getId,
        "git_url" -> r.getGitUrl
        ).foreach( p =>
          if (p._2 != null) node.setProperty(p._1, p._2)
      )
      possibleNode = Some(node)
    } catch {
      case e =>
        Console.err.println("Repository node creation failed, because: " + e)
    }
    possibleNode
  }

  def index(u:User, n:Node) {
    userIndex.putIfAbsent(n, "login", u.getLogin)
  }

  def lookup(u:User) = {
    userIndex.get("login", u.getLogin).getSingle match {
      case n:Node => Some(n)
      case _ => None
    }
  }
}
