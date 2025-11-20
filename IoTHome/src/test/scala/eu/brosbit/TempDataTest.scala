package eu.brosbit
import java.util.Date

class TempDataTest extends  munit.FunSuite {
  test("change temperature setter") {
    SensorData.setOwnSettings(23.0f, true)
    SensorData.addMeasure("yellow", Date().getTime, 20.0f)
    assertEquals(SensorData.getTemperatureForBoiler, 34.0f)
    SensorData.addMeasure("yellow", Date().getTime, 21.2f)
    assertEquals(SensorData.getTemperatureForBoiler, 36.0f)
    SensorData.addMeasure("yellow", Date().getTime, 21.999f)
    assertEquals(SensorData.getTemperatureForBoiler, 38.0f)
    SensorData.addMeasure("yellow", Date().getTime, 22.5f)
    assertEquals(SensorData.getTemperatureForBoiler, 39.0f)
    SensorData.addMeasure("yellow", Date().getTime, 22.81f)
    assertEquals(SensorData.getTemperatureForBoiler, 39.0f)
  }
}