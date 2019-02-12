$(document).ready(function() {
  var $compares = $(".str04-compare-image");

  if ($compares.length < 1) {
    return;
  }

  // Loop through each compare, as there could be many on the page
  $compares.each(function() {
    var $compare = $(this);
    var $lis = $compare.find("li");
    var $imgs = $compare.find("img");
    var $first = $lis.first();

    // Figure out the dimensions of the image for sizing
    var imgRatio = 1;
    var imgHeight = function(width) {
      return width / imgRatio;
    };
    var imgSrc = $imgs.first().prop("src");
    var monitorImage = new Image();
    monitorImage.src = imgSrc;
    monitorImage.onload = function() {
      imgRatio = monitorImage.width / monitorImage.height;
      setSizes();
    };

    // Helper function to set the sizes of everything based on the viewport and the ratio of the image
    function setSizes() {
      var width = $compare.outerWidth();
      $lis.css("background-size", `${width}px auto`);
      $compare.height(imgHeight(width));
    }

    // Set the comparison slider to the correct percent when receiving a click/tap event
    function setPercent(e) {
      var width = $compare.outerWidth();
      var x = e.offsetX || e.originalEvent.layerX || e.originalEvent.touches[0].pageX;

      // Handle if the event came from outside the boundary of the image
      if (x >= width) {
        x = width;
        isClicked = false;
      } else if (x < 0) {
        x = 0;
        isClicked = false;
      }

      var clickPercent = x / width * 100;
      $first.css("width", `${clickPercent}%`);
      setSizes();
    }

    // Set the image as a background for performance
    $lis.each(function(i) {
      var $thisLi = $(this);
      $thisLi.css("backgroundImage", `url(${$imgs[i].src})`);
    });

    // Set up event handlers
    $compare.on("touchmove", function(e) {
      e.preventDefault();
    });


    // First, monitor click starts/ends for simple drag support
    var isClicked = false;
    $compare.on("touchstart mousedown", function(e) {
      isClicked = true;
      setPercent(e);
    });
    $compare.on("touchend mouseup", function(e) {
      isClicked = false;
    });
    // If you're moving finger/cursor, only do anything if you're clicking
    $compare.on("touchmove mousemove", function(e) {
      if (isClicked) {
        setPercent(e);
      }
    });

    // Redo the sizes whenever the view changes
    $(window).on("resize", setSizes);
    // Set sizes the first time
    setSizes();
    // Add a helper class on the element to signify JS is set up
    $compare.addClass("ready");
  });
});
