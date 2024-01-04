
// Define the function in the global scope
function initPieChart(labels, data) {
    var ctx = document.getElementById('pieChart').getContext('2d');
    var pieChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                data: data
            }]
        }
    });
}
document.addEventListener('DOMContentLoaded', function () {
    // Call the function to initialize the pie chart
    initPieChart(allocationLabels, allocationData);
    initLineChart();
});

function initLineChart() {
    var ctx = document.getElementById('myChart1').getContext('2d');
    var myChart = new Chart(ctx, {
        type: 'line', // Specify the chart type (e.g., 'bar', 'line', 'pie', etc.)
        data: {
            labels: ['Label 1', 'Label 2', 'Label 3'], datasets: [{
                label: 'Performance', data: [10, 20, 30], backgroundColor: 'rgba(75, 192, 192, 0.2)', // Customize the background color
                borderColor: 'rgba(75, 192, 192, 1)', // Customize the border color
                borderWidth: 1 // Customize the border width
            }]
        }, options: {
            // Add any additional options here
        }
    });
}