import Main.log
import java.io.File

object ProcessHandler {
  private val iCuePath = "../Corsair/Corsair iCUE5 Software/iCUE.exe"
  private var pid: Option[Long] = None

  def checkValidPID(process: ProcessHandle, processName: String): Boolean = {
    process.info().command().isPresent && process.info().command().get().contains(processName)
  }

  def findAndKillProcess(processName: String): Unit = {
    pid match {
      //if pid is tracked kill and restart it
      case Some(pid) if ProcessHandle.of(pid).isPresent =>
        val process = ProcessHandle.of(pid).get()
        //if the pid is not stale kill app
        if(checkValidPID(process, processName)) process.destroy()
      //if untracked process find and kill it
      case None =>
        //search list of all process for correct app
        log("Searching processes")
        ProcessHandle.allProcesses().forEach { process =>
          val info = process.info()
          if(info.command().isPresent && info.command().get().contains(processName)) process.destroy()
        }
    }
  }

  def restartProcess(): Unit = {
    // Ensure the file exists
    if (new File(iCuePath).exists()) {
      log("Restarting process")
      val pb = new ProcessBuilder(iCuePath)
      try {
        val p = pb.start()
        //record pid for more efficient searching in future
        pid = Some(p.pid())
      } catch {
        case e: Exception => e.printStackTrace()
      }
    } else {
      log(s"Executable not found: $iCuePath")
    }
  }
}
