$(document).ready(function () {
  if(!checkFeatureFlag('heroCarousel')) {
      return;
  }

  if ($('.carousel-hero').length < 1) {
    return;
  }

  var $carousel = $('.carousel-hero').flickity({
    prevNextButtons: false,
    pageDots: false,
    autoPlay: 5000,
    pauseAutoPlayOnHover: false
  });

  var flkty = $carousel.data('flickity');

  $(".c-hro3__wrapper").hover(function () {
    flkty.stopPlayer();
  }, function () {
    flkty.playPlayer();
  });

  var $cellButtonGroup = $('.button-group--cells');
  var $cellButtons = $cellButtonGroup.find('.button');

  $carousel.on('select.flickity', function () {
    $cellButtons.filter('.is-selected')
      .removeClass('is-selected');
    $cellButtons.eq(flkty.selectedIndex)
      .addClass('is-selected');
  });

  $cellButtonGroup.on('click', '.button', function () {
    var index = $(this).index();
    $carousel.flickity('select', index);
  });

});