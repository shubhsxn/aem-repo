$(document).ready(function(){
    var $lightNav = $(".gbl05-light-navigation");
    if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('lightNavigation')) {
        return;
    }
    if ($lightNav.length === 0) {
      return;
    }
    var $navElem = $lightNav.find("nav");
    $lightNav.find(".header-text").on("click", function() {
      $(this).toggleClass("active");
      $navElem.toggleClass("active");
    });
  });