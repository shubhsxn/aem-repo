$(document).ready(function () {

    if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('productHeader')) {
        return;
    }
    if ($('.hro03-product-header').hasClass('no-panel')) {
        var  spantextHeight = $('.header-text .product-title').children('span').height();
        if (spantextHeight >= 180) {
            let cssTransformY = parseInt(jQuery('.product-logo').css('transform').match(/-?[\d\.]+/g)[5]);
            if($(window).width() >= 768 && $(window).width() <= 992){
                $(".panel").css('minHeight', $(".panel").height()+spantextHeight + 50);
                $('.hro03-product-header .header-text').css('margin-top',0);
                $('.hro03-product-header .header-text .product-category').css('margin-top','45px');
                $(".product-logo").css('transform', 'translateY(' + parseInt(cssTransformY + spantextHeight + 50) + 'px)');
            }
            else{
                $(".panel").css('minHeight', $(".panel").height()+spantextHeight - 50);
                $(".product-logo").css('transform', 'translateY(' + parseInt(cssTransformY + spantextHeight - 50) + 'px)');
            }
        }

        if($(".product-logo img").length === 0) {
            $(".product-logo").addClass('p-without-image');
        }
    }
    else{
        if($(".product-logo img").length === 0) {
            $(".product-logo").addClass('no-product-logo');
        }
    }

    var $headers = $(".hro03-product-header");
    if ($headers.length === 0) {
        return;
    }

    $headers.each(function () {

        var $this = $(this);
        var $logo = $this.find(".product-logo");
        var dialog = $this.find("dialog").get(0);


        if (dialog) {

            // Activate dialog element polyfill on dialog element
            if (typeof dialogPolyfill === "object") {
                dialogPolyfill.registerDialog(dialog);
            }

            // Close dialog element when close button is clicked
            var $closeButton = $this.find("dialog .c-button");
            $closeButton.on("click", function (e) {
                e.preventDefault();
                dialog.close();
            });

            // Open dialog element when logo is clicked
            $logo.on("click", function (e) {
                e.preventDefault();
                dialog.showModal();
            });

        }

    });
    const checkLocalProductData = function () {
        let productId = $('.productSource').data('product-id');

        if (productId) {
            let checkProductDataTimeOut = setInterval(() => {
                let productData = (typeof getProductStorage !== 'undefined' && getProductStorage('productData') !== '') ? JSON.parse(getProductStorage('productData')) : '';

                if (productData) {
                    clearInterval(checkProductDataTimeOut);

                    if (typeof productData['agrian_product'] !== 'undefined' && $('.productSource').data('product-source') === 'agrian') {
                        if ($headers.find('.header-text .product-title').text() === '') {
                            $headers.find('.header-text .product-title').text(productData.agrian_product.product_name);
                        }
                        if (typeof digitalData !== 'undefined' && digitalData.productName === '') {
                            digitalData.productName = productData.agrian_product.product_name;
                        }
                    }
                }
            }, 100);
        }
    }
    checkLocalProductData();

});
