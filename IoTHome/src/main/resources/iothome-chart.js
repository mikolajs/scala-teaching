/*
boiler_info -- temperture from boiler, most important is boiler_temperature
boiler_set_temperature -- counted temperature for boiler after check by boiler server

*/
const now = new Date().getTime();
const DATALOAD = 3;

const boilerInfoDataList = [];
const boilerSetDataList = [];
//const boilerExpectedTempDataList = [];
const dataBoilerInfoChart = [];
const dataBoilerSetChart = [];
const roomsMap = new Map();

let dataLoaded = 0;
const DATA_COUNT = 60; //300 minuts
const labels = [];
for(let i = 0; i < DATA_COUNT; i++) labels.push(5*i);

function createMinutesAgo(t){
    return Math.ceil((now - t)/60000);
}

function createDataChartArray(tempArr, dataArr){
    console.log('create data Array');
    console.log(tempArr);
    let lastIndex = 0;
    let lastTemp = 0.0;
    while(tempArr[lastIndex][0] < 0) lastIndex++;
    for(let i = 8; i <= DATA_COUNT*5; i+= 8){
        //console.log(i);
        //while(tempArr[lastIndex][0] < i - 5) lastIndex++;
        if(lastIndex >= tempArr.length) break;
        while(tempArr[lastIndex][0] < i - 8 && lastIndex < tempArr.length - 1) lastIndex++;
        console.log(tempArr[lastIndex]);
        if(tempArr[lastIndex][0] <= i && tempArr[lastIndex][0] >= i - 8) {
            //lastTemp = tempArr[lastIndex][1];
            dataArr.push(tempArr[lastIndex][1]);
        } else dataArr.push(0.0);
        console.log(i, tempArr[lastIndex][1]);
    }
    //console.log(dataArr);
}

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
    for(m of json.measures) {
        roomsMap.set(m.th, [])
    }
    //console.log(measureDataList);
    //console.log(roomsMap);
    for(const m of json.measures){
        //console.log(md);
        let minAgo = createMinutesAgo(m.t);
        if(minAgo > 300) continue;
        let arr = [];
        arr.push(minAgo);
        arr.push(m.T);
        let roomsData = roomsMap.get(m.th);
        roomsData.push(arr);
        roomsMap.set(m.th, roomsData);
    }
    dataLoaded++;
    if(dataLoaded == DATALOAD) runMkChart();
}

function loadBoilerSet(){
    console.log("load boiler set");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqBoilerSet);
    req.open("GET", "info/boiler_set_temp");
    req.send();
}
function reqBoilerSet(){
    let json = JSON.parse(this.responseText);
    let str = '';
    let dataArr = [];
    for(b of json.boiler) {
        dataArr.push([createMinutesAgo(b.t), b.T]);
    }
    console.log(json)
    console.log('load boiler set');
    createDataChartArray(dataArr, dataBoilerSetChart);
    //console.log(boilerSetDataList);
    dataLoaded++;
    if(dataLoaded == DATALOAD) runMkChart();
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
    let dataArr = []
    for(b of json.boiler) {
        dataArr.push([createMinutesAgo(b.time), b.boilerTemp]);
    }
    console.log('load boiler info');
    createDataChartArray(dataArr, dataBoilerInfoChart);
    //console.log(boilerInfoDataList);
    dataLoaded++;
    if(dataLoaded == DATALOAD) runMkChart();
}
/**
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
       boilerExpectedTempDataList.push([createMinutesAgo(b.t, b.T)]);
    }
    //console.log(boilerExpectedTempDataList);
    dataLoaded++;
    if(dataLoaded == 4) runMkChart();
}
*/

loadMeasures();
loadBoilerSet();
loadBoilerInfo();
//loadBoilerExpectedTemp();

const ctx = document.getElementById('myChart');
const Utils = ChartUtils.init();

const data = {
    labels: labels,
    datasets: [
        {
            label: 'Boiler Temperature',
            data: dataBoilerInfoChart,
            borderColor: Utils.CHART_COLORS.yellow,
            backgroundColor: Utils.transparentize(Utils.CHART_COLORS.yellow, 0.5),
        },
        {
            label: 'Temperature set',
            data: dataBoilerSetChart,
            borderColor: Utils.CHART_COLORS.red,
            backgroundColor: Utils.transparentize(Utils.CHART_COLORS.red, 0.5),
        }
    ]
};


const config = {
    type: 'line',
    data: data,
    options: {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: 'Temperatures'
            }
        }
    },
};



function insertDataSetMeasures(){
    let colorsChartForTemp = [
        Utils.CHART_COLORS.orange, Utils.CHART_COLORS.blue, Utils.CHART_COLORS.purple, Utils.CHART_COLORS.green
    ];
    let c = 0;
   for(let k of roomsMap.keys()){
       let dataArr = [];
       createDataChartArray(roomsMap.get(k), dataArr);
      data.datasets.push(
          {
            label: 'Temperature measure ' + k,
            data: dataArr,
            borderColor: colorsChartForTemp[c],
            backgroundColor: Utils.transparentize(colorsChartForTemp[c++], 0.5),
          }
      );
   }
}

let chart;

function runMkChart(){
  insertDataSetMeasures();
  chart =  new Chart(ctx, config);
}
