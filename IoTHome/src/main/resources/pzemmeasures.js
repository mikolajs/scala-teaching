var pzemName = "";
function loadPZEMNames(){
    //console.log("load PZEM names");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqNames);
    req.open("GET", "/getPzemNames");
    req.send();
}

function reqNames(){
    //console.log(this.responseText);
    let json = JSON.parse(this.responseText);
    //console.log(json);
    let options = "";
    for(let name of json.pzem_names){
        options += "<option>";
        options += name;
        options += "</option>";
    }
    document.getElementById("pzemNames").innerHTML = options;
    getPzemName();
}

function loadPZEMMeasures(){
    //console.log("load pzem measures");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqMeasures);
    req.open("GET", "/getPzemMeasures?n="+pzemName);
    req.send();
}

function reqMeasures(){
    //console.log(this.responseText);
    let json = JSON.parse(this.responseText);
    //console.log(json);
    let str = ""
    for(let measure of json.measures){
        str += "<tr><td>";
        str += new Date(measure.createTime).toLocaleString("pl-PL");
        str += "</td><td>";
        str += measure.v;
        str += "</td><td>";
        str += measure.c;
        str += "</td><td>";
        str += measure.p;
        str += "</td><td>";
        str += measure.e;
        str += "</td><td>";
        str += measure.f;
        str += "</td><td>";
        str += measure.pf;
        str += "</td></tr>";
    }
    document.getElementById("dataPzem").innerHTML = str;
}

function getPzemName(){
    let e = document.getElementById("pzemNames");
    pzemName = e.options[e.selectedIndex].text;
}

loadPZEMNames();
