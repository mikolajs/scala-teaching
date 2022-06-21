
function change(elem){
 console.log(elem.value);
}

function getTemperatures(){
 console.log('checkTemperatures');
 let req = new XMLHttpRequest();
 req.open('GET', 'http://localhost:9900/get', true);
 req.onreadystatechange = function (aEvt) {
  if (req.readyState == 4) {
     if(req.status == 200)
      setTemperatures(req.responseText);
     else
      setInfo("Błąd podczas ładowania strony");
     }
  };
  req.send(null);
}

function setInfo(str){
 document.getElementById('info').innerHTML = str;
}

function setTemperatures(data){
  let json = JSON.parse(data);
  if(json.measures != undefined){
    setInfo("");
    let div = document.getElementById('measures');
    div.innerHTML = "";
    for(let i = 0; i < json.measures.length; i++){
      div.innerHTML += (createTempInfo(json.measures[i].name, json.measures[i].temp));
    }
  }
}

function createTempInfo(i, t){
  return '<h2>Licznik ' + i + ': <span>' + t + '</span></h2>';
}
