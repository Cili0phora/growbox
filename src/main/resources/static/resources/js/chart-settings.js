var ctx = document.getElementById("ground").getContext('2d');
var groundData = $.parseJSON($("#ground").attr("data"));
var labels = $.parseJSON($("#ground").attr("labels"));
var myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: labels,
        datasets: [{
            label: 'Влажность',
            data: groundData,
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)',
            ],
            borderColor: [
                'rgba(255,99,132,1)',
            ],
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }
});

var ctx = document.getElementById("air").getContext('2d');
var temperatureData = $.parseJSON($("#air").attr("temperature"));
var airHumData = $.parseJSON($("#air").attr("airHum"));
var labels = $.parseJSON($("#air").attr("labels"));
var myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: labels,
        datasets: [{
            label: 'Температура',
            data: temperatureData,
            backgroundColor: [
                'rgba(255, 206, 86, 0.2)',
            ],
            borderColor: [
                'rgba(255, 206, 86, 1)',
            ],
            borderWidth: 1
        },
        {
            label: 'Влажность',
            data: airHumData,
            backgroundColor: [
                'rgba(54, 162, 235, 0.2)',
            ],
            borderColor: [
                'rgba(54, 162, 235, 1)',
            ],
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }
});