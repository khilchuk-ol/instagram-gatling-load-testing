package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class GetUserSimulation extends Simulation {

  val httpConf = http
    .baseUrl("https://api.instagram.com/v1/")
    .header("Accept", "application/json")
    .header("Content-Type", "application/x-www-form-urlencoded")

  val existingFeeder = csv("feeders/userIDsList.csv").circular
  val notExistingFeeder = csv("feeders/fakeUserIDsList.csv").circular

  def getExistingUser() = {
    exec(http("Get existing users")
      .get("users/${user-id}")
      .queryParam("access_token", ConfigsHelper.accessToken)
      .check(status.is(200)))
  }

  def getUserRecentMedia() = {
    exec(http("Get users recent media")
      .get("users/${user-id}/recent/media")
      .queryParam("access_token", ConfigsHelper.accessToken)
      .check(status.is(200)))
  }

  def getNonExistingUser() = {
    exec(http("Get not existing users")
      .get("users/${user-id}")
      .queryParam("access_token", ConfigsHelper.accessToken)
      .check(status.is(404)))
  }

  val scnEx = scenario("Get existing users and recent media test")
    .feed(existingFeeder)
    .exec(getExistingUser())
    .pause(5)
    .exec(getUserRecentMedia())

  val scnNotEx = scenario("Get not existing users test")
    .feed(notExistingFeeder)
    .exec(getNonExistingUser())

  setUp(
    scnEx.inject(atOnceUsers(20))
      .protocols(httpConf),
    scnNotEx.inject(atOnceUsers(20))
      .protocols(httpConf)
  )
}