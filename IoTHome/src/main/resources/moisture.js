var deviceName = "";
function loadDevices(){
    console.log("load Watering device names");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqDevices);
    req.open("GET", "/getMoistureDevices");
    req.send();
}

function reqDevices(){
    if (this.status == 200){
        //console.log(this.responseText);
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

function loadMoisture(){
    getDeviceName();
    console.log("load moisture for ", deviceName);
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqMoisture);
    req.open("GET", "/getMoistureInfo?d="+deviceName);
    req.send();
}

function reqMoisture(){
    if(this.status == 200){
        console.log(this.responseText);
        let json = JSON.parse(this.responseText);
        //        console.log(json);
        let str = ""
        for(let info of json.moistureTimes){
            str += "<tr><td>";
            str += new Date(info.time).toLocaleString("pl-PL");
            str += '</td><td>';
            str += info.moisture;
            str += "</td></tr>";
        }
        document.getElementById("moistureData").innerHTML = str;

    } else console.log("Eror Moisture: " + this.status);

}

function getDeviceName(){
    let e = document.getElementById("deviceNames");
    if(e.options.length > 0) deviceName = e.options[e.selectedIndex].text;
}

loadDevices();