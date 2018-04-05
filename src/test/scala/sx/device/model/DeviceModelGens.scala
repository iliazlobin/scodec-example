package sx.device.model

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen._

import sx.device.model.MeasurementData.{SpeedDetection, Target}
import sx.scodec.CodecGens

trait DeviceModelGens extends CodecGens {

  def vectorGen[T](sz: Int)(implicit a: Arbitrary[T]) = containerOfN[Vector, T](sz, a.arbitrary)

  val wordStateGen = vectorGen[Boolean](5).map(v => WordState(v(0), v(1), v(2), v(3), v(4)))
  val invalidDeviceStateGen = vectorGen(11)(Arbitrary(uint8Gen)).map(v => DeviceState(v(0), v(1), v(2), v(3), v(4), v(5), v(6), v(7), v(8), v(9), v(10)))

  val deviceStateGen = for {
    dc <- uint8Gen
    t <- vectorGen(3)(Arbitrary(choose(-40, 50)))
    i <- vectorGen(3)(Arbitrary(choose(0, 2560).map(_ / 10 * 10)))
    u <- vectorGen(3)(Arbitrary(choose(0, 127)))
    nu <- choose(-127, 0)
  } yield DeviceState(dc, t(0), t(1), t(2), i(0), i(1), i(2), u(0), nu, u(1), u(2))

  val targetGen = for {
    s <- uint16Gen
    d <- uint16Gen
    sd <- oneOf(SpeedDetection.NORMAL_SPEED, SpeedDetection.BELOW_SPEED, SpeedDetection.OVER_SPEED, SpeedDetection.UNRELIABLE_SPEED)
  } yield Target(s, d, sd)

  implicit val targetArbitrary: Arbitrary[Target] = Arbitrary(targetGen)

  val invalidMeasurementDataGen = for {
    n <- arbitrary[Int]
    tc <- arbitrary[Int]
    ths <- arbitrary[Float]
    ups <- vectorGen[Float](1024)
    dps <- vectorGen[Float](1024)
    targs <- arbitrary[Int]
    ts <- choose(1, 10).flatMap(c => vectorGen[Target](c))
  } yield MeasurementData(n, tc, ths, ups, dps, targs, ts)

  val measurementDataGen = for {
    md <- invalidMeasurementDataGen
    n <- uint8Gen
    tc <- uint16Gen
    targs <- uint8Gen
    ts <- vectorGen[Target](targs)
  } yield MeasurementData(n, tc, md.threshould, md.upperPoints, md.downPoints, targs, ts)
}
