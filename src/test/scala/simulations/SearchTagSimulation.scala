package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SearchTagSimulation extends Simulation {

  val httpConf = http
    .baseUrl("https://api.instagram.com/v1/")
    .header("Accept", "application/json")
    .header("Content-Type", "application/x-www-form-urlencoded")

  val csvFeeder = csv("feeders/tagsList.csv").circular

  def getTag() = {
    exec(http("Get items count with tag")
      .get("tags/${tag-name}")
      .queryParam("access_token", ConfigsHelper.accessToken)
      .check(status.is(200)))
  }

  val scn = scenario("Get Tag test")
    .feed(csvFeeder)
    .exec(getTag())

  setUp(
    scn.inject(atOnceUsers(20))
  ).protocols(httpConf)

}