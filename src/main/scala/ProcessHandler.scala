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
      case Some(pid) =>
        val process = ProcessHandle.of(pid).get()
        //if the pid is not stale kill app
        if(checkValidPID(process, processName)) process.destroy()
      //if untracked process find and kill it
      case None =>
        //search list of all process for correct app
        val processList = ProcessHandle.allProcesses()
        val process = processList.filter(p => p.info().command().isPresent &&
          p.info().command().get().contains(processName)).toList

        //if list is empty indicates process is not active
        if (process.size() == 0) return
        process.get(0).destroy()
    }
  }

  def restartProcess(): Unit = {
    // Ensure the file exists
    if (new File(iCuePath).exists()) {
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
