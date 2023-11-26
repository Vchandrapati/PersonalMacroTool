import java.io.File
import java.nio.file.{Files, Paths}

object DataHandler {
  //get data dir path
  private val userHome = System.getProperty("user.home")
  private val appDataPath = Paths.get(userHome, "AppData", "Local", "Toolify")

  //check dir exists if not create one
  Files.createDirectories(appDataPath)

  def findFile(filename: String): File = {
    val filePath = appDataPath.resolve(filename)
    val file = new File(filePath.toString)

    file
  }
}
