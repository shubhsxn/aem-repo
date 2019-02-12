$(document).ready(function() {
  if(!checkFeatureFlag('fixedGridTilesContainer')){
    return;
  }

  	var	$tileImpactElement = $('.impact').find('.det05-tile-impact');

  	if($tileImpactElement.length){
  		$tileImpactElement.each(function(){
	  		var $this = $(this),
	  		tileImpactClass = $this.attr('class');

	  		$this.closest('.experienceFragments').addClass(tileImpactClass);
	  		$this.removeClass(tileImpactClass);
	  	});
  	}

});