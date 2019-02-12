/**
 * Extension to the standard dropdown/select component. It enabled hidding/unhidding of other components based on the
 * selection made in the dropdown/select.
 *
 * How to use:
 *
 * - add the class cq-dialog-custom-dropdown-showhide to the dropdown/select element
 * - add the data attribute cq-dialog-dropdown-showhide-target to the dropdown/select element, value should be the
 *   selector, usually a specific class name, to find all possible target elements that can be shown/hidden.
 * - add the target class to each target component that can be shown/hidden
 * - add the class hidden to each target component to make them initially hidden
 * - add the attribute showhidecustomtargetvalue to each target component, one of the (comma separated)values should equal the value of the select
 *   option that will unhide this element.
 */
(function (document, $) {
    "use strict";

    // when dialog gets injected
    // $(document).on("foundation-contentloaded", function (e) {
    //     // if there is already an inital value make sure the according target element becomes visible
    //     showHideCustomHandler($(".cq-dialog-custom-dropdown-showhide", e.target));
    // });
    
    $(document).on('dialog-ready', function(e){
        if(!checkAuthorFeatureFlag('jsFramework')){
            return;
        }
        showHideCustomHandler($(".cq-dialog-custom-dropdown-showhide", e.target));

        $(document).on("selected", ".cq-dialog-custom-dropdown-showhide", function (e) {
            showHideCustomHandler($(this));
        });
    });
	
	$(document).ready( function(e){
        if(!checkAuthorFeatureFlag('jsFramework')){
            return;
        }
        showHideCustomHandler($(".cq-dialog-custom-dropdown-showhide", e.target));

        $(document).on("selected", ".cq-dialog-custom-dropdown-showhide", function (e) {
            showHideCustomHandler($(this));
        });
    });

    function showHideCustomHandler(el) {
        el.each(function (i, element) {
            if($(element).is("coral-select")) {
                // handle Coral3 base drop-down
                Coral.commons.ready(element, function (component) {
                    showHideCustom(component, element);
                    component.on("change", function () {
                        showHideCustom(component, element);
                    });
                });
            } else {
                // handle Coral2 based drop-down
                var component = $(element).data("select");
                if (component) {
                    showHideCustom(component, element);
                }
            }
        })
    }

    function showHideCustom(component, element) {
        // get the selector to find the target elements. its stored as data-.. attribute
        var target = $(element).data("cqDialogDropdownShowhideTarget");
        var $target = $(target);

        if (target) {
            var value;
            if (component.value) {
                value = component.value;
            } else {
                value = component.getValue();
            }

            // make sure all unselected target elements are hidden.
            $target.not(".hide").addClass("hide");

            // unhide the target element that contains the selected value as data-showhidetargetvalue attribute
            $target.filter("[data-showhidecustomtargetvalue*='" + value + "']").removeClass("hide"); // deprecated
			if($target.find('textarea[name="./shortDescription"]').length){
                if(value == "yes"){
					$target.find('textarea[name="./shortDescription"]').attr('aria-invalid',true).attr('aria-required',true).change();
                }
                else{
					$target.find('textarea[name="./shortDescription"]').attr('aria-invalid',false).attr('aria-required',false).change();
                }
            }
			if($target.find('input[name="./cq:articleType"]').length){
                if(value == "yes"){
					$target.find('input[name="./cq:articleType"]').attr('aria-invalid',true).attr('aria-required',true).change();
                    $target.find('foundation-autocomplete[name="./cq:articleType"]').attr('required',true);
            		$target.find('foundation-autocomplete[name="./cq:articleType"]').find('input').attr('aria-invalid',true).attr('aria-required',true).change();
                }
                else{
					$target.find('input[name="./cq:articleType"]').attr('aria-invalid',false).attr('aria-required',false).change();
                    $target.find('foundation-autocomplete[name="./cq:articleType"]').removeAttr('required');
            		$target.find('foundation-autocomplete[name="./cq:articleType"]').find('input').attr('aria-invalid',false).attr('aria-required',false).change();
                }
            }
			if($target.find('input[name="./displayDate"]').length){
                if(value == "yes"){
                    if($target.find('input[name="./displayDate"]').val() === ''){
						$target.find('input[name="./displayDate"]').attr('aria-invalid',true).attr('aria-required',true).change();
                        $target.find('input[name="./displayDate"]').siblings('input').attr('required',true).attr('invalid',true);
                        $target.find('input[name="./displayDate"]').siblings('input').attr('aria-invalid',true).attr('aria-required',true).change();
                        $target.find('input[name="./displayDate"]').parent('coral-datepicker').attr('required',true).attr('invalid',true);
                        $target.find('input[name="./displayDate"]').parent('coral-datepicker').attr('aria-invalid',true).attr('aria-required',true).change();
                    }
                    else{
                        $target.find('input[name="./displayDate"]').attr('aria-invalid',false).attr('required',true).change();
                        $target.find('input[name="./displayDate"]').siblings('input').removeAttr('invalid').attr('required',true);
                        $target.find('input[name="./displayDate"]').siblings('input').attr('aria-invalid',false).attr('aria-required',true).change();
                        $target.find('input[name="./displayDate"]').parent('coral-datepicker').removeAttr('invalid').attr('required',true);
                        $target.find('input[name="./displayDate"]').parent('coral-datepicker').attr('aria-invalid',false).attr('aria-required',true).change();

                    }
                }
                else{
					$target.find('input[name="./displayDate"]').attr('aria-invalid',false).attr('aria-required',false).change();
                    $target.find('input[name="./displayDate"]').siblings('input').removeAttr('required').removeAttr('invalid');
					$target.find('input[name="./displayDate"]').siblings('input').attr('aria-invalid',false).attr('aria-required',false).change();
                    $target.find('input[name="./displayDate"]').parent('coral-datepicker').removeAttr('required').removeAttr('invalid');
					$target.find('input[name="./displayDate"]').parent('coral-datepicker').attr('aria-invalid',false).attr('aria-required',false).change();
                }
            }
        }
    }
	 $(document).on('click.inpage-verticle','.coral-TabPanel-navigation .coral-TabPanel-tab',function(){
        var verticleArcticlePage = false;

        if($('textarea[name="./shortDescription"]').length || $('input[name="./cq:articleType"]').length || $('input[name="./displayDate"]').length ){
			verticleArcticlePage = true;
        }

        if(verticleArcticlePage) {

            if($('select[name="./vertical-type"]').length && $('select[name="./vertical-type"]').val() == 'no'){
                $('textarea[name="./shortDescription"]').attr('aria-invalid',false).attr('aria-required',false).change();

                $('input[name="./cq:articleType"]').attr('aria-invalid',false).attr('aria-required',false).change();
				$('foundation-autocomplete[name="./cq:articleType"]').removeAttr('required');
            	$('foundation-autocomplete[name="./cq:articleType"]').find('input').attr('aria-invalid',false).attr('aria-required',false).change();

				$('input[name="./displayDate"]').attr('aria-invalid',false).attr('aria-required',false).change();
                $('input[name="./displayDate"]').siblings('input').removeAttr('invalid').removeAttr('required');
                $('input[name="./displayDate"]').siblings('input').attr('aria-invalid',false).attr('aria-required',false).change();
                $('input[name="./displayDate"]').parent('coral-datepicker').removeAttr('invalid').removeAttr('required');
                $('input[name="./displayDate"]').parent('coral-datepicker').attr('aria-invalid',false).attr('aria-required',false).change();
            }
            else{
				$('textarea[name="./shortDescription"]').attr('aria-invalid',true).attr('aria-required',true).change();

                $('input[name="./cq:articleType"]').attr('aria-invalid',true).attr('aria-required',true).change();
				$('foundation-autocomplete[name="./cq:articleType"]').attr('required',true);
            	$('foundation-autocomplete[name="./cq:articleType"]').find('input').attr('aria-invalid',true).attr('aria-required',true).change();

                $('input[name="./displayDate"]').attr('aria-invalid',false).change();
                $('input[name="./displayDate"]').siblings('input').attr('invalid',false);
                $('input[name="./displayDate"]').siblings('input').attr('aria-invalid',false).change();
                $('input[name="./displayDate"]').parent('coral-datepicker').attr('invalid',false);
                $('input[name="./displayDate"]').parent('coral-datepicker').attr('aria-invalid',false).change();
            }

            if($('body').find('input[name="./displayDate"]').length){
                if($('body').find('input[name="./displayDate"]').val() === ''){
					var todayDate = new Date();
                    var month = ["January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"][todayDate.getMonth()];
                    var str = month + ' ' + todayDate.getDate() + ', ' + todayDate.getFullYear() + ' ' + (todayDate.getHours() > 12? todayDate.getHours() - 12 : todayDate.getHours()) + ':' + todayDate.getMinutes() + ' ' + (todayDate.getHours() > 12? 'pm' : 'am');
    
                    $('body').find('coral-datepicker[name="./displayDate"]')[0].valueAsDate = todayDate;
    
                    $('body').find('input[name="./displayDate"]').attr('aria-invalid',false).trigger("change");
                    $('body').find('input[name="./displayDate"]').next('input').attr('aria-invalid',false).trigger("change");
                    $('body').find('input[name="./displayDate"]').next('input').removeAttr('invalid');
                    $('body').find('input[name="./displayDate"]').parent('coral-datepicker').attr('aria-invalid',false).trigger("change");
                    $('body').find('input[name="./displayDate"]').parent('coral-datepicker').removeAttr('invalid');
    
                    $('body').find('input[name="./displayDate"]').parent('coral-datepicker').next('.coral-Form-fielderror').hide();
                }


        	}
        }
    });
})(document, Granite.$);