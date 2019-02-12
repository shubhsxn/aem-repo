$(document).ready(function() {
  if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('bioDetailCardContainer')){
      return;
  }

  let $bios = $(".con08-biography-filter");

  if ($bios.length < 1) {
    return;
  }

  let $items = $bios.find(".item"),
      $filter = $bios.find('select[name="filter"]');

  $filter.on("change", function() {
    let region = $filter.val();
    if (region === "0") {
      $items.show();
      return;
    }
    $items.hide();
    $('[data-region*="'+region+'"]').show();
  });
});


