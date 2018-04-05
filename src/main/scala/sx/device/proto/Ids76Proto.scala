package sx.device.proto

import scalaz._
import scalaz.Scalaz._

import sx.device.model._

object Ids76Proto {

  sealed trait Base {
    val sign: Sign
    val number: Int
    val functionCode: FunctionCode
  }

  sealed trait Request extends Base {
    val sign = Sign.Request
  }

  case class DeviceStateRequest(number: Int) extends Request {
    val functionCode = FunctionCode.DeviceState
  }
  case class ModeControlRequest(number: Int, modeControl: ModeControl) extends Request {
    val functionCode = FunctionCode.ModeControl
  }
  case class MeasurementDataRequest(number: Int) extends Request {
    val functionCode = FunctionCode.MeasurementData
  }

  case class DeviceStateResponse(number: Int, result: Result, wordState: WordState, deviceState: DeviceState) extends Response {
    val functionCode = FunctionCode.DeviceState
  }
  case class ModeControlResponse(number: Int, result: Result, wordState: WordState) extends Response {
    val functionCode = FunctionCode.ModeControl
  }
  case class MeasurementDataResponse(number: Int, result: Result, measurementData: MeasurementData) extends Response {
    val functionCode = FunctionCode.MeasurementData
  }

  trait FunctionCode {
    val byte: Int
  }
  object FunctionCode {
    case object DeviceState extends FunctionCode {
      val byte = 0x00
    }
    case object MeasurementData extends FunctionCode {
      val byte = 0x01
    }
    case object ModeControl extends FunctionCode {
      val byte = 0x02
    }
  }

  trait Sign {
    val byte: Int
  }
  object Sign {
    case object Request extends Sign {
      val byte = 0x5A
    }
    case object Response extends Sign {
      val byte = 0xA5
    }
  }

  sealed trait Response extends Base {
    val sign = Sign.Request
    val result: Result
  }
  trait Result {
    val byte: Int
  }
  object Result {
    case object Success extends Result {
      val byte = 0x00
    }
    case object Unsupported extends Result {
      val byte = 0x01
    }
    case object Busy extends Result {
      val byte = 0x02
    }
    case object Error extends Result {
      val byte = 0xFF
    }
  }

  case class ModeControl(measurementModeSign: Boolean, measurementDuration: Int)
  object ModeControl {
    def validateModeControl(modeControl: ModeControl): ValidationNel[String, ModeControl] = {
      modeControl.successNel
    }
  }
}
