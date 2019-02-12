export class charactristicsChart {
    constructor() {}
    initialize() {
        $(".chart-container").each(function(index) {
            var the_data = $(this).data("chart-csv"),
                emptyFlag = $(this).data("empty-flag"),
                graphClassName = 'chart-graph_'+index;

            $(this).find('.chart-graph').addClass(graphClassName);

            if(the_data){
                d3.csv(the_data, function (data) {
                    return {
                        label: data[d3.keys(data)[0]],
                        rating: +data[d3.keys(data)[1]]
                    };
                }, function(data) {
                    if (data){
                      let chartData;

                      if(!emptyFlag) {
                         chartData = data.filter(chartRow => chartRow.rating >= 1 && chartRow.rating <= 9);
                      } else {
                         chartData = data;
                      }

                      d3.select('.'+graphClassName)
                        .selectAll('div.chart-row')
                        .data(chartData)
                        .enter()
                        .append('div')
                        .attr('class', function (d, i) {
                            d3.select(this)
                                .append('div')
                                .attr('class', 'chart-label')
                                .append('span')
                                .text(d.label);
                            var bar = d3.select(this)
                                .append('div')
                                .attr('class', 'chart-bar');
                            for (var j = 1; j <= d.rating; j++) {
                                bar.append('div')
                                    .attr('class', 'chart-bar-box chart-bar-box-'+ j)
                            }
                            return 'chart-row chart-row-' + (i + 1);
                        });
                    }
                });
            }
        })
    }
}

document.addEventListener("DOMContentLoaded", function (event) {
    let $chartContainer = $(".chart-container");
    if ($chartContainer.length !== 0) {
        new charactristicsChart().initialize();
    }
});
