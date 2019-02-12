$(document).ready(function() {
    if (!checkFeatureFlag('globalHeader')) {
        return;
    }
    var $root = $(":root");
    var $nav = $(".global-nav");
    var $main = $nav.children(".main");
    var $hamburger = $main.children("button");

    if ($nav.length) {
        $nav.closest('.feature-flag').addClass('sticky-header');
    }

    var $stickyHdr = $(".sticky-header");

    // Hamburger button
    $hamburger.on("click touchstart", function(e) {
        e.preventDefault();
        $(window).scrollTop(0);
        setTimeout(function() {
            $root.toggleClass("menu-open");
            $nav.toggleClass("active");
            $stickyHdr.toggleClass("active");
            $main.find("li.active").removeClass('active');
            $main.toggleClass('active');
        }, 10);
    })

    $(document).find('nav.main .subnav').find('a').each(function() {
        $(this).attr('data-analytics-value',
            $(this).closest('.subnav').closest('li').children('a').text().trim());
    });

    function enableLinkFunctionality(){
        if(window.innerWidth <= 768){

          $main.find("li div").find("nav").on("click", function(e) {
            $(this).parents('li').toggleClass("active");

            $("nav li.active > a:first").on("click", function(e) {
                if($(this).css('position') === 'absolute'){
                  $(this).parents('li').removeClass("active");
                  e.preventDefault();
                }
            });
        });
      }

      // Click handler to close mobile nav if you click outside of it
      $(document).on('click touchstart', 'html.menu-open', function(e) {
          if ($.contains($nav.get(0), e.target) === true) {
              return;
          }

          $root.removeClass("menu-open");
          $nav.removeClass("active").find(".active").removeClass('active');
          $stickyHdr.removeClass("active");
      });
    }
    // Link toggles for mobile subnavs
    enableLinkFunctionality();

    //keep logic in place if window is rezised
    $(window).resize(function(){
        if ((window.innerWidth >= 768) && (window.innerWidth <= 960)) {
            enableLinkFunctionality();
        }
    });

});
