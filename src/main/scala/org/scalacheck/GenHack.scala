package org.scalacheck

import org.scalacheck.Gen.R
import org.scalacheck.rng.Seed

// copied from https://github.com/etaty/scalacheck-magnolia

object GenHack {
  // expose some of the org.scalacheck.Gen private methods

  def gen[T](f: (Gen.Parameters, Seed) => R[T]): Gen[T] = {
    Gen.gen(f)
  }

  def r[T](r: Option[T], sd: Seed): R[T] = {
    Gen.r(r, sd)
  }

  def sieveCopy[T](gen: Gen[T], x: Any): Boolean = {
    gen.sieveCopy(x)
  }
}
