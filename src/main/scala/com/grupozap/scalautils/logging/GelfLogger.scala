package com.grupozap.scalautils.logging

import java.util.UUID

import com.grupozap.scalautils.logging.SysLogLevel.SysLogLevel
import com.typesafe.config.ConfigFactory
import org.joda.time.DateTime
import org.json4s.DefaultFormats
import org.json4s.native.Serialization._

/**
  * Based on the GELF (Graylog Extended Log Format) specification, this class
  * brings to you logging with traceability.
  *
  * This was designed on top of the Akka Logging Framework.
  * Logging is performed asynchronously to ensure that logging has minimal performance impact.
  * Logging generally means IO and locks, which can slow down the operations of your code if it was performed synchronously.
  *
  * The log information is converted into json format:
  *
  * {
  *   "timestamp": 1476965570596,
  *   "short_message": "Processing something",
  *   "full_message": "Processing something with a bit more of careful",
  *   "host": "MyMac-dev02"
  *   "level": 3,
  *   "_product": "my-product",
  *   "_application": "my-application",
  *   "_environment": "dev",
  *   "_log_type": "application",
  *   "_request_id": "9d8c7896-7e21-4921-ad50-3c8ca440a787"
  * }
  *
  * JSON format has emerged as the de facto standard for message passing.
  * It is both readable and reasonably compact, and it provides a standardized format for structuring data.
  *
  * Logging using the JSON format allows you to easily create and parse custom fields of your applications.
  *
  */

object GelfLogger extends GelfTrait {
  override def _requestId: Option[UUID] = None
}

case class GelfLogger(requestId: UUID) extends GelfTrait {
  override def _requestId: Option[UUID] = Some(requestId)
}

case class Message(requestId: Option[UUID] = None,
                   message: String,
                   customInfo: Map[String, Any] = Map(),
                   fullMessage: Option[String] = None,
                   logLevel: SysLogLevel = SysLogLevel.INFO) {

  implicit val formats = DefaultFormats

  val config = ConfigFactory.load()

  def toJson =
    write(
      Map(
        "timestamp" -> new DateTime().getMillis,
        "short_message" -> message,
        "full_message" -> fullMessage.getOrElse(message),
        "host" -> java.net.InetAddress.getLocalHost.getHostName,
        "level" -> logLevel.id,
        "_product" -> config.getString("gelf.product"),
        "_application" -> config.getString("gelf.application"),
        "_log_type" -> config.getString("gelf.log_type"),
        "_environment" -> config.getString("gelf.environment"),
        "_request_id" -> requestId.getOrElse(UUID.randomUUID()).toString
      ) ++ customInfo.map(tuple => s"_${tuple._1}" -> tuple._2)
    )
}

trait GelfTrait {

  def _requestId: Option[UUID]

  def info(message: String, customInfo: Map[String, Any] = Map(), fullMessage: Option[String] = None): String =
    getMessage(message, customInfo, fullMessage, SysLogLevel.INFO)

  def debug(message: String, customInfo: Map[String, Any] = Map(), fullMessage: Option[String] = None): String =
    getMessage(message, customInfo, fullMessage, SysLogLevel.DEBUG)

  def warn(message: String, customInfo: Map[String, Any] = Map(), fullMessage: Option[String] = None): String =
    getMessage(message, customInfo, fullMessage, SysLogLevel.WARNING)

  def error(message: String, customInfo: Map[String, Any] = Map(), fullMessage: Option[String] = None): String =
    getMessage(message, customInfo, fullMessage, SysLogLevel.ERROR)

  private def getMessage(message: String, customInfo: Map[String, Any] = Map(), fullMessage: Option[String], logLevel: SysLogLevel):String =
    Message(_requestId, message, customInfo, fullMessage, logLevel).toJson
}

/**
  * https://en.wikipedia.org/wiki/Syslog#Severity_level
  */
object SysLogLevel extends Enumeration {
  type SysLogLevel = Value
  val EMERGENCY = Value(0, "EMERGENCY")
  val ALERT = Value(1, "ALERT")
  val CRITICAL = Value(2, "CRITICAL")
  val ERROR = Value(3, "ERROR")
  val WARNING = Value(4, "WARNING")
  val NOTICE = Value(5, "NOTICE")
  val INFO = Value(6, "INFO")
  val DEBUG = Value(7, "DEBUG")
}
