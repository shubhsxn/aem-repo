$(document).ready(function(){

  let $timeline = $(".con11-timeline");
  if ($timeline.length === 0) {
    return;
  }

  let $modal = $(`<div class="det20-modal-overlay det16-rich-text-field"></div>`),
      $items = $timeline.find(".timelineCard"),
      $scrollToTop = $timeline.find(".c-button"),
      $readMoreElement;

  let clickEventName = "click.timeline-modal";

  $scrollToTop.on("click", function(e){
    e.preventDefault();
    let scrollTop = $timeline.offset().top;
    $("html, body").animate({ scrollTop: scrollTop }, 600, "swing");
  });

  function closeModal() {
    $("body").removeClass("modal-open");
    $modal.removeClass("active");
    $("body").off(clickEventName);
    if($readMoreElement){
      $readMoreElement.find(".read-more").focus();
    }
  }

  function showModal(content) {
    $("body").addClass("modal-open");

    // Populate the modal with this item's content
    $modal.html(content);

    // Create top close button
    let $button = $(`<button class="close-top">${$('.con11-timeline').data('closetext')}</button>`);
    $button.on("click", closeModal);
    $modal.prepend($button);

    // Create Bottom close button
    let $buttonBottom = $(`<button class="close-bottom c-button secondary">${$('.con11-timeline').data('closetext')}</button>`);
    $buttonBottom.on("click", closeModal);
    $modal.append($buttonBottom);

    // Activate the modal
    $modal.addClass("active").scrollTop(0).attr("tabindex",0).focus();

    // Click handler to close if user clicks outside
    setTimeout(function(){
      $("body").on(clickEventName, function(e){
        let inBounds = $(e.target).is(".det20-modal-overlay, .det20-modal-overlay *");
        if (!inBounds) {
          closeModal();
        }
      });
    }, 10);
  }

  // Add modal to document
  $timeline.append($modal);

  // Loop through each timeline item
  $items.each(function(){
    let $item = $(this);

    // Reveal when scrolled into view
    $item.waypoint({
      handler: function(dir) {
        if (dir === "up") { return; }
        $item.addClass("seen");
        this.destroy();
      },
      offset: "50%"
    });

    // Modal popup
    let $modalContent = $item.find(".script");
    if ($modalContent.length > 0) {
      // If there's modal content in this item, set it up
      $item.addClass("clickable").click(function(e){
        e.preventDefault();
        $readMoreElement = $(this);
        showModal($modalContent.html());
      });
    }
  });

  let $jumper = $(".con09-anchor-links-container"),
      $jumperLink = $jumper.find('a');

  if ($jumper.length > 0) {
    $jumper.removeClass("hide");

    let dateRanges = [];

    $jumperLink.each(function(key, val) {
      let linkTitle = $(this).text(),
          linkTitleVal = linkTitle.split('-');

      let obj = {
        text: linkTitle,
        years: {
          from: linkTitleVal[1],
          to: (key === 0) ? new Date().getFullYear() : linkTitleVal[0]
        }
      }
      dateRanges.push(obj);
    });

    let $dates = $items.find(".date");
    let findDateRangeItems = function() {
      // Loop through each prescribed date range
      dateRanges.forEach( (item, i) => {
        // Loop through all of the items to find the first one to match this date range
        $dates.each(function(){
          let thisDate = parseInt(this.innerText);
          if (thisDate >= item.years.from && thisDate <= item.years.to) {
            dateRanges[i].card = this;
            return false // Break
          }
        });
      });
    },

    populateJumperWithLinks = function() {
      let $jumperLinks = $jumper.find("nav");

      dateRanges.forEach( (item, i) => {
        if (!item.card) {
          return;
        }

        let $newLink = $jumperLinks.find('a[title="'+item.text+'"]');

        if (i === 0) {
          $newLink.addClass("active");
        }
        $newLink.on("click", function(e){
          e.preventDefault();

          let scrollTop = $(item.card).offset().top - $jumper.outerHeight() - 40;
          $("html, body").animate({ scrollTop: scrollTop }, 600, "swing");
          $newLink.trigger("activate");
        });

        $newLink.on("activate", function(){
          $jumperLinks.children("a").removeClass("active");
          $(this).addClass("active");
          let offset = $newLink.offset().left + $newLink.outerWidth() + 50;
          if (offset < window.innerWidth) { offset = 0; }
          $jumperLinks.scrollLeft(offset);
        });

        $(item.card).waypoint({
          handler: () => {
            $newLink.trigger("activate");
          },
          offset: "30%"
        });

        dateRanges[i].link = $newLink.get(0);
      });
    }

    findDateRangeItems();
    populateJumperWithLinks();
  }/* End of jumper */

  $(window).scroll(function(){

    if ($(window).scrollTop() < 100){
      $('.global-nav').css('top', 0);
    }
    else if($jumper.offset().top - $(window).scrollTop() < $('.global-nav').height()) {
      $('.global-nav').css('top', "-"+($('.global-nav').height()-($jumper.offset().top - $(window).scrollTop()))+"px");
    }
  })
});
