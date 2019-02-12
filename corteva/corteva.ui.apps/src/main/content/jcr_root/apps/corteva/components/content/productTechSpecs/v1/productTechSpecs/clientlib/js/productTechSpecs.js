(function() {
    let player,
        containerID = "card-related-full-width-video-player",
        containerIDS7 = "card-related-full-width-video-player-S7";

    $(document).ready(function() {

        let $videos = $(".det04-card-related-full-width .com04-video");
        if ($videos.length < 1) {
            return; }

        let $playerModal = $(`<div class="det20-modal-overlay" id="${containerID}-container"><button data-analytics-type="video" class="top-close-btn" data-analytics-value="close">${$('.det29-product-technical-specs').data('closetext')}</button><div id="${containerID}"></div><div id="${containerIDS7}"></div></div><div id="${containerID}-curtain"></div>`);
        $('body').append($playerModal);
        let $modal = $(`#${containerID}-container`);
        $modal.find("button").click(function() {
            $modal.removeClass("active");
            player.stopVideo();
        });

        $videos.on("click", function(e) {
            let $thisVideo = $(this);
            let videoID = $thisVideo.data('videoid');
            e.preventDefault();
            if ($thisVideo.closest('.scene7-video-wrapper').length) {
                $('iframe#card-related-full-width-video-player').hide();
                $modal.find($('#' + containerIDS7)).show();
                let $this = $(this),
                    $videoS7 = '<video controls><source src="' + $this.data('videoid') + '"></video>';
                if ($modal.find($('#' + containerIDS7)).find('video').length) {
                    $modal.find($('#' + containerIDS7)).find('video').remove();
                }
                $modal.find($('#' + containerIDS7)).append($videoS7);
                event.stopPropagation();
                $modal.find('video').get(0).play();
            } else {
                $modal.find($('#' + containerIDS7)).hide();
                $('iframe#card-related-full-width-video-player').show();
                player.loadVideoById(videoID);
            }

            $modal.addClass("active");

            let videoTitle = $(this).closest('.details').find('.product-video-title').clone(),
                videoDes = $(this).closest('.details').find('.product-video-description').clone();

            $('.det20-modal-overlay').find('.product-video-title').remove();
            $('.det20-modal-overlay').find('.product-video-description').remove();
            $modal.prepend(videoTitle);
            $modal.append(videoDes);

            $modal.find(".close-bottom").click(function() {
                $modal.removeClass("active");
                player.stopVideo();
            });

        });

        // Click video when you click CTA
        $(".det04-card-related-full-width a.cta.video").on("click", function() {
            $(this).parents(".det04-card-related-full-width").find(".com04-video").trigger("click");
        });

        if ($(document).find('.youtube-video-wrapper-related').length) {
            youtubeWrapper(containerID, containerID, (callbackPlayer) => {
                player = callbackPlayer;
            });
        }
    });
})();
