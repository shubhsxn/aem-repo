$(document).ready(function() {
    if(!checkFeatureFlag('cardsContainer')) {
        return;
    }
    $(".smart-tile-grid > .aem-Grid > .experienceFragments").on("click", function() {
      let $this = $(this),
        $sibs = $this.siblings(),
        $active = $sibs.filter(".active");

      $active.removeClass("active");
      $this.toggleClass("active");
    });
});