
class Game {
  //TODO:
  // 1. wrong answer from serwer after make move
  // 2. player2 not listen for move player1 ???
  constructor(){
   let type = "WebGL"
   if(!PIXI.utils.isWebGLSupported()){ type = "canvas" }
   PIXI.utils.sayHello(type);
   this.X = 600; //col
   this.Y = 600; //row
   this.app = new PIXI.Application({
     width: this.X, height: this.Y,  backgroundColor: 0xefefef,
       antialias: false,  transparent: false});
   document.getElementById('gameBoard').appendChild(this.app.view);
   //info about elements - now empty
   this.pools = new Array(60).fill(0).map(x => Array(60).fill('.'));
   this.visibleBoard = Array(400).fill("");
   this.container = new PIXI.Container();
   this.tX = PIXI.Texture.from("/img/X.png");
   this.tO = PIXI.Texture.from("/img/O.png");
   this.tE = PIXI.Texture.from("/img/E.png");
   this.tX_R = PIXI.Texture.from("/img/X_R.png");
   this.tO_R = PIXI.Texture.from("/img/O_R.png");
   //console.log("tekstury 3 i pól: " + this.pools.length*this.pools[0].length);
   this.timeToEnd = 20; //not use now
   this.startRow = 20;
   this.startCol = 20;
   this.lastX = new Point(-1, -1);
   this.lastO = new Point(-1, -1);
   this.app.stage.addChild(this.container);
   this.cookie = this._getCookie();
   this.connection = new Connection(this);
   this.started = false;
   this.yourTurn = true;
   this.player1;
   this.player2;
   this.tryCreateGame = false;
   this.miniMap = new MiniMap(this.pools);
   this._rewriteBoard();
   this.addToGame();
   setTimeout(() => this.checkRoom(), 5000);
  }

  _rewriteBoard(){
    //this._test();
    this.container.removeChildren(0,this.container.children.length);
    for(let i = 0; i < 20; i++){
      for(let j = 0; j < 20; j++){
        let e = i*20+j;
        if(this.pools[i+this.startRow][j+this.startCol] == 'x') {
          if(this.lastX.row == i+ this.startRow && this.lastX.col == j+ this.startCol)
            this.visibleBoard[e] = new PIXI.Sprite(this.tX_R);
          else this.visibleBoard[e] = new PIXI.Sprite(this.tX);
        } else if(this.pools[i+this.startRow][j+this.startCol] == 'o'){
          if(this.lastO.row == i+ this.startRow && this.lastO.col == j+ this.startCol)
          this.visibleBoard[e] =  new PIXI.Sprite(this.tO_R);
          else this.visibleBoard[e] =  new PIXI.Sprite(this.tO);
        } else {
            this.visibleBoard[e] =  new PIXI.Sprite(this.tE);
            this.visibleBoard[e].on('click', () => {
              this.mkMove(i + this.startRow, j + this.startCol);
            });
        }
        this.visibleBoard[e].x = j*30;
        this.visibleBoard[e].y = i*30;
        this.visibleBoard[e].interactive = true;
        this.visibleBoard[e].buttonMode = true;
        this.container.addChild(this.visibleBoard[e]);
      }
    }
    this.miniMap.setStartPosition(this.startRow, this.startCol);
  }

  _getCookie(){
    let c = document.cookie;
    if(c.split('=')[0] == 'gameplayer')  return c.split('=')[1];
    else {
      console.log('Wrong cookie: ' + c.split('=')[0]);
      return "";
    }
  }
  //repeat before gamer not connect
  addToGame(){ this.connection.addToGame();  }

  addedGame(json){
    console.log("addedGame: " + JSON.stringify(json));
    if(json.add == "NO") {
      if(json.error == "not logged") {
        console.log("Nie jesteś zalogowany!!!");
        return;
      }
      else if(json.error == "alredy in") console.log("Jesteś już w grze");
    }
      this.checkRoom();
  }

  checkRoom() { this.connection.checkRoom(); }

  checkedRoom(json){
    console.log("checkedRoom: " + JSON.stringify(json));
    if( json.roomInfo.player1.code == '' || json.roomInfo.player2.code == '') {
      setTimeout(() => {
      this.checkRoom();}, 5000);
      return;
    }

    if(json.roomInfo.player1.code == this.cookie){
      this.player1 = new Player(json.roomInfo.player1.name, 'o', true, true);
      this.player2 = new Player(json.roomInfo.player2.name, 'x', false, false);
    } else if(json.roomInfo.player2.code == this.cookie) {
      this.player1 = new Player(json.roomInfo.player1.name, 'o', true, false);
      this.player2 = new Player(json.roomInfo.player2.name, 'x', false, true);
    }  else {
      console.log("Nie możesz grać. To nie Twoja gra.");
      return;
    }

    this._whoPlayNow();
    this._refreshPlayersInfo();
    this.started = true;
  }


  sendMove(r, c){this.connection.sendMove(r, c);}

