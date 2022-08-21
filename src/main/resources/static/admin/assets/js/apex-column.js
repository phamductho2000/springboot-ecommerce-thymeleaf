document.getElementById('basic-column').innerHTML = '';
var colors = ["#727cf5", "#0acf97", "#fa5c7c"],
    dataColors = $("#basic-column").data("colors");
dataColors && (colors = dataColors.split(","));
var options = {
        chart: {height: 396, type: "bar", toolbar: {show: !1}},
        plotOptions: {bar: {horizontal: !1, endingShape: "rounded", columnWidth: "55%"}},
        dataLabels: {enabled: !1},
        stroke: {show: !0, width: 2, colors: ["transparent"]},
        colors: colors,
        series: [
            {name: "Doanh thu", data: [30, 44, 55, 57, 56, 61, 58, 63, 60, 66]},
            {name: "Lợi nhuận", data: [40, 76, 85, 101, 98, 87, 105, 91, 114, 94]},
            // { name: "Free Cash Flow", data: [35, 41, 36, 26, 45, 48, 52, 53, 41] },
        ],
        xaxis: {categories: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct"]},
        legend: {offsetY: 7},
        yaxis: {title: {text: " (VND)"}},
        fill: {opacity: 1},
        grid: {row: {colors: ["transparent", "transparent"], opacity: 0.2}, borderColor: "#f1f3fa", padding: {bottom: 5}},
        tooltip: {
            y: {
                formatter: function (o) {
                    return o.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
                },
            },
        },
    },
    chart = new ApexCharts(document.querySelector("#basic-column"), options);
chart.render();
