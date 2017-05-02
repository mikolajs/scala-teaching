package pl.edu.osp

import com.badlogic.gdx.backends.lwjgl._

object Main extends App {
    val cfg = new LwjglApplicationConfiguration
    cfg.title = "libgdx-demo"
    cfg.height = 700
    cfg.width = 1200
    cfg.forceExit = false
    cfg.useGL30 = true
    new LwjglApplication(new Libgdxdemo, cfg)
}
