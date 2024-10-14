
const now = new Date().getTime();

const boilerInfoDataList = [];
const boilerSetDataList = [];
const measureDataList = [];
const boilerExpectedTempDataList = [];
const dataBoilerInfo = [];
const dataBoilerSet = [];
const dataMeasureA = [];
const dataMeasureB = [];

function createMinutesAgo(t){
    return Math.ceil((now - t)/60000);
}

const server = 'http://192.168.0.218';

function loadMeasures(){
    console.log("load measures");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqMeasures);
    req.open("GET", server + ":8989/info/measures");
    req.send();
}
function reqMeasures(){
    //console.log(this.responseText);
    let json = JSON.parse(this.responseText);
    for(m of json.measures) {
        measureDataList.push([createMinutesAgo(m.t), m.T, m.th]);
    }
    console.log(measureDataList);
}

function loadBoilerSet(){
    console.log("load boiler set");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqBoilerSet);
    req.open("GET", server + ":8989/info/boiler_set_temp");
    req.send();
}
function reqBoilerSet(){
    let json = JSON.parse(this.responseText);
    let str = '';
    for(b of json.boiler) {
        boilerSetDataList.push([createMinutesAgo(m.t), m.T]);
    }
    console.log(boilerSetDataList);
}

function loadBoilerInfo(){
    console.log("load boiler info");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqBoilerInfo);
    req.open("GET", server + ":8989/info/boiler_info");
    req.send();
}
function reqBoilerInfo(){
    //console.log(this.responseText);
    let json = JSON.parse(this.responseText);
    let str = '';
    for(b of json.boiler) {
        boilerInfoDataList.push([createMinutesAgo(b.time), b.boilerTemp]);
    }
    console.log(boilerInfoDataList);
}

function loadBoilerExpectedTemp(){
    console.log("load Expected Temp");
    const req = new XMLHttpRequest();
    req.addEventListener("load", reqBoilerExpectedTemp);
    req.open("GET", server + ":8989/info/expected_temperature");
    req.send();
}
function reqBoilerExpectedTemp(){
    //console.log(this.responseText);
    let json = JSON.parse(this.responseText);
    let str = '';
    for(b of json.boiler) {
       boilerExpectedTempDataList.push([createMinutesAgo(b.t, b.T)]);
    }
    console.log(json);
}

loadMeasures();
loadBoilerSet();
loadBoilerInfo();
loadBoilerExpectedTemp();



const ctx = document.getElementById('myChart');
const Utils = ChartUtils.init();

const DATA_COUNT = 60;

const labels = [];
for(let i = 0; i < DATA_COUNT; i++) labels.push(5*i);

const data = {
    labels: labels,
    datasets: [
        {
            label: 'Temperature measure A',
            data: dataMeasureA,
            borderColor: Utils.CHART_COLORS.green,
            backgroundColor: Utils.transparentize(Utils.CHART_COLORS.red, 0.5),
        },
        {
             label: 'Temperature measure B',
             data: dataMeasureB,
             borderColor: Utils.CHART_COLORS.blue,
             backgroundColor: Utils.transparentize(Utils.CHART_COLORS.red, 0.5),
        },
        {
            label: 'boiler info',
            data: dataBoilerInfo,
            borderColor: Utils.CHART_COLORS.yellow,
            backgroundColor: Utils.transparentize(Utils.CHART_COLORS.blue, 0.5),
        },
        {
            label: 'Temperature expected',
            data: dataBoilerSet,
            borderColor: Utils.CHART_COLORS.red,
            backgroundColor: Utils.transparentize(Utils.CHART_COLORS.blue, 0.5),
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

const actions = [
    {
        name: 'Randomize',
        handler(chart) {
            chart.data.datasets.forEach(dataset => {
                dataset.data = Utils.numbers({count: chart.data.labels.length, min: -100, max: 100});
            });
            chart.update();
        }
    },
    {
        name: 'Add Dataset',
        handler(chart) {
            const data = chart.data;
            const dsColor = Utils.namedColor(chart.data.datasets.length);
            const newDataset = {
                label: 'Dataset ' + (data.datasets.length + 1),
                backgroundColor: Utils.transparentize(dsColor, 0.5),
                borderColor: dsColor,
                data: Utils.numbers({count: data.labels.length, min: -100, max: 100}),
            };
            chart.data.datasets.push(newDataset);
            chart.update();
        }
    },
    {
        name: 'Add Data',
        handler(chart) {
            const data = chart.data;
            if (data.datasets.length > 0) {
                data.labels = Utils.months({count: data.labels.length + 1});

                for (let index = 0; index < data.datasets.length; ++index) {
                    data.datasets[index].data.push(Utils.rand(-100, 100));
                }

                chart.update();
            }
        }
    },
    {
        name: 'Remove Dataset',
        handler(chart) {
            chart.data.datasets.pop();
            chart.update();
        }
    },
    {
        name: 'Remove Data',
        handler(chart) {
            chart.data.labels.splice(-1, 1); // remove the label first

            chart.data.datasets.forEach(dataset => {
                dataset.data.pop();
            });

            chart.update();
        }
    }
];

let chart;

function createDataSet(){
    let i1 = 0;
    let i2 = 0;
    let i3 = 0;
    let lastT_boilerSet = 0;
    let lastT_boilerInfo = 0;
    let lastT_measureA = 0;
    let lastT_measureB = 0;
    for(let i = 5; i <= DATA_COUNT*5; i+= 5){

    }
}


function mkChart(){
  createDataSet();
  chart =  new Chart(ctx, config);
}