const productLabelModule = function() {
    if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('productLabel')) {
        return;
    }

    var $productLabel = $(".det34-product-labels");
    if ($productLabel.length === 0) {
        return;
    }

    const checkLocalProductData = function() {
        let productId = $('.productSource').data('product-id');

        if (productId) {
            let checkProductDataTimeOut = setInterval(() => {
                let productData = (typeof getProductStorage !== 'undefined' && getProductStorage('productData') !== '') ? JSON.parse(getProductStorage('productData')) : '';

                if (productData) {
                    clearInterval(checkProductDataTimeOut);

                    if (typeof productData['agrian_product'] !== 'undefined' && $('.productSource').data('product-source') === 'agrian') {
                        let labelDownloadHref = '',
                            labelDownloadLink = '',
                            $productLabelEle = $productLabel.find('.product-label'),
                            safetyDataSheetHref = '',
                            safetyDataSheetLink = '',
                            $productSafetyEle = $productLabel.find('.safety-data-sheet'),
                            $productSupplementEle = $productLabel.find('.supplemental-label'),
                            productName = productData.agrian_product.product_name;

                        productData.agrian_product.documents.document.filter((item) => {
                            if (item.type === 'Label') {
                                labelDownloadLink = item.filename;
                            }
                            if (item.type === 'MSDS') {
                                safetyDataSheetLink = item.filename;
                            }
                        });

                        labelDownloadHref = $productLabelEle.attr('href');
                        $productLabelEle.attr('href', labelDownloadHref + labelDownloadLink);
                        if($productLabelEle.attr('data-analytics-value') === ''){
                            $productLabelEle.attr('data-analytics-value', productName);
                        }

                        safetyDataSheetHref = $productSafetyEle.attr('href');
                        $productSafetyEle.attr('href', safetyDataSheetHref + safetyDataSheetLink);
                        if($productSafetyEle.attr('data-analytics-value') === ''){
                            $productSafetyEle.attr('data-analytics-value', productName);
                        }
                        if($productSupplementEle.attr('data-analytics-value') === ''){
                            $productSupplementEle.attr('data-analytics-value', productName);
                        }
                    }
                }

            }, 100);
        }
    }
    checkLocalProductData();
}
$(document).ready(function() {
    productLabelModule();
});
