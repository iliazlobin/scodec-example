package sx.device.model

import scalaz._
import scalaz.Scalaz._

import sx.device.model.MeasurementData._

case class MeasurementData(number: Int, timerCounter: Int, threshould: Float, upperPoints: Vector[Float], downPoints: Vector[Float], targetCount: Int,
                           targets: Vector[Target])

object MeasurementData {

  case class Target(speed: Int, distance: Int, speedDetection: SpeedDetection.Value)
  object SpeedDetection extends Enumeration {
    val NORMAL_SPEED, BELOW_SPEED, OVER_SPEED, UNRELIABLE_SPEED = Value
  }

  type ValidationNelUnit[E] = ValidationNel[E, Unit]

  def validateMeasurementData(md: MeasurementData): ValidationNel[String, MeasurementData] = {

    ({
      if ((md.number >= 0) && (md.number <= 255)) Success(Unit)
      else "number must lie in range: 0 .. 255".failureNel
    } |@| {
      if ((md.timerCounter >= 0) && (md.timerCounter <= 65535)) Success(Unit)
      else "timerCounter must lie in range: 0 .. 255".failureNel
    } |@| {
      if (md.upperPoints.length == 1024) Success(Unit)
      else "upper points must have 1024 elements".failureNel
    } |@| {
      if (md.downPoints.length == 1024) Success(Unit)
      else "down points must have 1024 elements".failureNel
    } |@| {
      if (md.targetCount < 255) Success(Unit)
      else "target counts must be less then 255".failureNel
    } |@| {
      if (md.targetCount == md.targets.length) Success(Unit)
      else "targets must equal targetCount".failureNel
    }) {case _ => md}
  }
}
