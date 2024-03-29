package gerritforge

import com.github.barbasa.gatling.git.protocol.GitProtocol
import gerritforge.GerritGitScenario._
import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation

import scala.concurrent.duration._

class GerritGitSimulation extends Simulation {

  val gitProtocol = GitProtocol()
  val numUsers    = 10
  val feeder = (1 to numUsers) map { idx =>

    Map("refSpec" -> s"master")
  }

  val gitClone = scenario("Git clone from Gerrit")
    .feed(feeder.circular)
    .exec(cloneCommand)

  val gitCloneBranch = scenario("Git clone branch from Gerrit")
    .feed(feeder.circular)
    .exec(cloneBranchCommand)

  val gitFetch = scenario("Git fetch from Gerrit")
    .feed(feeder.circular)
    .exec(cloneCommand)
    .exec(fetchCommand)

  val gitPush = scenario("Git push to Gerrit")
    .feed(feeder.circular)
    .exec(cloneCommand)
    .exec(pushCommand)

  val gitPull = scenario("Git push to Gerrit")
    .feed(feeder.circular)
    .exec(cloneCommand)
    .exec(pullCommand)

  setUp(
    gitPush.inject(constantConcurrentUsers(numUsers) during (10 seconds))
  ).protocols(gitProtocol)

}
