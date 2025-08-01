package eu.brosbit
import java.util.Date

class TempDataTest extends  munit.FunSuite {
  val temperatureData = TemperatureData()
  test("change temperature setter for own settings") {
    temperatureData.setOwnSettings(23.0f, true)
    temperatureData.addMeasure("T", Date().getTime, 20.0f)
    assertEquals(temperatureData.getTemperatureForBoiler, 34.0f)
    temperatureData.addMeasure("T", Date().getTime, 21.2f)
    assertEquals(temperatureData.getTemperatureForBoiler, 36.0f)
    temperatureData.addMeasure("T", Date().getTime, 21.999f)
    assertEquals(temperatureData.getTemperatureForBoiler, 38.0f)
    temperatureData.addMeasure("T", Date().getTime, 22.5f)
    assertEquals(temperatureData.getTemperatureForBoiler, 39.0f)
    temperatureData.addMeasure("T", Date().getTime, 22.81f)
    assertEquals(temperatureData.getTemperatureForBoiler, 39.0f)
  }
}