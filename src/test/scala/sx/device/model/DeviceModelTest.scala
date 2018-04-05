package sx.device.model

import scalaz.{Failure, Success}
import org.scalatest.{Matchers, PropSpec}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import sx.device.model.MeasurementData._
import sx.device.model.DeviceState._

class DeviceModelTest extends PropSpec with GeneratorDrivenPropertyChecks with Matchers with DeviceModelGens {

  property("Generate valid models") {

    forAll(measurementDataGen) { md =>
      val r = validateMeasurementData(md) match {
        case Success(_) => true
        case Failure(_) => false
      }
      r should be(true)
    }

    forAll(deviceStateGen) { rs =>
      val r = validateDeviceState(rs) match {
        case Success(_) => true
        case Failure(_) => false
      }
      r should be(true)
    }
  }
}
