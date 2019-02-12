$(document).ready(function(){
  if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('navigationDropdown')) {
      return;
  }

  // Get all submenus
  var $subMenus = $(".global-nav .main .subnav");
  if ($subMenus.length === 0) {
    return;
  }

  $subMenus.each(function(){
    var $thisMenu = $(this);
    var $thisMenuLink = $thisMenu.parents("li").children("a");
    var $links = $thisMenu.find("a");

    $links.on("focus", function(){
      $thisMenuLink.addClass("active");
    });

    $links.on("blur", function(){
      $thisMenuLink.removeClass("active");
    });
  });
});