$(document).ready(function() {

    let $reg = $(".det28-product-registrations");
    if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('productRegistration')) {
        return;
    }
    if ($reg.length === 0) {
        return;
    }
    if ($('#product-registrations').length) {
        if ($('.productSource').data('product-source') === 'agrian') {
            let checkListStatesTimeOut = setInterval(() => {
                let productData = (typeof getProductStorage !== 'undefined' && getProductStorage('productData') !== '') ? JSON.parse(getProductStorage('productData')) : '';

                if (productData) {
                    clearInterval(checkListStatesTimeOut);
                    listofStates();
                    populateD3Map();
                }
            }, 100);
        } else {
            listofStates();
            populateD3Map();
        }
    }
    //mapview;
    $reg.each(function() {
         let $thisReg = $(this),
         $buttons = $thisReg.find("nav a"),
         $map = $thisReg.find("#map-d3"),
         $list = $thisReg.find(".list");
        // Map button
         $buttons.eq(0).on("click", function(e){
           e.preventDefault();
           $buttons.removeClass("active");
           $(this).addClass("active");
           $map.show();
           $list.hide();
         });
        // List button
         $buttons.eq(1).add($map.children("a")).on("click", function(e){
           e.preventDefault();
           $buttons.removeClass("active");
           $buttons.eq(1).addClass("active");
           $map.hide();
           $list.show();
         });
    });
    $reg.find("#map-d3").show();
    $reg.find(".list").hide();
});


let listofStates = () => {
        let isDatadynamic = $('#checkagrian').data('dynamicstate'),
            listContainer = $('#product-registrations').find('.band-content.list'),
            listData = {},
            parseData = {};
        if (typeof(isDatadynamic) === 'undefined' || isDatadynamic === 'nonagrian') {
            listData = $('#stateData').data('states');
        } else {
            parseData = JSON.parse(sessionStorage.getItem('productData'));
            listData = parseData.agrian_product.licensedState;
        }
        let key;
        let nonAgrainpopulatedList = []

        for (key in listData) {
            if (listData.hasOwnProperty(key)) {
                nonAgrainpopulatedList.push(listData[key]);
            }
        }
        nonAgrainpopulatedList.sort();

        let listTempalte = (nonAgrainpopulatedList) => {
                let template = nonAgrainpopulatedList => `
            <ul>
            ${nonAgrainpopulatedList.map(data =>`
              <li> ${data} </li>
            `).join('')}
            </ul>
            `;
            return template(nonAgrainpopulatedList);
        };
        listContainer.empty().append(listTempalte(nonAgrainpopulatedList));
};

const populateD3Map = () => {
  let lowColor = '#E6E8EA',
      highColor = '#00DC78',
      width = 960,
      height = 500,
      projection,
      countryCode = $('#stateData').data('country').toLowerCase();

  let isDatadynamic = $('#checkagrian').data('dynamicstate');

  if(typeof(isDatadynamic) === 'undefined' || isDatadynamic === 'nonagrian'){

    switch(countryCode){
      case 'br':
      // D3 Projection
      projection=d3.geoMercator()
        .scale(800)
        .center([-54, -15])
        .translate([width / 2, height / 2]);
        break;
      case 'ca':
        //Map projection for canada
        projection = d3.geoMercator()
           .scale(350)
           .center([-135.77330276459485,75.44927205499978]) //projection center
           .translate([width/10,height/10]) //translate to center the map in view
           break;
      default:
        // D3 Projection
        projection = d3.geoAlbersUsa()
          .scale([1000]); // scale things down so see entire US

    }
  }
  else{

    // D3 Projection
    projection = d3.geoAlbersUsa()
      .scale([1000]); // scale things down so see entire US

  }


  var path = d3.geoPath()
      .projection(projection);

  //Create SVG element and append map to the SVG
  var svg;
  switch(countryCode){
    case 'ca':
    svg = d3.select("#map-d3")
     .append("svg")
     .attr("preserveAspectRatio", "xMinYMin meet")
     .attr("viewBox", "50 245 570 250")
     .classed("svg-content-responsive", true);
     break;
     default:
      svg = d3.select("#map-d3")
      .append("svg")
      .attr("preserveAspectRatio", "xMinYMin meet")
      .attr("viewBox", "0 0 900 500")
       .classed("svg-content-responsive", true);
      break;
  }


  var ramp = d3.scaleLinear().domain([0,1]).range([lowColor,highColor])

  let parseData, mapData;
  if (typeof(isDatadynamic) === 'undefined' || isDatadynamic === 'nonagrian') {
      let stateData = $('#stateData').data('states-map');
      mapData = stateData;
  } else {
      parseData = JSON.parse(sessionStorage.getItem('productData'));
      mapData = JSON.parse(parseData.agrian_product.licensedStateMap);
  }


  // Load GeoJSON data and merge with states data
  // Bind the data to the SVG and create one path per GeoJSON feature
  // d3.json(mapData, function(json) {
    if(mapData) {
        svg.selectAll("path")
            .data(mapData.features)
            .enter()
            .append("path")
            .attr("d", path)
            .style("stroke", "#fff")
            .style("stroke-width", "2")
            .style("fill", function (d) {
                return ramp(d.properties.presence)
            });
    }
  //});
}
