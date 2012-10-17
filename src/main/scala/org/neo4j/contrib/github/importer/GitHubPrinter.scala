package org.neo4j.contrib.github.importer

import org.eclipse.egit.github.core.{Contributor, Repository, User}
import java.io.PrintStream

/**
 */
class GitHubPrinter(out:PrintStream) extends GitHubVisitor {

  def prepare() {}
  def finish() {}

  def visit(u: User) {
    out.println("user:" + u.getLogin)
  }


  def visit(r: Repository) {
    out.println("repository: " + r.getName)
  }

  def follows(follower:User, follows:User) {
    out.println("(%s)-[:FOLLOWS]->(%s)".format(follower.getLogin, follows.getLogin))
  }

  def contributes(contrib:Contributor, repo:Repository) {
    out.println("(%s)-[:CONTRIBUTES_TO]->(%s)".format(contrib.getLogin, repo.getName))
  }

  def memberOf(member:User, org:User) {
    out.println("(%s)-[:MEMBER_OF]->(%s)".format(member.getLogin, org.getName))
  }
}
