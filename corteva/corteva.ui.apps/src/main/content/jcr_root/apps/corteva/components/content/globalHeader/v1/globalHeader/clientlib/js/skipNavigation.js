const skipNavigation = function() {
    /**
     * Initialization function
    */
    let $skipNavLink = $('.skip-nav'),
        $headerElm = $skipNavLink.parents('.experiencefragment');

    function initialize() {
        skipNavigationLink();
    }

    let skipNavigationLink = function() {
        $skipNavLink.on("click", function(e) {
            e.preventDefault();
            $(window).scrollTop(0);
            $headerElm.next('div').find('a:first').focus();
        });
    }
    return initialize();
}

skipNavigation();