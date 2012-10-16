package org.neo4j.contrib.github.importer

import org.eclipse.egit.github.core.{Repository, User}
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
}
