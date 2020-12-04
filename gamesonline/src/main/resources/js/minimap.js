
class MiniMap {
  constructor(pools){
   let type = "WebGL"
   if(!PIXI.utils.isWebGLSupported()){ type = "canvas" }
   //PIXI.utils.sayHello(type);
   this.X = 180;
   this.Y = 180;
   this.app = new PIXI.Application({
     width: this.X, height: this.Y,  backgroundColor: 0xefefef,
       antialias: false,  transparent: false});
   document.getElementById('miniMap').appendChild(this.app.view);
   this.container = new PIXI.Container();
   this.app.stage.addChild(this.container);
   //info about elements - now empty
   this.pools = pools;
   this.startRow = 20;
   this.startCol = 20;
  }

  setStartPosition(startR, startC){
    this.startRow = startR;
    this.startCol = startC;
    this._rewriteBoard();
  }

  _rewriteBoard(){
    this.container.removeChildren(0, this.container.children.length);
    let graphics = new PIXI.Graphics();
    graphics.lineStyle({
      width: 0
    });
    graphics.beginFill(0x111111);

    for(let i = 0; i < 60; i++){
      for(let j = 0; j < 60; j++){
        if(this.pools[i][j] != '.') graphics.drawRect(j*3, i*3, 3, 3);
      }
    }
    graphics.endFill();
    graphics.lineStyle({
      width: 1,
      color: 0x00ff00
    });
    graphics.beginFill(0x11ff11, 0);
    graphics.drawRect(this.startCol*3, this.startRow*3, 60, 60);
    graphics.endFill();
    this.container.addChild(graphics);
  }


}
