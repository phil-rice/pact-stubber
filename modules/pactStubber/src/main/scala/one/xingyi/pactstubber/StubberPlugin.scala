//package one.xingyi.pactstubber
//
//import java.io.File
//import java.util.ResourceBundle
//import java.util.concurrent.Executors
//import java.util.concurrent.atomic.AtomicReference
//
//import sbt.Keys._
//import sbt._
//
//import scala.language.implicitConversions
//
//
//object StubberPlugin extends sbt.AutoPlugin {
//  override def trigger = allRequirements
//
//  val server = new AtomicReference[Option[ConfigBasedStubber]](None)
//
//  object autoImport {
//    lazy val stubberConfig = SettingKey[String]("config", "This is the location of the config file for the pact stubber")
//    lazy val stubberStart = taskKey[Unit]("starts the stubber")
//    lazy val stubberStop = taskKey[Unit]("starts the stubber")
//  }
//
//  import autoImport._
//
//  override lazy val buildSettings = Seq(
//    stubberConfig := "stubber.cfg",
//    stubberStart := stubberStartTask.value,
//    stubberStop := stubberStopTask.value
//  )
//
//  lazy val stubberStartTask = Def.task {
//    sLog.value.info("Starting Stubber")
//    implicit val resources: ResourceBundle = ResourceBundle.getBundle("messages")
//    implicit val executorService = Executors.newFixedThreadPool(10)
//    server.set(Some(ConfigBasedStubber(new File(autoImport.stubberConfig.value))))
//  }
//
//
//  lazy val stubberStopTask = Def.task {
//    server.get match {
//      case Some(s) =>
//        sLog.value.info("Stopping Stubber")
//        s.shutdown
//        server.set(None)
//      case None =>
//        sLog.value.info("Stubber not running, so not stopping!")
//    }
//
//  }
//}
//
//
