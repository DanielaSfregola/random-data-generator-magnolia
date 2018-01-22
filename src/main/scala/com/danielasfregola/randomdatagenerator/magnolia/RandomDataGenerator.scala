package com.danielasfregola.randomdatagenerator.magnolia

import com.danielasfregola.randomdatagenerator.magnolia.utils.{SeedDetector, MagnoliaLike}
import org.scalacheck._

object RandomDataGenerator extends RandomDataGenerator

trait RandomDataGenerator extends MagnoliaLike {

  import scalacheckmagnolia.MagnoliaArbitrary._

  protected[randomdatagenerator] val seed = SeedDetector.seed

  def random[T](implicit arb: Arbitrary[T]): T = random(1)(arb).head

  def random[T](n: Int)(implicit arb: Arbitrary[T]): Seq[T] = {
    val gen = Gen.listOfN(n, arb.arbitrary)
    val optSeqT = gen.apply(Gen.Parameters.default, seed)
    optSeqT.get
  }

}
