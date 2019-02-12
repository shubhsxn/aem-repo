export class hybridTable {
    constructor() {}

    initialize() {
        var newFlagText = $(".hybrid-table-container").data("new-keyword"),
            _this = this;

        d3.csv($(".hybrid-table-container").data("hybrid-csv"), function(data) {
            var titles = d3.keys(data[0]).splice(2),
                newFlagKey = d3.keys(data[0])[0],
                productUrlKey = d3.keys(data[0])[1],
                productLinks = data.map(a => a[productUrlKey]),
                rows;

            var hybridTable = d3.select('.table-container')
                .append('table')
                .attr('class', 'hybrid-table');

            hybridTable.append('thead').append('tr')
                .selectAll('th')
                .data(titles).enter()
                .append('th')
                .attr('class', function (d, i) {
                    if (i < 4) {
                        return 'header-mobile';
                    } else if (i >= 4 && i < 6) {
                        return 'header-tablet';
                    }
                    else {
                        return 'header-desktop';
                    }
                })
                .append('div')
                .attr('class', 'th-wrapper')
                .append('div')
                .attr('class', 'header-text')
                .text(function(d){
                    return d;
                })
                .select(function(){
                    return this.parentNode;
                })

                // Sorting arrow up append
                .append('div')
                .attr('class', 'arrow-container')
                .append('a')
                .attr('href', 'javascript:void(0);')
                .attr('class', 'arrow-container-up')
                .attr('data-analytics-type', 'hybrid-table-sort-asc')
                .attr('data-analytics-value', function (d) {
                    return d;
                })
                .on('click', function(d) {
                    rows.sort(function(a, b) {
                        if(isNaN(a[d] - 1) || a[d] === "") {
                            return a[d].localeCompare(b[d]);
                        } else {
                            return a[d] - b[d];
                        }
                    });
                })
                .select(function(){
                    return this.parentNode;
                })
                // Sorting arrow down append
                .append('a')
                .attr('href', 'javascript:void(0);')
                .attr('class', 'arrow-container-down')
                .attr('data-analytics-type', 'hybrid-table-sort-desc')
                .attr('data-analytics-value', function (d) {
                    return d;
                })
                .on('click', function(d) {
                    rows.sort(function(a, b) {
                        if(isNaN(a[d] - 1) || a[d] === "") {
                            return b[d].localeCompare(a[d]);
                        } else {
                            return b[d] - a[d];
                        }
                    });
                });

            rows = hybridTable.append('tbody').selectAll('tr')
                .data(data).enter()
                .append('tr')
                .attr('class', function(d){
                    if(d[newFlagKey].toLowerCase() === 'yes') {
                        return 'table-body-row new-flagged';
                    }
                    else {
                        return 'table-body-row';
                    }
                });

            rows.selectAll('td')
                .data(function (d) {
                    return titles.map(function (k) {
                        return { 'value': d[k], 'name': k};
                    });
                }).enter()
                .append('td')
                .attr('class', function (d, i) {
                    if (i === 0) {
                        return 'table-row';
                    }
                    else if (i > 0 && i < 4) {
                        return 'table-cell';
                    } else if (i >= 4 && i < 6) {
                        return 'table-cell-tablet';
                    }
                    else {
                        return 'table-cell-desktop';
                    }
                })
                .append('div')
                .attr('class', 'body-cell-value')
                .attr('data-analytics-type', 'hybrid-table-item')
                .attr('data-analytics-value', function (d) {
                    return d.value;
                })
                .text(function (d) {
                    return d.value;
                });

            d3.selectAll('.table-row .body-cell-value')
                .html(function(d, i) {
                    if(productLinks[i] !== "") {
                        return '<a href="'+('/' + productLinks[i].trim().replace(/^\/?Homepage/i,'')+'.html')
                        .replace(/\.html\.html/ig,'.html').replace(/[\/]{2,}/g,'/') + '">'+d.value+'</a>';
                    } else {
                        return d.value;
                    }
                });

            var newFlagRows = d3.selectAll('.new-flagged').select('.table-row');
            newFlagRows.insert('div',':first-child')
                .attr('class', 'new-label')
                .text(newFlagText);

            _this.setColumnWidthAndBg();
        });

        $(window).resize(function(){
            _this.setColumnWidthAndBg();
        });
    }

    setColumnWidthAndBg() {
        let hybridTableBgHeight = parseInt($('.hybrid-table-container').css('padding-top')) + $('#hybrid-title-band').outerHeight() + $('.hybrid-table thead').outerHeight();
        let tablecolumnEqualWidth =  $('.table-container').width()/$('.hybrid-table thead th').length;

        $('.hybrid-table-bg').height(hybridTableBgHeight);
        $('.hybrid-table thead th').css('width',tablecolumnEqualWidth);
    }
}

document.addEventListener("DOMContentLoaded", function (event) {
    let $hybridTableContainer = $(".hybrid-table-container");
    if ($hybridTableContainer.length !== 0) {
        new hybridTable().initialize();
    }
});
