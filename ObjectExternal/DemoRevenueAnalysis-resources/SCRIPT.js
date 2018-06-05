function grid(fields, data, expRow, expColumn) {
	DevExpress.viz.currentTheme("generic.light");

   var pivotGridChart = $("#pivotgrid-chart").dxChart({
        commonSeriesSettings: { type: "bar" },
        tooltip: {
            enabled: true,
            format: "currency",
            customizeTooltip: function(args) {
                return {
                    html: args.seriesName + " | Total<div class='currency'>" + args.valueText + "</div>"
                };
            }
        },
        size: { height: 300 },
        adaptiveLayout: { width: 450 }
    }).dxChart("instance");

    var pivotGrid = $("#pivotgrid").dxPivotGrid({
        allowSortingBySummary: true,
        allowFiltering: true,
        showBorders: true,
        showColumnGrandTotals: true,
        showRowGrandTotals: true,
        showRowTotals: false,
        showColumnTotals: false,
        fieldChooser: {
            enabled: true,
            height: 400
        },
        dataSource: {
            fields: fields,
            store: data
        }
    }).dxPivotGrid("instance");

    pivotGrid.bindChart(pivotGridChart, {
        dataFieldsDisplayMode: "splitPanes",
        alternateDataFields: false
    });

    function expand() {
        var dataSource = pivotGrid.getDataSource();
        if (expRow) dataSource.expandHeaderItem("row", [expRow]);
        if (expColumn) dataSource.expandHeaderItem("column", [expColumn]);
    }

    setTimeout(expand, 0);
}