$(document).ready(function(){
  if(typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('navHeaderTabs')){
    return;
  }
  let $headerTabs = $(".nav04-header-tabs");

  if ($headerTabs.length === 0) { return; }

  let $nav = $headerTabs.find("nav .band-content");
  let $bands = $headerTabs.nextAll(".band").not("footer");
  let $links;

  // Helper functions
  function activateLink(i) {
    $links.not(`:eq(${i})`).removeClass("active");
    $links.eq(i).addClass("active");
    $bands.not(`:eq(${i})`).removeClass("active");
    $bands.eq(i).addClass("active");
  }

  // Generate links
  let links = [];
  let toClick = 0;
  $bands.each( (i) => {
    let $thisBand = $bands.eq(i);

    let title = $thisBand.data('title') || $thisBand.find("h1, h2, h3, h4, h5").first().text() || `Section ${i}`;
    let id = $thisBand.prop("id");

    // If the band has no ID, make our own using the header text
    if (!id) {
      id = title.trim().toLowerCase().replace(/[^a-z]/gi, "-");
      $thisBand.prop("id", id);
    }
    // IF the ID of this band is in the url hash, we will activate it instead of the first
    if (id === window.location.hash.substr(1)) { toClick = i; }

    let $link = $(`<a href="#${id}" data-analytics-type="in-page-navigation">${title}</a>`);

    // Click handler
    $link.on("click", (e) => {
      e.preventDefault();
      activateLink(i);
      if ("pushState" in window.history) {
        window.history.pushState(false, false, `#${id}`);
      } else {
        window.location.hash = id;
      }
    });
    links.push($link);
  });

  $nav.append(links);
  $links = $nav.children("a");
  activateLink(toClick);
  $bands.addClass("ready");
  $headerTabs.addClass("ready"); // Add a class to indicate all dom changes been finished

  if ("onpopstate" in window) {
    window.onpopstate = function(event){
      var hash = location.hash;
      var i = $(`a[href="${hash}"]`).prevAll("a").length;
      activateLink(i);
    }
  }
});