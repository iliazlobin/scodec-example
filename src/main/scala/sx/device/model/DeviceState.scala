package sx.device.model

import scalaz._
import scalaz.Scalaz._
import scodec.bits._
import scodec._
import scodec.codecs._
import shapeless.HNil

import DeviceState._

case class DeviceState(dc: Int, t1: Int, t2: Int, t3: Int, i1: Int, i2: Int, i3: Int, u1: Int, u2: Int, u3: Int, u4: Int)

object DeviceState {

  def validateDeviceState(rs: DeviceState): ValidationNel[String, DeviceState] = {
    (
      validateDefectCode(rs.dc) |@|
        validateTemperature(rs.t1) |@|
        validateTemperature(rs.t2) |@|
        validateTemperature(rs.t3) |@|
        validateCurrent(rs.i1) |@|
        validateCurrent(rs.i2) |@|
        validateCurrent(rs.i3) |@|
        validateVoltage(rs.u1) |@|
        validateNegativeVoltage(rs.u2) |@|
        validateVoltage(rs.u3) |@|
        validateVoltage(rs.u4)
      ) { case _ => rs }
  }

  private def validateDefectCode(value: Int): ValidationNel[String, Int] = {
    if ((value >= 0) && (value <= 0xFF)) value.successNel
    else ("Defect code must be byte value").failureNel
  }

  private case class ValidationParams[T](name: String, units: String, min: T, max: T)
  private def validate[T <: Int](value: T)(vp: ValidationParams[T]): ValidationNel[String, T] = {
    if ((value >= vp.min) && (value <= vp.max)) value.successNel
    else (vp.name + " must lie in the range: " + vp.min + " ... " + vp.max + " " + vp.units).failureNel
  }

  private val temperatureValidationParams: ValidationParams[Int] = ValidationParams[Int]("Temperature", "°C", -40, 50)
  private def validateTemperature(value: Int): ValidationNel[String, Int] = validate[Int](value)(temperatureValidationParams)

  private val currentValidationParams: ValidationParams[Int] = ValidationParams[Int]("Current", "mA", 0, 2560)
  private def validateCurrent(value: Int): ValidationNel[String, Int] = validate[Int](value)(currentValidationParams)

  private val voltageValidationParams: ValidationParams[Int] = ValidationParams[Int]("Voltage", "V", 0, 127)
  private def validateVoltage(value: Int): ValidationNel[String, Int] = validate[Int](value)(voltageValidationParams)

  private val negativeVoltageValidationParams: ValidationParams[Int] = ValidationParams[Int]("Negative voltage", "V", -127, 0)
  private def validateNegativeVoltage(value: Int): ValidationNel[String, Int] = validate[Int](value)(negativeVoltageValidationParams)
}
