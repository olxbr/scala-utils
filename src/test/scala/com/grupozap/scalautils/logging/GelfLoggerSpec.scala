package com.grupozap.scalautils.logging

import java.util.UUID

import org.json4s._
import org.json4s.native.JsonMethods._
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpecLike}

class GelfLoggerSpec extends WordSpecLike with TableDrivenPropertyChecks with Matchers {

  implicit val formats = DefaultFormats

  val examples =
    Table(
      "messages",
      ("lorem ipsum", Map[String, Any](), None, SysLogLevel.INFO),
      ("lorem ipsum", Map[String, Any](), Some("another lorem"), SysLogLevel.ERROR),
      ("lorem ipsum", Map[String, Any]("custom_key" -> "value"), None, SysLogLevel.INFO),
      ("lorem ipsum", Map[String, Any](), Some("another lorem"), SysLogLevel.DEBUG),
      ("lorem ipsum", Map[String, Any]("custom_key" -> "value"), None, SysLogLevel.INFO)
    )

  "The GelfLogger" must {
    "generate a valid message" in {
      forAll(examples) { case (shortMessage, customInfo, fullMessage, logLevel) =>
        val message = logLevel match {
          case SysLogLevel.INFO =>
            GelfLogger.info(shortMessage, customInfo = customInfo, fullMessage = fullMessage)
          case SysLogLevel.WARNING =>
            GelfLogger.warn(shortMessage, customInfo = customInfo, fullMessage = fullMessage)
          case SysLogLevel.DEBUG =>
            GelfLogger.debug(shortMessage, customInfo = customInfo, fullMessage = fullMessage)
          case SysLogLevel.ERROR =>
            GelfLogger.error(shortMessage, customInfo = customInfo, fullMessage = fullMessage)
          case _ =>
            fail("Invalid SysLogLevel")
        }

        val asMap = parse(message).extract[Map[String, Any]]
        asMap should contain key "timestamp"
        asMap should contain key "host"
        asMap should contain key "_product"
        asMap should contain key "_application"
        asMap should contain key "_environment"
        asMap should contain key "_log_type"
        asMap should contain key "_request_id"
        asMap should contain ("short_message" -> shortMessage)
        asMap should contain ("full_message" -> fullMessage.getOrElse(shortMessage))
        asMap should contain ("level" -> logLevel.id)
        customInfo foreach (tuple => asMap should contain (s"_${tuple._1}" -> tuple._2))
      }
    }

    "preseve the requestId if needed" in {
      val requestId = UUID.randomUUID()
      val firstMessage = GelfLogger(requestId).info("test1")
      val secondMessage = GelfLogger(requestId).info("test2")
      val firstAsMap = parse(firstMessage).extract[Map[String, Any]]
      val secondAsMap = parse(secondMessage).extract[Map[String, Any]]
      firstAsMap("_request_id") shouldEqual secondAsMap("_request_id")
    }
  }
}
