package pl.edu.osp


import com.badlogic.gdx.{Gdx, Game}
import com.badlogic.gdx.graphics.g2d.{SpriteBatch}
import com.badlogic.gdx.graphics.{GL20}

class Libgdxdemo extends Game {
  private var batch:SpriteBatch = null
  private var subtitles:StartSubtitles = null
  private var torpedoShip:TorpedoShipGame = null
  private var state:Int = 0

  override def create(): Unit = {
    batch = new SpriteBatch()
    subtitles = new StartSubtitles
    torpedoShip = new TorpedoShipGame
  }

  override def render():Unit = {
    Gdx.gl.glClearColor(1, 1, 1, 1)
    Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT)
    batch.begin()
    state match {
      case 0 => {subtitles.render(batch); state = if(subtitles.end_?) 1 else 0}
      case _ => torpedoShip.render(batch)
    }
    batch.end()
  }

  override def dispose():Unit = {
    batch.dispose()
    subtitles.dispose()
  }

  private def marginR(y: Float):Int = (180.0f - 0.257142857f*y).toInt

}
