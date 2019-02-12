$(document).ready(function(){
  if(typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('galleryImage')){
    return;
  }

  var $galleries = $(".str03-gallery-image");

  if ($galleries.length < 1) {
    return;
  }

  var maxImages = 99;

  $galleries.each(function(){

    var $gallery = $(this);

    var $next = $gallery.find(".gallery-next");
    var $prev = $gallery.find(".gallery-prev");

    var $imageContainer = $gallery.find(".images");
    var $images = $imageContainer.children();

    $images.each(function(){
      //var text = $(this).find("img").prop("alt"); set data attr for image caption/label
      var text = $(this).find("img").data("label");
      var $thisImage = $(this);

      $thisImage.wrap(`<div class="item" data-total="${$images.length}"></div>`);

      $thisImage.after(`<p>${text}</p>`);
    });
    $images = $imageContainer.children();

    var activeImage = 1;
    var totalImages = $images.length;
    if (totalImages < maxImages) { maxImages = totalImages; }

    $next.on("click", function(){
      activeImage++;

      if (activeImage > maxImages) { activeImage = 1; }

      setClasses();
       $(this).attr('data-analytics-value',$('.str03-gallery-image .atm01-header-v3__wrapper h2').text().trim()+':'+activeImage)
    });

    $prev.on("click", function(){
      activeImage--;

      if (activeImage < 1) { activeImage = maxImages; }

      setClasses();
      $(this).attr('data-analytics-value',$('.str03-gallery-image .atm01-header-v3__wrapper h2').text().trim()+':'+activeImage)
    });

    function setClasses() {
      var removeClasses = ["start", "end"];
      for (var i=1;i<=maxImages;i++) {
        if (i === activeImage) { continue; }
        removeClasses.push(`active-${i}`);
      }
      $imageContainer.removeClass(removeClasses.join(" "));
      $imageContainer.addClass(`active-${activeImage}`);

      if (activeImage === 1) { $imageContainer.addClass("start"); }
      if (activeImage === maxImages) { $imageContainer.addClass("end"); }

      $images.removeClass("active");
      $images.eq(activeImage - 1).addClass("active");

    }

    setClasses();

  });


});
