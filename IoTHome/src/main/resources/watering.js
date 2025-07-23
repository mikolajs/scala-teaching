var deviceName = "";
function loadDevices(){
    console.log("load Watering device names");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqDevices);
    req.open("GET", "/getWateringDevices");
    req.send();
}

function reqDevices(){
    if (this.status == 200){
        console.log(this.responseText);
        let json = JSON.parse(this.responseText);
//        console.log(json);
        let options = "";
        for(let name of json.devices){
            options += "<option>";
            options += name;
            options += "</option>";
        }
        document.getElementById("deviceNames").innerHTML = options;
        getDeviceName();
    } else {
        console.log("status code " + this.status);
    }
}

function loadWatering(){
    getDeviceName();
    console.log("load watering for ", deviceName);
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqWatering);
    req.open("GET", "/getWateringInfo?d="+deviceName);
    req.send();
}

function reqWatering(){
    if(this.status == 200){
        console.log(this.responseText);
        let json = JSON.parse(this.responseText);
//        console.log(json);
        let str = ""
        for(let wt of json.wateringTimes){
            str += "<tr><td>";
            str += new Date(wt).toLocaleString("pl-PL");
            str += "</td></tr>";
        }
        document.getElementById("wateringData").innerHTML = str;

    } else console.log("Eror watering: " + this.status);

}

function reloadScheduler(){
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqScheduler);
    req.open("GET", "/reloadWateringScheduler");
    req.send();
}

function reqScheduler(){
    if(this.status == 200) console.log("Reloaded Watering Config");
    else console.log("Eror watering: " + this.status);
}

function getDeviceName(){
    let e = document.getElementById("deviceNames");
    if(e.options.length > 0) deviceName = e.options[e.selectedIndex].text;
}

loadDevices();