class Grid {
  constructor(unitSize, X, Y, gridContainer){
    this.unitSize = unitSize;
    console.log("Unit Size: " + unitSize);
    this.gridContainer = gridContainer;
    this.x = X; this.y = Y;
    this.drawHexGrid();
    //this.drawPointIndexes();
  }
  drawHexGrid() {
        let background = new PIXI.Graphics();
        background.lineStyle(2, 0x000000);
        background.zOrder = 1001;
        //background.beginFill(0x66CCFF);
        let pointX = 0;
        let pointY = 0;
        for (let i = 0; i < this.y; i++) {
            pointY = (i * 5) * this.unitSize + 12;
            if (i % 2 == 0) {
                pointX = 0;
            }
            else {
                pointX = -this.unitSize * 5;
            }
            for (var j = 0; j < this.x; j++) {
                background.moveTo(pointX, pointY);
                this.drawOneHex(background, pointX, pointY);
                pointX += this.unitSize * 10;
                if (i % 2 == 1 && j == this.x - 1) {
                    background.moveTo(pointX, pointY);
                    this.drawOneHex(background, pointX, pointY);
                }
            }
        }

        this.gridContainer.addChild(background);
    }
    drawOneHex(background, startX, startY) {
        //console.log("Draw from: " + startX + ", " + startY);
        let pointX = startX;
        let pointY = startY;
        pointY += 4 * this.unitSize;
        background.lineTo(pointX, pointY);
        pointX += 5 * this.unitSize;
        pointY += this.unitSize;
        background.lineTo(pointX, pointY);
        pointX += 5 * this.unitSize;
        pointY -= this.unitSize;
        background.lineTo(pointX, pointY);
        pointY -= 4 * this.unitSize;
        background.lineTo(pointX, pointY);
        pointX -= 5 * this.unitSize;
        pointY -= this.unitSize;
        background.lineTo(pointX, pointY);
        pointX -= 5 * this.unitSize;
        pointY += this.unitSize;
        background.lineTo(pointX, pointY);
    }

    drawPointIndexes(){
      let style = new PIXI.TextStyle({"fill":"black"});
      for(let i = 0; i < this.x; i++){
        for(let j = 0; j < this.y; j++){
          let point = this.getCenterOfPoolInPixels(i, j);
          let t = new PIXI.Text(j + "," + i, style);
          t.anchor.set(0.5);
          t.position.set(point.x, point.y);
          this.gridContainer.addChild(t);
        }
      }
    }

    getPoolClicked(x, y) {
      ///// TODO: ERROR -> with Y
        let yP = Math.ceil((y - 0.16666666 * this.unitSize) / (5 * this.unitSize));
        if (yP % 2 == 1) {
            return new PIXI.Point(Math.ceil(x / (10 * this.unitSize)) - 1, yP);
        }
        else {
            if (x < 5 * this.unitSize)  return new PIXI.Point(11, yP);
            else return new PIXI.Point(
                  Math.ceil((x - 5 * this.unitSize) / (10 * this.unitSize))-1, yP);
        }
    }

    getCenterOfPoolInPixels(X, Y) {
        let x = X;
        let y = Y;
        if (y % 2 == 1) {
            return new PIXI.Point(
              (10 * x + 10) * this.unitSize, (5 * y + 3) * this.unitSize);
        }
        else {
            return new PIXI.Point(
              (10 * x + 5) * this.unitSize, (y * 5 + 3) * this.unitSize);
        }
    }
}
