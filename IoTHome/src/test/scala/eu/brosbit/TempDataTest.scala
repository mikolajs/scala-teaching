package eu.brosbit
import java.util.Date

class TempDataTest extends  munit.FunSuite {
  test("change temperature setter") {
    TemperatureData.setOwnSettings(23.0f, true)
    TemperatureData.addMeasure('T', Date().getTime, 20.0f)
    assertEquals(TemperatureData.getTemperatureForBoiler, 34.0f)
    TemperatureData.addMeasure('T', Date().getTime, 21.2f)
    assertEquals(TemperatureData.getTemperatureForBoiler, 36.0f)
    TemperatureData.addMeasure('T', Date().getTime, 21.999f)
    assertEquals(TemperatureData.getTemperatureForBoiler, 38.0f)
    TemperatureData.addMeasure('T', Date().getTime, 22.5f)
    assertEquals(TemperatureData.getTemperatureForBoiler, 39.0f)
    TemperatureData.addMeasure('T', Date().getTime, 22.81f)
    assertEquals(TemperatureData.getTemperatureForBoiler, 39.0f)
  }
}