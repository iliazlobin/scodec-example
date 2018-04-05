package sx.device.serializer

import sx.device.model._
import scodec._
import scodec.codecs._

object DeviceDslModelCodecs {

  val wordStateCodec: Codec[WordState] = (bool :: bool :: bool :: bool :: bool :: ignore(3)).as[WordState]
  val voltageCodec: Codec[Int] = uint8.xmapc(_ * 10)(_ / 10)
  val deviceStateCodec: Codec[DeviceState] = (uint8 :: int8 :: int8 :: int8 :: voltageCodec :: voltageCodec :: voltageCodec :: int8 :: int8 :: int8 :: int8).as[DeviceState]

  import MeasurementData._
  val targetCodec: Codec[Target] = (uint16 :: uint16 :: enumerated(uint8, SpeedDetection)).as[Target]
  val measurementDataCodec: Codec[MeasurementData] = ((uint8 :: uint16 :: float :: vectorOfN(provide(1024), float) :: vectorOfN(provide(1024), float)) :::
    (uint8 flatPrepend (sz =>  vectorOfN(provide(sz), targetCodec).hlist))).as[MeasurementData]
}
