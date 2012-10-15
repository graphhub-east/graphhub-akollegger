package org.neo4j.contrib.github.importer

import org.eclipse.egit.github.core.User

/**
 */
trait GitHubVisitor {

  def prepare()
  def finish()

  def visit(u:User)

  def follows(follower:User, follows:User)
}
