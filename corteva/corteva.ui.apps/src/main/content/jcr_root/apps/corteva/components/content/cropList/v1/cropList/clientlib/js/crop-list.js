$(document).ready(function () {
  if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('cropList')) {
    return;
  }
  var accordionAnimationTime = 220;
  var $accordions = $(".crop-list-accordion");

  if ($accordions.length === 0) {
    return;
  }

  function closeItem($item) {
    var $itemContent = $item.find(".item-content");
    $itemContent.animate({
      height: 0
    }, accordionAnimationTime, "swing", () => {
      $item.removeClass("active");
    });
  }

  function expandItem($item, $accordion) {
    var $itemContent = $item.find(".item-content");
    $item.addClass("active");

    /* Animate the expanding of this item's content area */
    if ($accordion.hasClass("no-collapse") === false) {
      // Set content height to auto to capture its natural height
      $itemContent.css("height", "auto");
      var contentHeight = $itemContent.outerHeight();

      // Set the content height to 0 (should be instant), then animate it back open
      $itemContent.css("height", "0").animate({
        height: contentHeight
      }, accordionAnimationTime, "swing", () => {
        $itemContent.css("height", "auto");
      });
    }
  }

  $accordions.each(function () {
    var $accordion = $(this);
    var $items = $accordion.find(".item");
    var $links = $items.find("h3:not(.hide-accordion)");

    $links.on("click", function (e) {
      e.preventDefault();
      var $item = $(this).parent(".item");
   //   var $itemContent = $item.find(".item-content");
      var isActiveItem = $item.hasClass("active");

      /* If we need to close the other items  */
      if ($accordion.hasClass("multi-open") === false) {
        var $openItem = $items.filter(".active");
        closeItem($openItem);
      }

      /* If item clicked is already active */
      if (isActiveItem) {
        closeItem($item);
      } else {
        /* If the item clicked isn't yet active: */
        expandItem($item, $accordion);
      }
    })
  });

});