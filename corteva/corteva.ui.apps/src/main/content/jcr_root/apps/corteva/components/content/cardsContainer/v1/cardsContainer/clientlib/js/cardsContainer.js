$(document).ready(function() {
    if(!checkFeatureFlag('cardsContainer')) {
        return;
    }
    $(".con02-cards-3-column .cards").on("scroll.nudge", function() {
        $(this).addClass("scrolled");
        $(this).off("scroll.nudge");
    });
});