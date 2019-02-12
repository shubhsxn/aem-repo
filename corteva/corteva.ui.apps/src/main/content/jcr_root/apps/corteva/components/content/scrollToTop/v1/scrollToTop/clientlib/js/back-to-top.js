$(document).ready(function(){
  var $backToTops = $("a.back-to-top"),
    ua = navigator.userAgent.toLowerCase(),
    isMobile = ua.indexOf('mobile') > -1;

  if ($backToTops.length === 0 || isMobile) { return }

  // USe stickybits position: sticky replacement if installed
  // if (typeof stickybits !== "undefined") {
    // stickybits("a.back-to-top", { verticalPosition: 'bottom', stickyBitStickyOffset: 15 } );
  //}

  // Create click handlers for each BBT button on the page
  $backToTops.each(function(){

    var $this = $(this);
    var $parent = $this.parents(".band, body").first();
    var parentTop = $parent.offset().top;
    $(window).on('scroll', function(){
      if(this.pageYOffset > 100){
        $this.css('opacity', 1);
      } else{
        $this.css('opacity', 0);
      }
    });
    var verticalOffset = ($(".global-nav").length > 0 && getComputedStyle($(".global-nav")[0]).position.match(/sticky/))? 160 : 10; // Add to the offset if the global nav is stuck
    parentTop -= verticalOffset;

    $this.click(function(e){
      e.preventDefault();
      $("body,html").animate({ scrollTop: parentTop }, 900);
    });
  });
});