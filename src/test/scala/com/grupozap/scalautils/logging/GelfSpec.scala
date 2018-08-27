package com.grupozap.scalautils.logging

import java.util.UUID

import org.json4s._
import org.json4s.native.JsonMethods._
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{Matchers, WordSpecLike}

class GelfSpec extends WordSpecLike with TableDrivenPropertyChecks with Matchers {

  implicit val formats = DefaultFormats

  val examples =
    Table(
      "messages",
      ("test-product", "test-app", "dev", "lorem ipsum", None, None, None, SysLogLevel.INFO),
      ("test-product", "test-app", "dev", "lorem ipsum", None, Some("another lorem"), None, SysLogLevel.ERROR),
      ("test-product", "test-app", "dev", "lorem ipsum", Some(Map("custom_key" -> "value")), None, Some(UUID.randomUUID()), SysLogLevel.INFO),
      ("test-product", "test-app", "dev", "lorem ipsum", None, Some("another lorem"), None, SysLogLevel.DEBUG),
      ("test-product", "test-app", "dev", "lorem ipsum", Some(Map("custom_key" -> "value")), None, Some(UUID.randomUUID()), SysLogLevel.INFO)
    )

  "The Gelf formatter" must {
    "generate a valid message" in {
      forAll(examples) { case (product, application, environment, shortMessage, customInfo, fullMessage, requestId, logLevel) =>
        val formatter = Gelf(product, application, environment)
        val message = logLevel match {
          case SysLogLevel.INFO =>
            formatter.info(shortMessage, customInfo = customInfo, fullMessage = fullMessage, requestId = requestId)
          case SysLogLevel.WARNING =>
            formatter.warn(shortMessage, customInfo = customInfo, fullMessage = fullMessage, requestId = requestId)
          case SysLogLevel.DEBUG =>
            formatter.debug(shortMessage, customInfo = customInfo, fullMessage = fullMessage, requestId = requestId)
          case SysLogLevel.ERROR =>
            formatter.error(shortMessage, customInfo = customInfo, fullMessage = fullMessage, requestId = requestId)
          case _ =>
            fail("Invalid SysLogLevel")
        }

        val asMap = parse(message).extract[Map[String, Any]]
        asMap should contain key "timestamp"
        asMap should contain key "host"
        asMap should contain key "_log_type"
        asMap should contain key "_request_id"
        asMap should contain ("short_message" -> shortMessage)
        asMap should contain ("full_message" -> fullMessage.getOrElse(shortMessage))
        asMap should contain ("level" -> logLevel.id)
        asMap should contain ("_product" -> product)
        asMap should contain ("_application" -> application)
        asMap should contain ("_environment" -> environment)
        if (requestId.isDefined) {
          asMap should contain ("_request_id" -> requestId.get.toString)
        }
        if(customInfo.isDefined) {
          customInfo.get foreach (tuple => asMap should contain (s"_${tuple._1}" -> tuple._2))
        }
      }
    }
  }
}
