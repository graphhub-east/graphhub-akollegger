package org.neo4j.contrib.github.importer

import org.eclipse.egit.github.core.User
import java.io.PrintStream

/**
 */
class GitHubPrinter(out:PrintStream) extends GitHubVisitor {

  def prepare() {}
  def finish() {}

  def visit(u: User) {
    out.println(u.getLogin)
  }
  def follows(follower:User, follows:User) {
    out.println("(%s)-[:FOLLOWS]->(%s)".format(follower.getLogin, follows.getLogin))
  }
}
