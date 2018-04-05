package sx.device.proto

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen._

import sx.device.model.DeviceModelGens
import sx.device.proto.Ids76Proto._
import sx.scodec.CodecGens

trait Ids76ProtoGens extends CodecGens with DeviceModelGens {

  val modeControlGen = for {
    b <- arbitrary[Boolean]
    md <- choose(0, 65535)
  } yield ModeControl(b, md)

  val deviceStateRequestGen = uint8Gen map DeviceStateRequest
  val modeControlRequestGen = for {
    n <- choose(0, 255)
    mc <- modeControlGen
  } yield ModeControlRequest(n, mc)
  val measurementDataRequestGen = uint8Gen map MeasurementDataRequest

  val resultGen = oneOf(Result.Success, Result.Unsupported, Result.Busy, Result.Error)

  val deviceStateResponseGen = for {
    n <- uint8Gen
    r <- resultGen
    ws <- wordStateGen
    rs <- deviceStateGen
  } yield DeviceStateResponse(n, r, ws, rs)

  val modeControlResponseGen = for {
    n <- uint8Gen
    r <- resultGen
    ws <- wordStateGen
  } yield ModeControlResponse(n, r, ws)

  val measurementDataResponseGen = for {
    n <- uint8Gen
    r <- resultGen
    md <- measurementDataGen
  } yield MeasurementDataResponse(n, r, md)
}
