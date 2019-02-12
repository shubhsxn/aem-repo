function checkAuthorFeatureFlag(componentName){
	var execFurtherCode = true;
	if($('#ContentFrame').length > 0){
		$this = $($('#ContentFrame').contents()).find('#jsFrameworkFeatureFlag')
		if($this.data('componentname') === componentName){
			if($this.data('featureflagvalue') === 'off'){
				execFurtherCode = false;
			}
		}
	}
	return execFurtherCode;
}
function checkFeatureFlag(componentName){
	var execFurtherCode = true;

	$('.feature-flag').each(function(){
		var $this = $(this);
		if($this.data('componentname') === componentName){
			if($this.data('featureflagvalue') === 'off'){
				execFurtherCode = false;
			}
			return false;
		}
	});
	return execFurtherCode;
}
$(document).ready(function() {
    $('body').on('keyup','input[name="./ranking"]',function () { 
        this.value = this.value.replace(/[^0-9\.]/g,'');
    });
});