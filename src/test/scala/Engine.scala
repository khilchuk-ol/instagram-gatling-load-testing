import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object Engine extends App {

  val props = new GatlingPropertiesBuilder()
    .resourcesDirectory(IDEPathHelper.mavenResourcesDirectory.toString)
    .resultsDirectory(IDEPathHelper.resultsDirectory.toString)
    .binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString)

  Gatling.fromMap(props.build)

  import java.io.IOException

  @throws[IOException]
  def main(args: Array[String]): Unit = {
    
    val myappProcess = Runtime.getRuntime.exec("powershell.exe Start-Process notepad.exe -verb RunAs")
  }
}
