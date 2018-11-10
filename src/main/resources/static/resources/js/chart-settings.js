var ctx = document.getElementById("ground").getContext('2d');
var groundData = $.parseJSON($("#ground").attr("data"));
var groundLabels = $.parseJSON($("#ground").attr("labels"));
var myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: groundLabels,
        datasets: [{
            label: 'Влажность',
            data: groundData,
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)',
                'rgba(54, 162, 235, 0.2)',
            ],
            borderColor: [
                'rgba(255,99,132,1)',
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