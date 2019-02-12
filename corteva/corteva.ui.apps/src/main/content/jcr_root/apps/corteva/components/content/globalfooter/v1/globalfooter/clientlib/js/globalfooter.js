$(document).ready(function() {
  if(!checkFeatureFlag('globalfooter')){
    return;
  }

  $('body').find('footer a').attr('data-analytics-type','global-footer');
});