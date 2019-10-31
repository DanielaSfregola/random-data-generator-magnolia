random-data-generator-magnolia
=====================

[![Build Status](https://travis-ci.org/DanielaSfregola/random-data-generator-magnolia.svg?branch=master)](https://travis-ci.org/DanielaSfregola/random-data-generator-magnolia) [![License](http://img.shields.io/:license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt) [![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org) [![Chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/random-data-generator-magnolia/Lobby)

A library to generate random data for test purposes, using [ScalaCheck](https://github.com/rickynils/scalacheck) and [scalacheck-magnolia](https://github.com/etaty/scalacheck-magnolia).

This library is the evolution of [random-data-generator](https://github.com/DanielaSfregola/random-data-generator) - which is based on [Shapeless](https://github.com/milessabin/shapeless) rather than [Magnolia](https://github.com/propensive/magnolia).

Setup
-----
Supported Scala versions: 2.13.x, 2.12.x

If you don't have it already, make sure you add the Maven Central as resolver in your SBT settings:
```scala
resolvers += Resolver.sonatypeRepo("releases")
```

Also, you need to include the library as your dependency:
```scala
libraryDependencies += "com.danielasfregola" %% "random-data-generator-magnolia" % "2.8"
```

Usage
-----
Extends the trait [`RandomDataGenerator`](https://github.com/DanielaSfregola/random-data-generator-magnolia/blob/master/src/main/scala/com/danielasfregola/randomdatagenerator/magnolia/RandomDataGenerator.scala) to add the function `random` to your scope.
Once the trait has been extended, you can just use the random function as following:

```scala
import com.danielasfregola.randomdatagenerator.magnolia.RandomDataGenerator

object MyApp extends RandomDataGenerator {

  case class Example(text: String, n: Int)

  val example: Example = random[Example]
  // Example(ਈ䈦㈾钜㔪旅ꪔ墛炝푰⡨䌆ᵅ퍧咪, 73967257)
}
```

Alternatively, you can import [`RandomDataGenerator`](https://github.com/DanielaSfregola/random-data-generator-magnolia/blob/master/src/main/scala/com/danielasfregola/randomdatagenerator/magnolia/RandomDataGenerator.scala) as object:

```scala
import com.danielasfregola.randomdatagenerator.magnolia.RandomDataGenerator._

case class Example(text: String, n: Int)

val example: Example = random[Example]
// Example(巵腉밞鵾Վ뎠꿷덊,2147483647)


```

Have a look at the [tests](/src/test/scala/com/danielasfregola/randomdatagenerator/magnolia/RandomDataGeneratorSpec.scala) for more examples on how to use the library and on how to generate manual instances of `Arbitrary[T]` when needed.

Seed Selection
--------------
At the beginning of each test session, a seed is selected and used across all the tests.
The select seed is communicated in the logs. The log message looks something like the following:
```bash
[info] [RandomDataGenerator] Generating random data using seed 6260565278463862333
```

Fix your Seed
-------------
When investigating bugs or test failures, it can be useful to reproduce the same generated data of a specific session.

For every session, a seed is selected and communicated in the logs. The log message will look similar to the following:
```bash
[info] [RandomDataGenerator] Generating random data using seed 6260565278463862333
[info] [RandomDataGenerator] Replicate this session by setting RANDOM_DATA_GENERATOR_SEED=6260565278463862333

```

To generate the same data again, all you need to do is specify an environment variable indicating the seed number to use:
```bash
export RANDOM_DATA_GENERATOR_SEED=6260565278463862333
```

Once you are done, remember to remove the environment variable:
```bash
unset RANDOM_DATA_GENERATOR_SEED
```

When a fix seed variable is detected, in the logs you will see something similar to the following:
```bash
[info] [RandomDataGenerator] Variable RANDOM_DATA_GENERATOR_SEED detected: setting 6260565278463862333 as seed
```
otherwise, the following message will appear:
```bash
[info] [RandomDataGenerator] No variable RANDOM_DATA_GENERATOR_SEED detected: setting seed to random number
```

Multiple Random Instances 
-------------------------
Fixing the seed at the beginning of each session has an important side effect: when calling the function `random[T]`, we always get the same instance back.
However, sometimes we do need multiple instances of the same case class within the same test.

To generate multiple instances of the same case class use the `random[T](n: Int)` function as following:
```scala
import com.danielasfregola.randomdatagenerator.magnolia.RandomDataGenerator._

val examples: Seq[Example] = random[Example](2)
// List(Example(ਈ䈦㈾钜㔪旅ꪔ墛炝푰⡨䌆ᵅ퍧咪, 73967257), Example(᭞㩵᭟뛎Ժ䌑讵蓐ꍊꎼꙐ涌㰑袽,1736119865))
```

Improve the Compilation Time
----------------------------
This project is experimental and tries to address the slow compilation issues of [random-data-generator](https://github.com/DanielaSfregola/random-data-generator): this project uses [Magnolia](https://github.com/propensive/magnolia) which aims to provide a more performant way for type class derivation at runtime.

To improve the compilation time, you can cache your implicit `Arbitrary` instances using `shapeless.cachedImplicit`:

```scala
import shapeless._

object CachedArbitraries {
    implicit val arbA: Arbitrary[A] = cachedImplicit
    implicit val arbB: Arbitrary[B] = cachedImplicit
}
```
For more information on what it is and on how to use it have a look [here](http://stackoverflow.com/a/34401558).

Snapshot Versions
-----------------
To use a snapshot version of this library, make sure you have the resolver for maven central (snapshot repositories) in your SBT settings:
```scala
resolvers += Resolver.sonatypeRepo("snapshots")
```

Then, add the library as your dependency:
```scala
libraryDependencies += "com.danielasfregola" %% "random-data-generator-magnolia" % "2.9-SNAPSHOT"
```
