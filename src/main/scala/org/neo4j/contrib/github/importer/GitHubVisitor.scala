package org.neo4j.contrib.github.importer

import org.eclipse.egit.github.core.{Repository, User}

/**
 */
trait GitHubVisitor {

  def prepare()
  def finish()

  def visit(u:User)

  def visit(r:Repository)

  def follows(follower:User, follows:User)
}
