// Script to set up all interaction with the horizontal jumper
$(document).ready(function() {
  if(typeof checkFeatureFlag !== 'undefined' &&  !checkFeatureFlag('anchorNavigation')){
    return;
  }
  var $jumper = $(".con09-anchor-links-container");

  if ($jumper.length === 0) {
    return; // Don't run this uness there's a jumper on the page
  }
  let $bands ;
  if ($jumper.hasClass("jump-bands")) {
    $bands = $jumper.nextAll(".anchor-band:not(footer)"); // All bands following the jumper
  } else {
    $bands = $jumper.next(".anchor-band:not(footer)");
  }
  var scrolling = false; // Flag used tp prevent smooth window scrolling from being interrupted by horizontal nav scrolling

  // Function to populate the jumper with links to each of the bands
  function createLinks() {
    var $linksContainer = $jumper.find("nav");
    var links = []; // Will hold all of the created links

    $bands.each(function(i) {
      var $band = $(this);

      // Create link dom element
      var $link = $("<a data-analytics-type='in-page-navigation' href='javascript:void()' ></a>");

      // Use the band's data attribute or first heading tag content as the link text
      var heading = $band.data('title') || $band
        .find("h1, h2, h3")
        .first()
        .text()
        .trim();
      heading = String(heading);
      heading = heading.slice(heading.indexOf("_")+1);
      $link.text(heading);

      // Set up the link's click handler.
      $link.click(function(e) {
        e.preventDefault();

        var linksHeight = $jumper.height();
        var bandOffset = $band.offset();

        // Scroll the band into view
        scrolling = true;
        $("html, body").animate({ scrollTop: bandOffset.top - linksHeight + "px" }, 600, "swing");

        // Revert the flag after enough time has passed for the smooth scroll animation
        setTimeout(function() {
          scrolling = false;
        }, 1200);
      });

      // When the link has been activated by scrolling past the band
      $link.on("activate", function() {
        // Do some class housekeeping
        $(links).removeClass("active");
        $link.addClass("active");

        // If there's a scrollbar
        if (
          !scrolling &&
          $linksContainer.get(0).scrollWidth > $linksContainer.outerWidth()
        ) {
          var offset =  $link.get(0).offsetLeft - window.innerWidth * 0.5;
          if (offset < window.innerWidth * 0.5) { offset = 0; }

          $linksContainer.animate(
            { scrollLeft: offset },
            300
          );
        }
      });

      // Save the link to the array
      links.push($link.get(0));

      // Add the links to the document
      $linksContainer.append(links);

      // Create waypoint handlers to  activate the link when you scroll past it (manually)
      // Different offsets are needed for scrolling up and down
      $band.waypoint({
        handler: function(dir) {
          if (dir === "up") { return; }
          $link.trigger("activate");
        },
        // Top of the band is halfway up the page
        offset: "50%"
      });

      // Scrolling up:
      $band.waypoint({
        handler: function(dir) {
          if (dir === "down") { return; }
          $link.trigger("activate");
        },
        // Bottom (not top) of the band is halfway down the page
        offset: function() {
          return -$band.outerHeight() + window.innerHeight * 0.5;
        }
      });
    });

    // Show the jumper now that it's populated
    $jumper.removeClass("hide");
    if(typeof debounce !== 'undefined') {
      let checkAnchorLinkForActive = debounce(function() {
        let scrollDiff = $jumper.offset().top - $(document).scrollTop();
          if(scrollDiff > 400 ){
              $(links).removeClass("active");
          }
        }, 250);
      window.addEventListener('scroll', checkAnchorLinkForActive);
    }
  }

  // For proper position:sticky behavior, all of the bands need to be inside of a parent with the jumper
  function wrapJumper() {
    var $jumperContainer = $('<div class="jumper-container"></div>');

    $jumper.add($bands).wrapAll($jumperContainer);
  }

  function stickJumper(){
    var $container = $(".jumper-container"),
        $body = $('body');

    $container.waypoint({
      handler: function(dir) {
        if (dir === "down") { $body.addClass("stuck"); }
        else { $body.removeClass("stuck"); }
      },
      offset: 0
    });

    $container.waypoint({
      handler: function(dir) {
        if (dir === "down") { $body.removeClass("stuck"); }
        else { $body.addClass("stuck"); }
      },
      offset: function() {
        return -this.element.clientHeight
      }
    });
  }

  // The nav itself, which is also position:sticky, needs to be in a container along with all bands before the jumper
  function wrapNav() {
    var $nav = $(".sticky-header");
    var $addClassParent = $(".jumper-container").closest('.responsivegrid.aem-GridColumn').addClass('jumper-stick-container');
    var $navSiblings = $nav.nextUntil($addClassParent);
    var $container = $('<div class="nav-container"></div>');

    $nav.add($navSiblings).wrapAll($container);
  }

  // Init
  wrapJumper();
  wrapNav();
  stickJumper();
  if ($jumper.hasClass("jump-bands")) {
    createLinks();
  }
});
