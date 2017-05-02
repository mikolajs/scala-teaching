package pl.edu.osp

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.graphics.g2d.{SpriteBatch, BitmapFont, GlyphLayout}

/*
*For subititles showed when aplications starts
 */
class StartSubtitles {
  private val texture:Texture = new Texture(Gdx.files.internal("data/river.png"))
  private val generator = new FreeTypeFontGenerator(Gdx.files.internal("data/Comic_Sans_MS_Bold.ttf"))
  private val parameter = new FreeTypeFontParameter()
  parameter.size = 80
  parameter.borderColor = Color.BLACK
  parameter.borderWidth = 3
  parameter.color = Color.YELLOW
  private val font = generator.generateFont(parameter)
  generator.dispose()
  private val layout = new GlyphLayout()
  var scale = 1f
  val subtitles = Array("LIBGDX-DEMO", "LibGDX nie jest", "silnikiem gier.", "Jest zbiorem narzedzi.",
    "Pozwala tworzyc gry na:", "Windows, Linux,", "iOS, Andoirda i HTML")
  val positions = new Array[Int](subtitles.length)
  for(i <- 0 until subtitles.length) {
    positions(i) = -110 * i
  }
  private var endFlag = false

  def render(batch: SpriteBatch): Unit = {
    batch.draw(texture, 0, 0)
    for(i <- 0 until subtitles.length) {
      val pos = positions(i)
      if(pos >=  0 && pos <= 960) {
        val Y = (pos / 1.414213562).toFloat
        val mL = pos
        val mR = marginR(Y)
        font.getData.setScale(
          (Gdx.graphics.getWidth - mL - mR).toFloat / (Gdx.graphics.getWidth - 180).toFloat
        )
        layout.setText(font,  subtitles(i))
        font.draw(batch, subtitles(i), (Gdx.graphics.getWidth + mL - (layout.width + mR )) / 2, Y)
      }
      positions(i) += 1
    }
    if(positions.last > 900) endFlag = true
  }
  private def marginR(y: Float):Int = (180.0f - 0.257142857f*y).toInt

  def dispose(): Unit = {
    texture.dispose()
    font.dispose()
  }

  def end_? = endFlag
}
