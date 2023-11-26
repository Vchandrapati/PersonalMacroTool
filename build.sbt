ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"
javacOptions ++= Seq("--release", "21") // Target Java 21


lazy val root = (project in file("."))
  .settings(
    name := "MacroTool",
    libraryDependencies ++= Seq(
      "com.1stleg" % "jnativehook" % "2.1.0",
      "org.scalafx" %% "scalafx" % "16.0.0-R24"
    )


  )
