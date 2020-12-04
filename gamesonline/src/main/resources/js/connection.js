
class Connection {
  constructor(game){
    this.game = game;
    this.room = window.location.href.split('room=')[1];
    //console.log(this.room);
    this.xHttpMovies = new XMLHttpRequest();
    this.xHttpAdd = new XMLHttpRequest();
    this.xHttpCheck = new XMLHttpRequest();
  }

  sendMove(row, col){
    let url = this._makeSimpleURL("game/mymove");
    url += "?room=" + this.room;
    url += "&row=" + row;
    url += "&col="+ col;
    var self = this;
    this.xHttpMovies.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        //console.lod("get response: " + url);
        console.log(this.responseText);
        let json = JSON.parse(this.responseText);
        self.game.sentMoveCompleted(json, row, col);
      }
    }
    this.xHttpMovies.open("GET", url, true);
    this.xHttpMovies.setRequestHeader('code', this.game.cookie);
    this.xHttpMovies.send();
  }

  getPlayerMove(){
    let url = this._makeSimpleURL("game/ismoved");
    url += "?room=" + this.room;
    var self = this;
    this.xHttpMovies.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        //console.lod("get response: " + url);
        console.log(this.responseText);
        let json = JSON.parse(this.responseText);
        self.game.checkMoveComplited(json);
      }
    }
    this.xHttpMovies.open("GET", url, true);
    this.xHttpMovies.setRequestHeader('code', this.game.cookie);
    this.xHttpMovies.send();
  }

  addToGame(){
    let url = this._makeSimpleURL('game/addtoroom');
    url += "?room=" + this.room;
    var self = this;
    this.xHttpAdd.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        //console.log("get response: " + url);
        //console.log(this.responseText);
        let json = JSON.parse(this.responseText);
        self.game.addedGame(json);
      }
    }
    this.xHttpAdd.open("GET", url, true);
    this.xHttpAdd.setRequestHeader('code', this.game.cookie);
    this.xHttpAdd.send();
  }

  checkRoom(){
    let url = this._makeSimpleURL("game/checkroom?room=");
    url += this.room;
    var self = this;
    this.xHttpCheck.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        //console.log("get response: " + url);
        //console.log(this.responseText);
        let json = JSON.parse(this.responseText);
        self.game.checkedRoom(json);
      }
    }
    this.xHttpCheck.open("GET", url, true);
    this.xHttpCheck.setRequestHeader('code', this.game.cookie);
    this.xHttpCheck.send();
  }

  _makeSimpleURL(subpath){
    let url = window.location.protocol + "//" + window.location.host + "/" + subpath;
    return url;
  }

}
