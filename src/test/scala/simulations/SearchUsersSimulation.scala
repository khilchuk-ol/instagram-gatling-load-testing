package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SearchUsersSimulation extends Simulation {

  val httpConf = http
    .baseUrl("https://api.instagram.com/v1/")
    .header("Accept", "application/json")
    .header("Content-Type", "application/x-www-form-urlencoded")

  val csvFeeder = csv("feeders/userSearchQueriesList.csv").circular

  def getUsers() = {
    exec(http("Search users")
      .get("users/search")
      .queryParam("q", "${q}")
      .queryParam("access_token", ConfigsHelper.accessToken)
      .check(status.is(200)))
  }

  val scn = scenario("Search users test")
    .feed(csvFeeder)
    .exec(getUsers())

  setUp(
    scn.inject(atOnceUsers(20))
  ).protocols(httpConf)

}