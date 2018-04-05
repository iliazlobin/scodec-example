package sx.device.serializer

import scodec._
import scodec.bits.ByteVector
import scodec.codecs._

import sx.device.proto.Ids76Proto.{FunctionCode, _}
import sx.device.proto.Ids76Proto.FunctionCode.{MeasurementData, DeviceState}
import sx.device.proto.Ids76Proto.Sign.{Request, Response}
import sx.device.serializer.DeviceDslModelCodecs._
import sx.scodec.TupleCodecSyntax._

object Ids76DeviceCodecs {

  val deviceStateRequestCodec: Codec[DeviceStateRequest] = baseCodec(Request, DeviceState).hlist.as[DeviceStateRequest]
  val measurementDataRequestCodec: Codec[MeasurementDataRequest] = baseCodec(Request, MeasurementData).hlist.as[MeasurementDataRequest]
  val modeControlRequestCodec: Codec[ModeControlRequest] = (baseCodec(Request, FunctionCode.ModeControl).hlist :+ modeControlCodec).as[ModeControlRequest]

  val deviceStateResponseCodec: Codec[DeviceStateResponse] = (responseCodec(Response, DeviceState).thlist :+ wordStateCodec :+ deviceStateCodec).as[DeviceStateResponse]
  val modeControlResponseCodec: Codec[ModeControlResponse] = (responseCodec(Response, DeviceState).thlist :+ wordStateCodec).as[ModeControlResponse]
  val measurementDataResponseCodec: Codec[MeasurementDataResponse] = (responseCodec(Response, DeviceState).thlist :+ measurementDataCodec).as[MeasurementDataResponse]

  private def baseCodec(sign: Sign, fc: FunctionCode) = basePartCodec(sign) ~> uint8 <~ constant(fc.byte)
  private def basePartCodec(sign: Sign) = constant(ByteVector.fromInt(sign.byte, 1))
  private def modeControlCodec: Codec[ModeControl] = (bool ~ ignore(7) ~ uint16).thlist.as[ModeControl]
  private def responseCodec(sign: Sign, fc: FunctionCode) = lazily(baseCodec(sign, fc) ~ resultCodec)

  private val resultCodec = mappedEnum[Result, Int](uint8,
    (Result.Success, Result.Success.byte),
    (Result.Unsupported, Result.Unsupported.byte),
    (Result.Busy, Result.Busy.byte),
    (Result.Error, Result.Error.byte))
}
