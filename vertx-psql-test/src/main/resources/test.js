
function loadMeasures(){
    console.log("load measures");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqMeasures);
    req.open("GET", "/info/measures");
    req.send();
}
function reqMeasures(){
    //console.log(this.responseText);
    let json = JSON.parse(this.responseText);
    let str = '';
    for(m of json.measures) {
        str += '<tr><td>';
        str += m.th;
        str += '</td><td>';
        str += new Date(m.t).toLocaleString('pl-Pl');
        str += '</td><td>';
        str +=  m.T;
        str += '</td></tr>';
    }
    document.getElementById('measuresTab').innerHTML = str;

    //console.log(json);
}

function loadBoilerSet(){
    console.log("load boiler set");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqBoilerSet);
    req.open("GET", "/info/boiler_set_temp");
    req.send();
}
function reqBoilerSet(){
    let json = JSON.parse(this.responseText);
    let str = '';
    for(b of json.boiler) {
        str += '<tr><td>';
        str += new Date(b.t).toLocaleString('pl-Pl');
        str += '</td><td>';
        str +=  b.T;
        str += '</td></tr>';
    }
    document.getElementById('boilerSetTab').innerHTML = str;
    //console.log(json);
}

function loadBoilerInfo(){
    console.log("load boiler info");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqBoilerInfo);
    req.open("GET", "/info/boiler_info");
    req.send();
}
function reqBoilerInfo(){
    //console.log(this.responseText);
    let json = JSON.parse(this.responseText);
    let str = '';
    for(b of json.boiler) {
        str += '<tr><td>';
        str += new Date(b.time).toLocaleString('pl-Pl');
        str += '</td><td>';
        str +=  b.returnTemp;
        str += '</td><td>';
        str +=  b.boilerTemp;
        str += '</td><td>';
        str +=  b.setpointBound;
        str += '</td><td>';
        str +=  b.oemDiagnostic;
        str += '</td></tr>';

    }
    document.getElementById('boilerInfoTab').innerHTML = str;
    //console.log(json);
}
function loadBoilerExpectedTemp(){
    console.log("load Expected Temp");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqBoilerExpectedTemp);
    req.open("GET", "/info/expected_temperature");
    req.send();
}
function reqBoilerExpectedTemp(){
    //console.log(this.responseText);
    let json = JSON.parse(this.responseText);
    let str = '';
    for(b of json.boiler) {
        str += '<tr><td>';
        str += new Date(b.t).toLocaleString('pl-Pl');
        str += '</td><td>';
        str +=  b.T;
        str += '</td></tr>';
    }
    document.getElementById('expectedTempTab').innerHTML = str;
    //console.log(json);
}

loadMeasures();
loadBoilerSet();
loadBoilerInfo();
loadBoilerExpectedTemp();