  sentMoveCompleted(json, r, c){
    console.log("sentMoveCompleted" + json.add);
    if(json.add == 'OK'){ ///how json ???????????????!!!
      if(json.info == 'won'){
        document.getElementById('whoMove').innerHTML = 'Koniec partii...';
        this._makeNextGame();
        return;
      } else {
        document.getElementById('whoMove').innerHTML = 'Oczekiwanie na ruch gracza';
        this.checkMove();
        this.yourTurn = false;
      }
    } else {
      document.getElementById('whoMove').innerHTML = 'Błąd przesłania ruchu';
      // setTimeout(() => this.sendMove(r, c), 1000);
    }
  }

  checkMove(){this.connection.getPlayerMove();}

  checkMoveComplited(json){
    console.log("checkMovieComplited: " + json.check);
    if(json.check == "OK") console.log("move of USER: " + json.user);
    if(json.check == "NO" || json.user == this.cookie) setTimeout(() => this.checkMove(), 5000);
    else {
      if(json.effect == 'won') {
        document.getElementById('whoMove').innerHTML = 'Przeciwnik wygrał grę';
        this._makeNextGame();
        return;
      }
      this._loadedMove(json.row, json.col);
      document.getElementById('whoMove').innerHTML = 'Twój ruch';
    }
  }

///change to write only on pools and adding only lastX, lastY _rewriteBoard make rest
  mkMove(r, c){
    if(!this.started) return;
    if(this.pools[r][c] != '.') return;
    console.log("clicked move on: " + r + ", " +c);
    if(this.yourTurn){
      if(this.player2.you) {
        this._addMove(r, c, 'x');
      } else {
        this._addMove(r, c, 'o');
      }
        //this._timesToEnd(3, false); //// TODO: implement function
      this._rewriteBoard();
      // this.yourTurn = false;
      if(this.player1.you){
        this.player1.now = false;
        this.player2.now = true;
      } else {
        this.player1.now = true;
        this.player2.now = false;
      }
      this._whoPlayNow();
      //document.getElementById('whoMove').innerHTML = 'Wysyłanie ruchu';
      this.sendMove(r, c);
    }
  }

 _loadedMove(r, c){
    if(!this.started) return;
    if(this.pools[r][c] != '.') return;
    console.log("loaded move on: " + r + ", " +c);
    if(!this.yourTurn){
      if(this.player1.you) {
        this._addMove(r, c, 'x');
      } else {
        this._addMove(r, c, 'o');
      }
        //this._timesToEnd(3, false); //// TODO: implement function
      this._rewriteBoard();
      // this.yourTurn = false;
      if(this.player1.you){
        this.player1.now = true;
        this.player2.now = false;
      } else {
        this.player1.now = false;
        this.player2.now = true;
      }
      this._whoPlayNow();
      this.yourTurn = true;
      //document.getElementById('whoMove').innerHTML = 'Wysyłanie ruchu';
    }
 }


  _addMove(r, c, sign){
      let s = r*20+c;
      if(this.pools[r][c] == '.') {
        if(sign == 'x') {
          this.lastX.row = r;
          this.lastX.col = c;
          this.pools[r][c] = sign;
        } else {
          this.lastO.row = r;
          this.lastO.col = c;
          this.pools[r][c] = sign;
        }
      }
     this._rewriteBoard();
  }

  _whoPlayNow(){
    if(this.player1.you){
      if(this.player1.now){
        document.getElementById('whoMove').innerHTML = 'Twój ruch';
        this.yourTurn = true;
      } else {
        document.getElementById('whoMove').innerHTML = 'Czekaj na ruch gracza';
        this.yourTurn = false;
        this.checkMove();
      }
    } else {
      if(this.player2.now){
        document.getElementById('whoMove').innerHTML = 'Twój ruch';
        this.yourTurn = true;
      } else {
        document.getElementById('whoMove').innerHTML = 'Czekaj na ruch gracza';
        this.yourTurn = false;
        this.checkMove();
      }
    }
  }

  _refreshPlayersInfo(){
    if(this.player1 != undefined){
      document.getElementById('player1Name').innerHTML = this.player1.name;
      document.getElementById('player1Points').innerHTML = this.player1.point;
    }
    if(this.player2 != undefined){
      document.getElementById('player2Name').innerHTML = this.player2.name;
      document.getElementById('player2Points').innerHTML = this.player2.point;
    }
  }

  _makeNextGame(){
    console.log("Next game not implemented");
    setTimeout(() => {
      document.getElementById('whoMove').innerHTML = 'Jeszcze nie działa opcja';
    }, 5000);
  }

  scrollUp(){
    if(this.startRow >= 5) {
      this.startRow -= 5;
      this._rewriteBoard();
      this.miniMap.setStartPosition(this.startRow, this.startCol);
    }
  }
  scrollDown(){
    if(this.startRow <= 35) {
      this.startRow += 5;
      this._rewriteBoard();
      this.miniMap.setStartPosition(this.startRow, this.startCol);
    }
  }
  scrollRight(){
    if(this.startCol <= 35) {
      this.startCol += 5;
      this._rewriteBoard();
      this.miniMap.setStartPosition(this.startRow, this.startCol);
    }
  }
  scrollLeft(){
    if(this.startCol >= 5) {
      this.startCol -= 5;
      this._rewriteBoard();
      this.miniMap.setStartPosition(this.startRow, this.startCol);
    }
  }


  _test(){
    for(let i = 0; i < 60; i++){
        console.log(this.pools[i].join(''));
    }
  }

}
