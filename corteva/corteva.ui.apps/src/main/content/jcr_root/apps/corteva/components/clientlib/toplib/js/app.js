/**
 * * checkFeatureFlag
 * for the feature flag implementation on AEM
 * */

function checkFeatureFlag(componentName) {
    var execFurtherCode = true;

    $('.feature-flag').each(function () {
        var $this = $(this);
        if ($this.data('componentname') === componentName) {
            if ($this.data('featureflagvalue') === 'off') {
                execFurtherCode = false;
            }
            return false;
        }
    });
    return execFurtherCode;
}

/**
 * * debounce
 * added for scrolling
 * */

function debounce(func, wait, immediate) {
    return function () {
        var context = this,
            args = arguments,
            later,
            timeout,
            callNow;

        later = function () {
            timeout = null;
            if (!immediate) {
                func.apply(context, args);
            }
        }

        callNow = immediate && !timeout;

        clearTimeout(timeout);

        timeout = setTimeout(later, wait);
        if (callNow) {
            func.apply(context, args);
        }
    };
}

/**
 * * formChosenDropdown
 * initialize chosen dropdown
 * */

function formChosenDropdown() {
    var $dropdowns = $("select.form-dropdown, select.form-dropdown-filter");

    //if ($dropdowns.length) {
    //    // Setup handler to add .focus-within to chosen container when tabbed into it (for accessibility)
    //    $dropdowns.on("chosen:ready", function (e) {
    //        var $this = $(this),
    //            $chosen = $this.next(".chosen-container"),
    //            $input = $chosen.find("input");
//
    //        $input.on("focus", function (e) {
    //            $chosen.addClass("focus-within");
    //        }).on("blur", function (e) {
    //            $chosen.removeClass("focus-within");
    //        });
    //    });
        function optionsClassCopy(data, container) {
            if (data.element && $(data.element).attr("class")) {
                $(container).addClass($(data.element).attr("class"));
            }
            return data.text;
        }
        $dropdowns.select2({
            width: "100%",
            minimumResultsForSearch: Infinity,
            templateResult: optionsClassCopy,
            templateSelection: optionsClassCopy
        });

        // When the parent form of a dropdown is cleared (native form.reset event), reset the chosens
        //$dropdowns.parents("form").each(function(){
        //    var $this = $(this);
        //    $this.on("reset", function(e){
        //      setTimeout(function(){
        //        $this.find("select").trigger('chosen:updated');
        //      }, 30);
        //    });
        //});
    //} else {
    //    return;
    //}
}

window.checkFeatureFlag = function (componentName) {
    return checkFeatureFlag(componentName);
}

window.debounce = function (func, wait, immediate) {
    return debounce(func, wait, immediate);
}

/**
 * * getProductdata
 * get product data and store in sessionstorage
 * */
const getProductdata = function () {
    let $productSource = $('.productSource'),
        productServeletApi = $productSource.data('product-source-api'),
        productSource = $productSource.data('product-source'),
        productId = $productSource.data('product-id') || '',
        productServeletUrl = productServeletApi,
        productData = getProductStorage('productData');

    if (productData!== null && JSON.parse(productData).hasOwnProperty('agrian_product') && JSON.parse(productData)['agrian_product'].product_id !== productId.toString()) {
        deleteProductStorage();
    }
    //getProductStorage called  again to update productData
    productData = getProductStorage('productData');
    if (productSource === 'agrian' && !productData && productId !== '') {
        $.ajax({
                method: 'GET',
                url: productServeletUrl,
                dataType: 'json'
            })
            .done(function (responseData) {
                setProductStorage(responseData);
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                console.log('There is an error with service: ' + errorThrown);
            })
            .always(function () {

            });
    }
}

/**
 * * setProductStorage
 * set product data to sessionstorage
 * */
const setProductStorage = function (productData) {
    let modifiedProductData = JSON.stringify(productData);

    sessionStorage.setItem('productData', modifiedProductData);
}

/**
 * * getProductStorage
 * get product data from sessionstorage
 * */
window.getProductStorage = function () {
    return sessionStorage.getItem('productData');
}

/**
 * * deleteProductStorage
 * delete product data from sesisonstorage
 * */
const deleteProductStorage = function () {
    sessionStorage.removeItem('productData');
}

window.youtubeWrapper =  function (containerID,videoFrameID,callbackFn) {
    if($('script[src="https://www.youtube.com/player_api"]').length <= 0){
        var tag = document.createElement('script');
        tag.src = "https://www.youtube.com/player_api";
        var firstScriptTag = document.getElementsByTagName('script')[0];
        firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
    }

    if (typeof window.onYouTubeIframeAPIReady === 'undefined') {
        window.onYouTubeIframeAPIReady = onYouTubeIframeAPIReady;
    } else {
        let youTubeApiTimeOut = setInterval(() => {
          if (typeof(YT) !== 'undefined' && typeof(YT.Player) !== 'undefined') {
              clearInterval(youTubeApiTimeOut);
              onYouTubeIframeAPIReady();
          }
        }, 500);
    }

    function onYouTubeIframeAPIReady() {
        let youtubeContainer = document.getElementById(containerID);
        if (!youtubeContainer) { return; }
        let youtubeVideoID = youtubeContainer.dataset.videoid;

        let youtubePlayer = new YT.Player(videoFrameID, {
            height: '390',
            width: '640',
            videoId: youtubeVideoID,
            playerVars: {
              rel: 0,
              controls: 0,
              showinfo: 0
            },
            events: {
              'onReady': () => {
                youtubeContainer.classList.add("ready")
              },
              'onStateChange': (payload) => {
                let playStatus = payload.data;
                $(youtubeContainer).removeClass("playing paused ended");
                switch (playStatus) {
                  case YT.PlayerState.PLAYING:
                    youtubeContainer.classList.add("playing");
                    break;
                  case YT.PlayerState.PAUSED:
                    youtubeContainer.classList.add("paused");
                    break;
                  case YT.PlayerState.ENDED:
                    youtubeContainer.classList.add("ended");
                    break;
                }
              }
            }
        });
        callbackFn(youtubePlayer);
    }
    //return player;
}

const cardsContainerHeight = function(){
    let getcardsContainer = $(".cardsContainer .det02-card-primary-content"),
        maxHeight = 0;
        getcardsContainer.each(function(){
        if ($(this).outerHeight() > maxHeight) { maxHeight = $(this).outerHeight(); }
     });
     getcardsContainer.css("height", maxHeight+65);
};


$(document).ready(function () {
    formChosenDropdown();
    getProductdata();
    cardsContainerHeight();
});
