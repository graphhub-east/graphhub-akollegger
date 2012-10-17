package org.neo4j.contrib.github.importer

import org.eclipse.egit.github.core.{Contributor, Repository, User}
import org.eclipse.egit.github.core.service.OrganizationService

/**
 */
trait GitHubVisitor {

  def prepare()
  def finish()

  def visit(u:User)

  def visit(r:Repository)

  def follows(follower:User, follows:User)

  def contributes(contrib:Contributor, repo:Repository)

  def memberOf(member:User, org:User)

}
