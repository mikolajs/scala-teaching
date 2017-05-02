package pl.edu.osp

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch

class TorpedoShipGame {
  private val texture:Texture = new Texture(Gdx.files.internal("data/river.png"))


  def render(batch: Batch): Unit = {
    batch.draw(texture, 0, 0)
  }
}
