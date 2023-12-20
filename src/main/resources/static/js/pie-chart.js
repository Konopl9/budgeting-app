
// Define the function in the global scope
function initPieChart(labels, data) {
    /* Prepare data for Chart.js */
    var colors = generateColors(data.length);

    /* Chart.js Configuration */
    var ctx = document.getElementById('pieChart').getContext('2d');
    var pieChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                data: data,
                backgroundColor: colors,
            }]
        }
    });
}

// Function to generate random colors
function generateColors(count) {
    var colors = [];
    for (var i = 0; i < count; i++) {
        colors.push('#' + Math.floor(Math.random() * 16777215).toString(16));
    }
    return colors;
}
document.addEventListener('DOMContentLoaded', function () {
    // Call the function to initialize the pie chart
    initPieChart(allocationLabels, allocationData);
});

document.addEventListener('DOMContentLoaded', function () {
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
});