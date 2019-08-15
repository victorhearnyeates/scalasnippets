package random

import org.apache.commons.rng.simple.RandomSource
import org.scalatest.{FunSuite, Matchers}
import org.apache.commons.text.RandomStringGenerator

class RandomUtils extends FunSuite with Matchers {

  test("Generating random strings") {

    val generator: RandomStringGenerator = {
      val rng = RandomSource.create(RandomSource.MT)
      new RandomStringGenerator.Builder().withinRange('a', 'z').usingRandom(rng.nextInt).build()
    }

    println(generator.generate(4))
  }
}
