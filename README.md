# scala-utils
[![Build Status](https://travis-ci.org/grupozap/scala-utils.svg?branch=master)](https://travis-ci.org/grupozap/scala-utils)

`scala-utils` is an utility library that attempts to add useful code rapidly in your development pipeline, so that you can focus on what is really needed. It does not replace any existing library, instead it allows you to add production-ready features.

Code quality: [Sonar](https://sonarcloud.io/project/overview?id=olxbr_scala-utils)

## Features

- **Logging**: Add features such as a configurable GELF log formatter without the need of a full Graylog connector library (https://github.com/grupozap/scala-utils/tree/master/src/main/scala/com/grupozap/scalautils/logging)

## Use it in your project

### SBT

```
libraryDependencies += "br.com.gzvr" %% "scala-utils" % "1.1.0"
```

You'll need to add our JFrog repository:

```
resolvers += "Artifactory" at "https://squadzapquality.jfrog.io/artifactory/olxbr-sbt-release/"
```

- Supported Scala versions: `2.11` and `2.12`

## Contributors

- Renato Silva (https://github.com/resilva87) - maintainer
- Thiago Pereira (https://github.com/thiagoandrade6) - maintainer

## Contributing

If you found a bug in the source code or if you want to contribute with new features, you can help submitting an issue; even better if you can submit a pull request :)

### Publish

Once you merge your code to the master branch, [GitHub Actions](https://github.com/olxbr/scala-utils/actions) should automatically publish it.

To publish manually, create a `credentials.properties` file in the project's directory, with [the content you can find here](https://vault.grupozap.io/ui/vault/secrets/squad-quality/show/servicos/jfrog-quality), and run:
```shell
sbt clean compile package publish
```
