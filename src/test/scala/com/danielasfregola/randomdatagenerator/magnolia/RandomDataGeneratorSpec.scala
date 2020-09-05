package com.danielasfregola.randomdatagenerator.magnolia

import java.util.Currency

import org.scalacheck._
import org.specs2.mutable._

class RandomDataGeneratorSpec extends RandomDataGenerator with SpecificationLike {

  case class Example(text: String, n: Int)

  case class AlphaStrExample(text: String)

  case class Person(name: String, age: Int)

  case class BigExample(f1: String, f2: Int, f3: Long, f4: Char, f5: String,
                        f6: String, f7: Int, f8: Long, f9: Char, f10: String,
                        f11: String, f12: Int, f13: Long, f14: Char, f15: String,
                        f16: String, f17: Int, f18: Long, f19: Char, f20: String,
                        f21: String, f22: Int, f23: Long, f24: Char, f25: String,
                        f26: String, f27: Int, f28: Long, f29: Char, f30: String)

"RandomDataGenerator - Magnolia" should {

  "generate a random instance of a simple case class" in {

    val instance = random[Example]

    instance should beAnInstanceOf[Example]
  }

  "generate multiple instances of a simple case class" in {
    val size = 3
    val instances = random[Example](size)

    instances.distinct.size === size
    instances should beAnInstanceOf[Seq[Example]]
  }

  "generate a random instance of a non-predefined type" in {

    implicit val arbitraryCurrency: Arbitrary[Currency] = Arbitrary {
      Gen.oneOf(Currency.getInstance("GBP"), Currency.getInstance("EUR"), Currency.getInstance("USD"))
    }

    val instance = random[Currency]

    instance should beAnInstanceOf[Currency]
  }

  "generate a random instance by using a custom generator" in {

    implicit val arbitraryPerson: Arbitrary[Person] = Arbitrary {
      for {
        name <- Gen.oneOf("Daniela", "John", "Martin", "Marco")
        age <- Gen.choose(0, 100)
      } yield Person(name, age)
    }

    val instance = random[Person]

    instance should beAnInstanceOf[Person]
    Seq("Daniela", "John", "Martin", "Marco").contains(instance.name) should beTrue
    (0 to 100).contains(instance.age) should beTrue
  }

  "generate a random instance by using a overridden generator of a predefined type" in {

    implicit val arbitraryString: Arbitrary[String] = Arbitrary(Gen.alphaStr)

    val instance = random[AlphaStrExample]

    instance should beAnInstanceOf[AlphaStrExample]
    instance.text.forall(_.isLetter) should beTrue
  }

  "generate a random instance of a case class with more than 22 fields" in {

    val instance = random[BigExample]

    instance should beAnInstanceOf[BigExample]
  }

  "throw an exception when the arbitrary is too restrictive" in {

    implicit val restrictive = Arbitrary(Gen.chooseNum(1, 100).suchThat(_ > 200))
    val expectedException = new RandomDataException(
      """Could not generate a random value for RandomDataGeneratorSpec.this.Example.
        |Please, make use that the Arbitrary instance for type RandomDataGeneratorSpec.this.Example is not too restrictive""".stripMargin)
    random[Example] must throwA(expectedException)
  }
}

}
