
class Searching {
  constructor(){
      this.cookie = this._getCookie();
      this.xhttpCon = new XMLHttpRequest();
      this.xhttp = new XMLHttpRequest();
      this.xHttpLogout = null;
  }

  init(){
    this._loadPlayers();
    document.getElementById('playerCode').innerHTML = this.cookie;
    //console.log("CODE: " + this.cookie);
  }

  closeModal(){
    document.getElementById('popInfoModal').style.display = 'none';
  }
 //connect to game with user:
  connectGame(room){
    let url = this._makePlayerConnectURL(room);
    var self = this;
    this.xhttpCon.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
         let objJSON = JSON.parse(this.responseText);
         if(objJSON.add == "OK") {
             window.location.href = window.location.protocol +
              "//" + window.location.host + "/html/game.html?room=" + room;
         } else {
           self._createPopInfoModal('Błąd', 'Poszukiwany pokój gry już nie istnieje');
         }
      }
    }
    this.xhttpCon.open("GET", url, true);
    this.xhttpCon.setRequestHeader('code', this.cookie);
    this.xhttpCon.send();
  }
///// TODO: go to empty room
  makeOwnGame(){
    let url = this._makeSimpleURL("game/createroom");
    console.log("makeOwnGame url: " + url);
    var self = this;
    this.xhttpCon.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        console.log("makeOwnGame: " + this.responseText);
        let objJSON = JSON.parse(this.responseText);
        if(objJSON.add == "OK") {
          window.location.href = window.location.protocol + "//" + window.location.host + "/html/game.html?room=" +
          objJSON.roomCode;
        } else {
          self._createPopInfoModal('Błąd', 'Brakuje już wolnych pokoi do gry, albo sprawdź zalogowanie');
        }
      }
    }
    this.xhttpCon.open("GET", url, true);
    this.xhttpCon.setRequestHeader('code', this.cookie);
    this.xhttpCon.send();
  }

  logout(){
    let url = this._makeSimpleURL('logout');
    console.log("logout: " + url);
    var self = this;
    this.xHttpLogout = new XMLHttpRequest();
    this.xHttpLogout.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        self._createPopInfoModal("Wylogowano", "Wylogowałeś się z portalu");
        window.setTimeout(() => self.closeModal(), 4000);
        window.setTimeout(() => self._closeCard(), 4000);
      }
    }
    this.xHttpLogout.open("GET", url, true);
    this.xHttpLogout.setRequestHeader("code", this.cookie);
    this.xHttpLogout.send();
  }

  _closeCard(){
     window.location.href = window.location.protocol + "//" + window.location.host + "/html/login.html";
  }

  _getCookie(){
    let c = document.cookie;
    if(c.split('=')[0] == 'gameplayer')  return c.split('=')[1];
    else {
      console.log('Wrong cookie: ' + c.split('=')[0]);
      return "";
    }
  }

  _makePlayerConnectURL(room){
    let url = document.location.protocol + "//" + document.location.host;
    url += "/game/addtoroom";
    url += "?room=" + room;
    return url;
  }

  _makeSimpleURL(last){
    let url = document.location.protocol + "//" + document.location.host;
    url += "/" + last;
    return url;
  }

  _loadPlayers(){
    let url = this._makeSimpleURL('players');
    //console.log("load players started. URL from: \n " + url);
    var self = this;
    this.xhttp.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
     ///todo: make proper
     console.log("Resp for load Players: " + this.responseText);
       let objJSON = JSON.parse(this.responseText);
       let aPlayers = objJSON.users;
       let player = new Object();
       let tableUserBody = document.getElementById("tableUserBody");
       tableUserBody.innerHTML = "";
       for(let i = 0; i < aPlayers.length; i++){
         player = aPlayers[i];
         let strHTML = '<tr><td>' + player.name + '</td><td>' + player.points + '</td></tr>';
         //console.log("TableBody: " + strHTML);
         tableUserBody.innerHTML += strHTML;
       }
       let tableRoomBody = document.getElementById("tableRoomBody");
       tableRoomBody.innerHTML = "";
       let rooms = objJSON.rooms;
       for(let i = 0; i < rooms.length; i++){
         if(rooms[i].player2.name != '') continue;
         let strHTML = '<tr><td>' + rooms[i].player1.name + '</td><td>';
         strHTML += rooms[i].player1. points + '</td><td>';
         strHTML += '<button class="btn btn-info" onclick="searching.connectGame(\'';
         strHTML += rooms[i].roomCode +  '\')">Połącz</button></td></tr>';
         tableRoomBody.innerHTML += strHTML;
       }
     } else if(this.status == 404) { ///todo: change status code
       self._createPopInfoModal("BŁĄD", "Nie można wczytać graczy");
       window.setTimeout(() => self.closeModal(), 4000);
     }
    };
    this.xhttp.open("GET", url, true);
    this.xhttp.setRequestHeader("code", this.cookie);
    this.xhttp.send();
    window.setTimeout(() => this._loadPlayers(), 10000);
  }

  _createPopInfoModal(head, text){
    document.getElementById('popInfoModal').style.display = 'block';
    document.getElementById('popInfoHead').innerHTML = head;
    document.getElementById('popInfoText').innerHTML = text;
  }

}
