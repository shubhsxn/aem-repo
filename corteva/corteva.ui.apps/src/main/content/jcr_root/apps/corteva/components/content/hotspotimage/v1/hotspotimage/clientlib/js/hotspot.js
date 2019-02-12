$(document).ready(function(){
  var $hotspots = $(".det30-hotspot-image");

  if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('hotspotimage')){
      return;
  }
  if ($hotspots.length === 0) {
    return;
  }

  $hotspots.each(function(){
    var $this = $(this);
    var $clickAreas = $this.find("area");
    var $modals = $this.find(".modal");
    var $closeButtons = $modals.find(".c-button");

    $closeButtons.on("click", function(e){
      e.preventDefault();
      $modals.removeClass("active");
    }).parent("a").attr("href", "#");

    $clickAreas.on("click", function(e){
      e.preventDefault();
      var targetModal = $(this).attr("href");
      $modals.filter(targetModal).addClass("active");
    });

  });

  $('body').click(function (event)
{
   if(!$(event.target).closest('.det30-hotspot-image').length && !$(event.target).is('.det30-hotspot-image')) {
     $(".modal").removeClass('active');
   }
});

});
