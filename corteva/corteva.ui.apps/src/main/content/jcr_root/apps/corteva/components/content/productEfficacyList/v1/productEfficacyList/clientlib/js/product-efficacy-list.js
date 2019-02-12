$(document).ready(function () {
  if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('productEfficacyList')) {
    return;
  }
  var $lists = $(".det27-product-efficacy-list");
  if ($lists.length === 0) {
    return;
  }
  $lists.each(function () {
    var $this = $(this);
    var dialog = $this.find("dialog").get(0);
    var $imageLinks = $this.find(".items-controlled li a");
    if (dialog) {
      // Activate dialog element polyfill on dialog element
      if (typeof dialogPolyfill === "object") {
        dialogPolyfill.registerDialog(dialog);
      }

      // Close dialog element when close button is clicked
      var $closeButton = $this.find("dialog .c-button");
      $closeButton.on("click", function (e) {
        e.preventDefault();
        dialog.close();
      });

      // Open dialog element when links are clicked
      $imageLinks.on("click", function (e) {
        e.preventDefault();
        var $thisLink = $(this);
        var href = $thisLink.prop("href");
        var title = $thisLink.find('span').text();
        var altText = $thisLink.data("alt");
        $(dialog).find("img").prop("src", href);
        $(dialog).find("img").prop("alt", altText);
        $(dialog).find(".modal-heading").html(title);
        dialog.showModal();
      });
    }
  });
});
