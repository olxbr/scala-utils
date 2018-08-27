# Logging Package

This package contains logging facilities that otherwise would need replication from project to project; it does not attempts to create another logging service (such as Apache Log4j, Logback, etc)

## Gelf Logger

The Gelf Logger is a simple object that allows the user to generate valid [GELF](http://docs.graylog.org/en/2.4/pages/gelf.html) structured messages without the need to add another library in the project. It is designed such that you can, for instance, simply log a GELF formatted message in the standard output, which can be captured and redirected with [Filebeat](https://www.elastic.co/guide/en/beats/filebeat/master/filebeat-overview.html) to a centralized log server, making it easier to manage and index these log.

### Setup

In order to use the Gelf Logger you need to create an `application.conf` file in `src/main/resources` folder of your project, and add the following:

```
gelf {
  product = "my-product"  // Name of the product of this application, such as "frontend-system", "mail-platform"
  application = "my-app"  // Name of the application you're logging
  log_type = "application"  // The type of logging, such as "application", "service", etc
  environment = "production" // The environment you're running your application
}
```

### Examples

```scala
// Creates a JSON string in the valid GELF format with DEBUG level. 
// Supports `info`, `debug`, `warn` and `error` messages 
Gelf.debug("Memory usage > threshold")

// You can add custom parameters
Gelf.debug("Memory usage > threshold", customInfo = Map("current_memory_usage" -> 99.9873))

// And a full message
Gelf.debug("Memory usage > threshold", customInfo = Map("current_memory_usage" -> 99.9873), fullMessage = Some("Current memory usage is beyond defined threshold"))
```