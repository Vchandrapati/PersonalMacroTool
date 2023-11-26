import Main.log

import java.io.{File, PrintWriter}
import java.util.{Calendar, Optional}
import scala.io.Source

object Lockfile {
  private val fileName = "lockfile.txt"
  private var process: Optional[ProcessHandle] = Optional.empty()

  def check(): Boolean = {
    val lockFile = DataHandler.findFile(fileName)
    if(lockFile.exists()) {
      val source = Source.fromFile(lockFile.toString)
      val line: List[String] = source.getLines().toList
      source.close()
      process = ProcessHandle.of(line.head.toInt)
      //Check if process exists and is valid
      if(process.isPresent && ProcessHandler.checkValidPID(process.get(), "javaw.exe")) {
        //app can continue starting
        false
      } else {
        log("New lockFile required")
        lockFile.delete()
        createLock(lockFile)
        true
      }
    } else {
      createLock(lockFile)
      true
    }
  }

  private def createLock(lockFile: File): Unit = {
    log("Creating lockfile")
    val current = Calendar.getInstance()

    lockFile.createNewFile()
    val printWriter = new PrintWriter(lockFile)
    printWriter.println(ProcessHandle.current().pid())

    //lockfile contains pid and date started
    printWriter.println(current.getTime)
    printWriter.close()

    log("Lockfile created")
  }
}
