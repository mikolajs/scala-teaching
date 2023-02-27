class MapWorld {
  constructor(grid, X, Y, container, resources){
    this.grid = grid;
    this.container = container;
    this.resources = resources;
    //let worldsMap = new WorldMap();
    //this.drawMap(worldsMap.worldTiles);
    this.worldData = new TheWorld();
    while(!this.worldData.loaded){
      console.log("waiting");
    }
    //X columns Y rows
    this.X = this.worldData.X;
    this.Y = this.worldData.Y;
    this.showX = 10; //size of showed tiles 
    this.showY = 10; //size of showed tiles
    //console.log(this.X + " and " + this.Y);
    this.startX = 0;
    this.startY = 0;
    this.drawMap();//chaange to drawMapTimeout
  }

  drawMapTimeout(){
    if(!this.worldData.loaded) {
      window.setTimeout(() => {this.drawMapTimeout();}, 2000);
      console.log("waiting next 2 seconds");
      return;
    }
    this.drawMap();
  }

  //// TODO: Implement
  drawMap(){
    //// TODO: Change direction x and y (must be the same as in worldmap test())
    // console.log(resources);
    this.container.removeChildren(0,this.container.children.length);
    let startX = this.startX;
    let startY = this.startY;
    /// TODO: must add delete sprite from containder before add new sprites

    let tile = "";
    console.log(this.worldData.worldTiles.length);
    for(let i = 0; i < this.showY; i++){
      for(let j = 0; j < this.showX; j++){
        tile = this.worldData.worldTiles[startY+i][startX+j];
        //console.log(tile);
        const sprite = new PIXI.Sprite(this._getTileTexture(tile));
        sprite.anchor.set(0.5);
        const p = this.grid.getCenterOfPoolInPixels(j, i);
        sprite.x = p.x;
        sprite.y = p.y;
        this.container.addChild(sprite);
        //console.log("Title added at " + i + "," + j);
        //console.log(p);
        tile = this.worldData.worldObjects[startY+i][startX+j];
        if(tile == '') continue;
        console.log(tile + " w maps");
        const objSprite = new PIXI.Sprite(this._getTileTexture(tile));
        objSprite.anchor.set(0.5);
        objSprite.x = p.x;
        objSprite.y = p.y;
        this.container.addChild(objSprite);
      }
    }
  }

  scrollUp(step){
    if(this.startY >= step) this.startY -= step;
    this.drawMap();
  }

  scrollDown(step){
    if(this.startY <= this.Y-this.showY-step) this.startY += step;
    this.drawMap();
  }
  scrollLeft(step){
    if(this.startX >= step) this.startX -= step;
    this.drawMap();
  }
  scrollRight(step){
    if(this.startX <= this.X-this.showX-step) this.startX += step;
    this.drawMap();
  }

  _getTileTexture(tile){
    if(tile == 'pl') return this.resources.p.texture;
    else if(tile == 'wd') return this.resources.w.texture;
    else if(tile == 'hi') return this.resources.h.texture;
    else if(tile == 'mo') return this.resources.m.texture;
    else if(tile == 'st') return this.resources.s.texture;
    else if(tile == 'fd') return this.resources.f.texture;
    else if(tile == 'hu') return this.resources.u.texture;
    else if(tile == 'ol') return this.resources.ol.texture;
    else if(tile == 'om') return this.resources.om.texture;
    else if(tile == 'os') return this.resources.os.texture;
    else if(tile == 'oe') return this.resources.oe.texture;
    else if(tile == 'oc') return this.resources.oc.texture;
    else if(tile == 'ov') return this.resources.ov.texture;
    else if(tile == 'og') return this.resources.og.texture;
    else if(tile == 'ua') return this.resources.ua.texture;
    else if(tile == 'us') return this.resources.us.texture;
    else if(tile == 'uc') return this.resources.uc.texture;
    else if(tile == 'ow') return this.resources.ow.texture;
    else if(tile == 'bl') return this.resources.bl.texture;
    else if(tile == 'bm') return this.resources.bm.texture;
    else if(tile == 'bs') return this.resources.bs.texture;
    else if(tile == 'dl') return this.resources.dl.texture;
    else if(tile == 'dm') return this.resources.dm.texture;
    else if(tile == 'ds') return this.resources.ds.texture;
    else if(tile == 'sl') return this.resources.sl.texture;
    else if(tile == 'sm') return this.resources.sm.texture;
    else if(tile == 'ss') return this.resources.ss.texture;
    else return this.resources.p.texture;

  }

}
