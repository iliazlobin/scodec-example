package sx.scodec

import org.scalacheck.Gen
import org.scalacheck.Gen.choose

trait CodecGens {

  def uint8Gen: Gen[Int] = choose(0, 0xff)
  def uint16Gen: Gen[Int] = choose(0, 0xffff)
  def uint32Gen: Gen[Long] = choose(0, 0xffffffffL)
}
