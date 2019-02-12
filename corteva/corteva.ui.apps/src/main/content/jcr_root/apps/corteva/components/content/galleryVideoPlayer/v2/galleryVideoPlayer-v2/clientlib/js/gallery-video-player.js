 (function() {
    let player,
        //containerID = 'video-container',
        videoFrameID = 'video-player';
    $(document).load(function() {
        if ($(document).find('.youtube-video-wrapper-gallery').length) {
          $('.youtube-video-container').each(function(){
            let $this = $(this);
            videoFrameID = $this.attr('data-videoID')+'-video-player';

            youtubeWrapper($this.attr('id'), videoFrameID, (callbackPlayer) => {
              player = callbackPlayer;
            });
          });
        }
    });

   $(document).load(function() {
        if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('galleryVideoPlayer')) {
            return;
        }

        var $scene7FeaturedVideo = $('.scene7-video-container').find('video');
        if ($scene7FeaturedVideo.length) {

            let ua = navigator.userAgent.toLowerCase(),
                isIphone = ua.indexOf("iphone") > -1,
                eventName = 'click.scene7';

                if(isIphone){
                    eventName = 'touchstart';
                }

            $scene7FeaturedVideo.on(eventName, function(event) {
                event.stopPropagation();
                var $this = $(this);
                if ($this.get(0).paused) {
                    $this.get(0).play();
                    $this.closest('.scene7-video-container').addClass('playing');
                } else {
                    $this.get(0).pause();
                    $this.closest('.scene7-video-container').removeClass('playing');
                }
            });
        }
        var $videos = $(".str02-gallery-video-player .more-videos a"),
            $desc = $(".str02-gallery-video-player p.description"),
            $title = $(".str02-gallery-video-player h2:first");

        if ($desc.height() > 30) {
            $desc.removeClass('no-content');
        }

        $desc.on("click", function() {
            if (!$(this).is('.no-content')) {
                $desc.toggleClass("active");
            }
        });

        if ($videos.length < 1) {
            return; }

        $videos.each(function() {
            var $thisVideo = $(this);
            //var videoID = $thisVideo.data('videoid');

            // Click event to play the video
            $thisVideo.on("click", function(e) {
                e.preventDefault();

                var title = $thisVideo.find(".title").text();
                var desc = $thisVideo.find(".desc").html();
                var cVideo = $(this).attr('data-videoid');
                $desc.html(desc);
                $title.text(title);
                if(player.loadVideoById){
                player.loadVideoById(cVideo);
                }
                else{
                    if(document.location.hash){

                        var hashId = document.location.hash;    
                        $(".galleryvideoitem").each(function(){
                            var firstFlag = false;
                            var hashString = "#"+($(this).data("id")+1)
                            if(hashString == hashId){
                				var datasrc = $(this).attr("id");
                                $("youtube-video-container iframe").attr("src",datasrc);
                            }

                        });
                
                    }


                }
                $videos.removeClass("active");
                $thisVideo.addClass("active");
            });
        });
        var $s7Videos = $(".scene7-video-wrapper .more-videos a");
        if ($s7Videos.length < 1) {
            return; }

        $s7Videos.each(function() {
            var $thisVideo = $(this),
                s7VideoID = $thisVideo.data('videourl'),
                $descS7 = $(".scene7-video-wrapper p.description"),
                $titleS7 = $(".scene7-video-wrapper h2:first");

            // Click event to play the video
            $thisVideo.off('click').on("click", function(e) {
                e.preventDefault();

                var titleS7 = $thisVideo.find(".title").text();
                var descS7 = $thisVideo.find(".desc").html();
                $scene7FeaturedVideo.attr('poster', $thisVideo.find('img').attr('src'));

                $descS7.html(descS7);
                $titleS7.text(titleS7);

                $scene7FeaturedVideo.find('source').attr('src', s7VideoID);
                $scene7FeaturedVideo.load();
                $scene7FeaturedVideo.get(0).play();
                $scene7FeaturedVideo.closest('.scene7-video-container').addClass('playing');

                $s7Videos.removeClass("active");
                $thisVideo.addClass("active");

            });
        });

    });
})();