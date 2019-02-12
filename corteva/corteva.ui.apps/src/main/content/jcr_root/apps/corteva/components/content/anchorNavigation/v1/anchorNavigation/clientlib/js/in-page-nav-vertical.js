$(document).ready(function() {
 if(typeof checkFeatureFlag !== 'undefined' &&  !checkFeatureFlag('anchorNavigation')){
    return;
  }

  let $navs = $(".nav05-in-page-nav-vertical"),
    $document = $("html, body"),
    scrollFlag = false;

  if ($navs.length < 1) {
    return;
  }
  // Loop through each nav on the page.
  $navs.each(function() {
    let $nav = $(this),
      $sections = $nav.nextAll("section"),
      $dropDown = $nav.find("select"),
      scrolling = false,
      links = [],
      options = [],
      $links;

    // Set up functions to activate and reveal (scroll into view) links to try to be more optimized
    function activateLink(i) {
      // Add/remove .active on the sidebar links
      $links.not(`:eq(${i})`).removeClass("active");
      $links.eq(i).addClass("active");

      // Update the selection in the dropdown as well
      if ($dropDown.children("option[value=" + i + "]").length > 0) {
        $dropDown.val(i).trigger("change.select2");
      }
    }
    function revealLink(i, dir) {
      let navHeight = $nav.height(),
          $link = $links.eq(i),
          linkPos = $link.position().top,
          scrollPoint;

      if ($link.length === 0) { return }

      if (linkPos > navHeight / 2) {
        if (linkPos > navHeight && dir === undefined) {
          scrollPoint = navHeight;
        } else {
          if (scrollFlag === false) {
            scrollPoint = linkPos - 30;
          }
        }
      } else if (linkPos < navHeight && dir === 'up') {
        scrollPoint = linkPos - 50;
        scrollFlag = true;
      }
      $nav.animate({ scrollTop: scrollPoint }, 60);
    }
    function goToSection(i) {
      scrolling = true;
      let timeout = 1200,
        $goSection = $sections.eq(i),
        id = $goSection.prop("id"),
        scrollTop = $goSection.offset().top - (window.innerHeight * .28);

      $document.animate({ scrollTop:  scrollTop }, timeout, ()=> { scrolling = false });

      if ("pushState" in window.history) {
        window.history.pushState(false, false, `#${id}`);
      } else {
        window.location.hash = id;
      }
    }

    // Build DOM scaffolding around the nav element and sections
    $nav.add($sections).wrapAll(`<div class="inner-nav-wrapper"></div>`);
    $sections.wrapAll(`<div class="sections"></div>`);
    //$wrapper = $nav.parent();
    $nav.wrap(`<div class="nav-wrapper"></div>`);
    //$navWrapper = $nav.parent();

    // Loop through each section that follows the navigator
    $sections.each(function(i) {
        let $section = $(this),
            $thisLink = $(`<a data-analytics-type="in-page-navigation" href="#"></a>`),
            $header = $section.find("h1,h2,h3,h4,h5,h6").first(),
            $thisOption,
            linkText,
            id;

        // If no header, use the first few words for link text
        if ($header.length > 0) {
          linkText = $header.text();
        } else {
          linkText = $section.text().substr(0, 25);
        }

        // Populate link's text
        $thisLink.text(linkText);

        // Generate an ID for this section if it doesn't have one
        if (!$section.prop("id")) {
          id = linkText.trim().toLowerCase().replace(/[^a-z]/gi, "-") + '-' + i;
          $section.prop("id", id);
        } else {
          id = $section.prop("id");
        }

        // For accessibility sake, make the link really link to the section
        $thisLink.prop("href", `#${id}`);

        // Override the link's click event to do smooth scrolling
        $thisLink.on("click", function(e) {
          e.preventDefault();

          // Go to the section
          goToSection(i);

          // Add active class to link
          activateLink(i);

          // Scroll to link
          revealLink(i);
        });

        // Save the link
        links.push($thisLink);

        // Add scroll listener to activate links when scrolling past sections
        if ("waypoint" in $.fn) {
          // Scrolling down:
          $section.waypoint({
            handler: dir => {
              if (scrolling) { return; }
              if (dir === "up") { return; }
              activateLink(i);
              revealLink(i, dir);
            },
            offset: "50%"
          });

          // Scrolling up:
          $section.waypoint({
            handler: dir => {
              if (scrolling) { return; }
              if (dir === "down") { return; }
              activateLink(i);
              revealLink(i, dir);
            },
            offset: function() {
              return -$section.outerHeight() + window.innerHeight * 0.5;
            }
          });
        }

        // Populate the dropdown
        if ($dropDown.length > 0) {
          $thisOption = $(`<option value="${i}">${linkText}</option>`);
          options.push($thisOption);
        }
      });

  // Add links to the DOM
  $nav.append(links);
  $links = $nav.children("a");

  let $stickyNav = $(".sticky-header, .global-nav"),
    verticalOffset = ($stickyNav.length > 0 && getComputedStyle($stickyNav[0]).position.match(/sticky|fixed/))? 170 : 170; // Add to the offset if the global nav is stuck

  if (typeof stickybits !== "undefined") {
    stickybits(".nav05-in-page-nav-vertical", { stickyBitStickyOffset: verticalOffset } );
  }

    // Add options to dropdown
    if ($dropDown.length > 0) {
      //$dropDown.append(options) this is changed because options are authorable.
      $dropDown.val(-1);

      $dropDown
        .trigger("change.select2")
        .on("change", () => {
          var i = $dropDown.val();

          if (i < 0) { return; }

          // Go to the section
          goToSection(i);

          // Add active class to link
          activateLink(i);
        });
    }
  });
});
