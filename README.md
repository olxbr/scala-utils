# scala-utils
[![Build Status](https://travis-ci.org/grupozap/scala-utils.svg?branch=master)](https://travis-ci.org/grupozap/scala-utils)

`scala-utils` is an utility library that attempts to add useful code rapidly in your development pipeline, so that you can focus on what is really needed. It does not replace any existing library, instead it allows you to add production-ready features.

## Features

- **Logging**: Add features such as a configurable GELF log formatter without the need of a full Graylog connector library (https://github.com/grupozap/scala-utils/tree/master/src/main/scala/com/grupozap/scalautils/logging)

## Download

### Maven

  ```
<dependency>
  <groupId>br.com.gzvr</groupId>
  <artifactId>scala-utils_2.11</artifactId>
  <version>1.1.0</version>
  <type>pom</type>
</dependency>
```

### SBT

```
libraryDependencies += "br.com.gzvr" %% "scala-utils" % "1.1.0"
```

You might need to add the Bintray repository:

```
resolvers ++= Seq(
  Resolver.bintrayRepo("gzvr", "maven")
)
```

- Supported Scala vesions: `2.11` and `2.12`

## Contributors

- Renato Silva (https://github.com/resilva87) - maintainer
- Thiago Pereira (https://github.com/thiagoandrade6) - maintainer

## Contributing

If you found a bug in the source code or if you want to contribute with new features, you can help submitting an issue; even better if you can submit a pull request :)