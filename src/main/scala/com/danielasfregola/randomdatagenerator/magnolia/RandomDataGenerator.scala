package com.danielasfregola.randomdatagenerator.magnolia

import com.danielasfregola.randomdatagenerator.magnolia.utils.{SeedDetector, MagnoliaLike}
import org.scalacheck._

import scala.reflect.runtime.universe._

object RandomDataGenerator extends RandomDataGenerator

trait RandomDataGenerator extends MagnoliaLike {

  protected[randomdatagenerator] val seed = SeedDetector.seed

  def random[T: WeakTypeTag: Arbitrary]: T = random(1).head

  def random[T: WeakTypeTag: Arbitrary](n: Int): Seq[T] = {
    import scala.util.{ Try, Success, Failure }

    val gen = Gen.listOfN(n, implicitly[Arbitrary[T]].arbitrary)
    Try(gen.apply(Gen.Parameters.default, seed)) match {
      case Success(Some(v)) => v
      case _ => explode[T]
    }
  }

  private def explode[T: WeakTypeTag]() = {
    val tpe = implicitly[WeakTypeTag[T]].tpe
    val msg =
      s"""Could not generate a random value for $tpe.
         |Please, make use that the Arbitrary instance for type $tpe is not too restrictive""".stripMargin
    throw new RandomDataException(msg)
  }

}
