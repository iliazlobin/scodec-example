package sx.device.serializer

import org.scalatest.{Assertion, Matchers, PropSpec}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import scodec.{Codec, DecodeResult}
import scodec.Attempt.{Failure, Successful}

import sx.device.proto.Ids76ProtoGens
import sx.device.serializer.Ids76DeviceCodecs._

class CodecsTest extends PropSpec with GeneratorDrivenPropertyChecks with Matchers with Ids76ProtoGens {

  property("deviceStateRequest roundtrip check") {
    forAll(deviceStateRequestGen) (roundtripCheck(_, deviceStateRequestCodec))
  }

  property("measurementDataRequest roundtrip check") {
    forAll(measurementDataRequestGen) (roundtripCheck(_, measurementDataRequestCodec))
  }

  property("modeControlRequest roundtrip check") {
    forAll(modeControlRequestGen) (roundtripCheck(_, modeControlRequestCodec))
  }

  property("deviceStateResponse roundtrip check") {
    forAll(deviceStateResponseGen) (roundtripCheck(_, deviceStateResponseCodec))
  }

  property("measurementDataResponse roundtrip check") {
    forAll(measurementDataResponseGen) (roundtripCheck(_, measurementDataResponseCodec))
  }

  property("modeControlResponse roundtrip check") {
    forAll(modeControlResponseGen) (roundtripCheck(_, modeControlResponseCodec))
  }

  private def roundtripCheck[T](value: T, codec: Codec[T]): Assertion = {
    (codec.encode(value) match {
      case Successful(v) => Right(v)
      case Failure(e) => Left(e)
    }) flatMap { bv =>
      codec.decode(bv) match {
        case Successful(DecodeResult(v, _)) => Right(v)
        case Failure(e) => Left(e)
      }
    } should be(Right(value))
  }
}
