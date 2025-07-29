let isFull = false;
function showFull(elem){
   ///TODO: show full size image
    if(!isFull){
        document.getElementById('fulldiv').style.display = "block";
        document.getElementById('fullimg').src = elem.src;
        isFull = true;
    }
}

function closeFull(){
    document.getElementById('fulldiv').style.display = "none";
    isFull = false;
}

function loadCameras(){
    let t = getTime();
    let d = getCameraName();
    console.log("load cameras from", d);
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqCameras);
    req.open("GET", "/getCameraImages?d="+d+"&t="+t);
    req.send();
}

function reqCameras(){
    //console.log(this.responseText);
    let json = JSON.parse(this.responseText);
    console.log(json);
    let imagesHtml = "";
    for(let imgSrc of json){
        imagesHtml += '<img class="imagesView" src="';
        imagesHtml += imgSrc.cameraName;
        imagesHtml += '" alt="not found img" width="300" onclick="showFull(this)">';
    }
    //console.log(imagesHtml);
    document.getElementById('images').innerHTML = imagesHtml;
}

function loadCameraNames(){
    //console.log("load cameras names");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqCameraNames);
    req.open("GET", "/getCameraNames");
    req.send();
}

function reqCameraNames(){
    let json = JSON.parse(this.responseText);
    //console.log(json);
    let options = "";
    for(name of json.devices){
        options += "<option>";
        options += name;
        options += "</option>";
    }
    document.getElementById("cameras").innerHTML = options;
}

function getCameraName(){
    let e = document.getElementById("cameras");
    return e.options[e.selectedIndex].text;
}

function getTime(){
    let date = picker.getDate();
    console.log(date.getTime(), new Date().getTime());
    return date.getTime();
}

loadCameraNames();
const picker = new Picker(document.getElementById('picker'), {
    date: new Date(),
    format: ' DD.MM.YYYY HH:mm',
    controls: true,
});
picker.pick();
document.getElementById('fulldiv').style.display="none";