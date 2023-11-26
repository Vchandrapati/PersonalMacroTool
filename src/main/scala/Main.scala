import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener

import java.util.logging.{Level, LogManager, Logger}
import java.awt._
import java.awt.event._
import java.io.{File, FileOutputStream, PrintStream}
import java.util.Calendar


object Main extends NativeKeyListener {
  val current: Calendar = Calendar.getInstance()

  private val fileOut = new FileOutputStream(DataHandler.findFile("logs.txt"))
  System.setOut(new PrintStream(fileOut))
  System.setErr(new PrintStream(fileOut))

  if(!Lockfile.check()) {
    //exit app if already running
    log("Application is already running")
    sys.exit()
  }

  private var isShiftPressed = false
  private var isControlPressed = false

  def main(args: Array[String]): Unit = {
    try {
      GlobalScreen.registerNativeHook()
    } catch {
      case ex: NativeHookException =>
        log("Failed to register native hook: " + ex.getMessage)
        System.exit(1)
    }

    GlobalScreen.addNativeKeyListener(this)
    LogManager.getLogManager.reset()
    val logger = Logger.getLogger("org.jnativehook")
    logger.setLevel(Level.OFF)

    if (SystemTray.isSupported) {
      val tray = SystemTray.getSystemTray
      val image = Toolkit.getDefaultToolkit.getImage("resources/tray_icon.png") // Path to your icon image
      val popup = new PopupMenu()
      val exitItem = new MenuItem("Exit")
      val resetICUE = new MenuItem("Reset iCUE")

      exitItem.addActionListener((e: ActionEvent) => {
        //delete lock so new instance can be created
        val file = DataHandler.findFile("lockfile.txt")
        if(file.exists())
          file.delete()

        System.exit(0)
      })

      resetICUE.addActionListener((e: ActionEvent) => {
        ProcessHandler.findAndKillProcess("iCUE")
        ProcessHandler.restartProcess()
      })

      popup.add(exitItem)
      popup.add(resetICUE)
      val trayIcon = new TrayIcon(image, "Tools", popup)
      trayIcon.setImageAutoSize(true)
      tray.add(trayIcon)
    } else {
      log("System tray not supported!")
    }
  }

  override def nativeKeyPressed(e: NativeKeyEvent): Unit = {
    if(e.getKeyCode == NativeKeyEvent.VC_SHIFT)
      isShiftPressed = true

    if (e.getKeyCode == NativeKeyEvent.VC_CONTROL)
      isControlPressed = true

    if (e.getKeyCode == NativeKeyEvent.VC_D && isShiftPressed && isControlPressed) {
      ProcessHandler.findAndKillProcess("iCUE.exe")
      ProcessHandler.restartProcess()
    }
  }

  override def nativeKeyReleased(e: NativeKeyEvent): Unit = {
    if (e.getKeyCode == NativeKeyEvent.VC_SHIFT)
      isShiftPressed = false

    if (e.getKeyCode == NativeKeyEvent.VC_CONTROL)
      isControlPressed = false
  }

  override def nativeKeyTyped(e: NativeKeyEvent): Unit = {

  }

  def log(logText: String): Unit = {
    println(s"${current.getTime}: $logText")
  }
}