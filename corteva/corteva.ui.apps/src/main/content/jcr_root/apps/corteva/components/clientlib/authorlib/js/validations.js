var validateRequired = function() {
        var $form = $("form.foundation-form"),
            fieldClass = '.isMandatory',
            //$hiddenField = $form.find("input[type=hidden].isMandatory"),
            $requiredField = $form.find(fieldClass).not('.coral-NumberInput'),
            isValidate = false;

        $requiredField.each(function() {
            var $this = $(this),
                fieldVal,
                $fieldElm,
                $inputElm = $this;

            if (!$this.attr('aria-required') && $this.get(0).nodeName === 'SPAN') {
                $fieldElm = $this.find('.coral-Textfield, .coral-FileUpload-input');
                fieldVal = $fieldElm.val();
                $inputElm = $fieldElm;
            } else if ($this.attr('type') === 'hidden') {
                fieldVal = $this.val();
                $fieldElm = $this.siblings('.coral-RichText');
            } else {
                fieldVal = $this.val();
                $fieldElm = $this;
            }

            if (fieldVal === "" && $this.is(fieldClass) && $this.not('.coral-NumberInput')) {
                if ($this.parents('.hide').length) {		
                    $fieldElm.removeClass('is-invalid');
                    $inputElm.attr('aria-required', 'false');
                    isValidate = true;
                } else {
                    $fieldElm.addClass('is-invalid');
                    $inputElm.attr('aria-required', 'true');
                    isValidate = false;
                }					
            } else {
                $fieldElm.removeClass('is-invalid');
                $inputElm.attr('aria-required', 'false');
                isValidate = true;
            }
        });
        return isValidate;
    },

    //For multi item adding feild and has the minimum required items
    multiFieldMinLengthCheck = function(e) {
        var isValidate = false;
        var $this = $(e.currentTarget),
            $form = $this.closest('form.foundation-form'),
            message,
            field = $form.find(".coral-Multifield").not('.hide .coral-Multifield').not('.coral-NumberInput'),
            size = field.data("minitems"),
            ui = $(window).adaptTo("foundation-ui"),
            tabLinkCount = $(field).find('ol').children('li').length;

        $('<input type="text" name="minLengthCheck" aria-required="true" class="is-invalid" style="display:none;"/>').appendTo($form);

        if (size && (tabLinkCount >= 0 && tabLinkCount < size)) {
            message = "Minimum " + size + " items are required.";
            ui.alert("Warning", message, "notice");
            isValidate = false;
            return false;
        } else {
            isValidate = true;
            $('[name="minLengthCheck"]').remove();
        }
        return isValidate;
    },

    //For multi item adding feild and has the maximum required items
    multiFieldMaxLengthCheck = function() {
        $(".js-coral-Multifield-add").click(function() {
            var $this = $(this),
                $field = $this.parent(),
                size = $field.data("maxitems");
            if ($("div[data-path*='utilityMenu']").length === 1) {
                size = $field.data("maxitemsgnav");
            }

            if (size) {
                var ui = $(window).adaptTo("foundation-ui"),
                    totalLinkCount = $this.prev('ol').children('li').length;

                if (totalLinkCount >= size) {
                    ui.alert("Warning", "Maximum " + size + " items are allowed!", "notice");
                    return false;
                }
            }
        });
    },

    //image required validations
    imageRequired = function() {
        var fileUpload = '.coral-FileUpload',
			$fileUpload = $(fileUpload),
            $cuiFileUpload,
            fieldError = '.coral-Form-fielderror';

        /*cuiFileUpload.$element.on("fileuploadsuccess", function (event) {
            alert('fileuploadsuccess');
        });*/
		
		$fileUpload.each(function() {
			var $this = $(this);
			$cuiFileUpload = $this.data('fileUpload');
			
			if ($cuiFileUpload) {
				$cuiFileUpload.$element.find('.cq-FileUpload-clear').on('click', function(event) {
					var $this = $(this);
					if ($this.siblings('.cq-FileUpload-thumbnail-img').html() === '') {
						$this.parents(fileUpload).siblings(fieldError).show();
					}
				});
			
				$cuiFileUpload.$element.on("assetselected", function (event) {
					if (event.path !== '') {
						$(this).siblings(fieldError).hide();
					}
				});
			}
		});
    },

    //field required if checkbox is checked
    validateCheckBoxRequired = function(elem, fieldElem) {
        var fieldError = '.coral-Form-fielderror';
        if($(elem).is(':checked')) {
            $(fieldElem).attr('aria-required', 'true');
        } else {
            $(fieldElem).attr('aria-required', 'false');
            $(fieldElem).removeClass('is-invalid');
            $('.isMandatoryCheckInp').siblings(fieldError).hide();
        }
    },

    //multiple checkbox required check
    validateMultipleCheckbox = function(){      
        var requiredCheck = $('.article-feed').find('.isMandatoryCheck');
        var inputfield = $('.article-feed').find('input[type=text]');
        requiredCheck.on('click',function(){  
               
            if($(this).is(':checked')) {
                var dataName = $(this).data('requiredtocheck');
                inputfield.each(function(i){
                    let inputfieldData =  $(this).data('requiredtocheck');
                    if(inputfieldData === dataName){
                      $(this).attr('aria-required', 'true').change();                     
                    }
                  });
            }else{
                inputfield.each(function(i){
                    var dataName = $(this).data('requiredtocheck');
                    var inputfieldData =  $(this).data('requiredtocheck');                    
                    if(inputfieldData === dataName && inputfieldData !== undefined){
                      $(this).attr('aria-required', 'false').change();    
                      $(this).removeClass('is-invalid');  
                      $(this).removeClass('.coral-Icon--alert');             
                    }
                  });        
                }
        });
    },

    validateOnsubmit =  function(){
        var requiredCheck = $('.article-feed').find('.isMandatoryCheck');
        var inputfield = $('.article-feed').find('input[type=text]');
        requiredCheck.each(function(i){
            if($(this).is(':checked')) {
                var chkedData = $(this).data('requiredtocheck');
                //var textboxData = ''
                inputfield.each(function(){
                    var textboxData =  $(this).data('requiredtocheck');

                    if(chkedData === textboxData){
                        $(this).attr('aria-required', 'true');
                    }
                });

                
            }
            var checkboxData =  $(this).data('requiredtocheck');
            if(checkboxData === inputfield){
              $(this).attr('aria-required', 'true');
              return;
            }
          });
       
    },
    multifieldResetFlag = false,

    multifieldDynamicLimit = function(el) {
        if($('.dynamic-limit-validation-multifield').length > 0) {
            el.each(function (i, element) {
                if($(element).hasClass('custom-parent-selector')) {
                    $('.custom-dynamic-limit-selector select').not('.hide .custom-dynamic-limit-selector select').change();
                }
                if($(element).hasClass('custom-dynamic-limit-selector')) {
                    var maxlimit = $(element).find('select option:selected').data('maxitems');
                    var minlimit = $(element).find('select option:selected').data('minitems');
                    $('.dynamic-limit-validation-multifield').data('maxitems', maxlimit);
                    $('.dynamic-limit-validation-multifield').data('minitems', minlimit);
                    if(multifieldResetFlag) {
                        $('.dynamic-limit-validation-multifield ol').empty();
                        $(".cq-dialog-submit").removeAttr("disabled");
                    }
                    multifieldResetFlag = true;
                }
            })
        }
    },
	
    //Image fields check
    validateImageFields = function(){      
        var requiredCheck = $('.product-tile').find('.isMandatoryCheck');
        var inputfield = $('.product-tile').find('input[type=text]');
        requiredCheck.on('click',function(){                
            if($(this).is(':checked')) {
                var dataName = $(this).data('requiredtocheck');
                inputfield.each(function(i){
                    let inputfieldData =  $(this).data('requiredtocheck');
                    if(inputfieldData === dataName){
                      $(this).attr('aria-required', 'true').change();                     
                    }
                  });
            }else{
                inputfield.each(function(i){
                    var dataName = $(this).data('requiredtocheck');
                    var inputfieldData =  $(this).data('requiredtocheck');                    
                    if(inputfieldData === dataName && inputfieldData !== undefined){
                      $(this).attr('aria-required', 'false').change();    
                      $(this).removeClass('is-invalid');  
                      $(this).removeClass('.coral-Icon--alert');             
                    }
                  });        
                }
        });
    },
	
	//Image fields validation on submit
	validateImageBlockOnsubmit =  function(){
        var requiredCheck = $('.product-tile').find('.isMandatoryCheck');
        var inputfield = $('.product-tile').find('input[type=text]');
        requiredCheck.each(function(i){
            if($(this).is(':checked')) {
                var chkedData = $(this).data('requiredtocheck');
                inputfield.each(function(){
                    var textboxData =  $(this).data('requiredtocheck');
                    if(chkedData === textboxData){
                        $(this).attr('aria-required', 'true');
                    }
                });                
            }
            var checkboxData =  $(this).data('requiredtocheck');
            if(checkboxData === inputfield){
              $(this).attr('aria-required', 'true');
              return;
            }
          });
       
    },

    aritcleFilterValidatons = function(){
         
        if($('.articlefilter').length){
            $('.articlefilter .coral-SelectList-item--option').on('click.selectarticletype',function(){
                var liCount = 0;
                $('.coral-TagList-tag').each(function(){
                    liCount = liCount+1
                });
                if(liCount === 0){
                    $('.articlefilter input[name="./displayArticleTypeFilter"]').attr('disabled','true');
                }else{
                    $('.articlefilter input[name="./displayArticleTypeFilter"]').removeAttr('disabled');
                }
            });

            $(document).on('click','.coral-Icon--close ,.coral-TagList-tag-removeButton',function(){
                var index = 0;
                $('.coral-TagList-tag').each(function(){
                    index = index+1
                });

                if(index === 1){
                    $('.articlefilter input[name="./displayArticleTypeFilter"]').attr('disabled','true');
                }else{
                    $('.articlefilter input[name="./displayArticleTypeFilter"]').removeAttr('disabled');
                }
            });
           

            if( $('.isMandatoryCheck[name="./displayFilterSection"]').is(':checked')){
                $('.filtersection').show();
                $('.filtersection input').each(function(){
                        $(this).attr('aria-required', 'true');
                });
            }
            else{
                $('.filtersection').hide();
                $('.filtersection input').each(function(){
                    $(this).attr('aria-required', 'false');
                });
            }
            $('.isMandatoryCheck[name="./displayFilterSection"]').on('change',function(){                   
                    if($(this).is(':checked')){
                        $('.filtersection').show();
                        $('.filtersection input').each(function(){
                            $(this).attr('aria-required', 'true');
                        });
                    }else{
                        $('.filtersection').hide();
                        $('.filtersection input').each(function(){
                            $(this).attr('aria-required', 'false');
                        });
                    }
            })
        }
        
    },

     salesRepComponentValidations = function(){
            if($('.find-sales-rep').length){


                if( $('.isMandatoryCheck[name="./searchButtonDisplay"]').is(':checked')){
                    $('.searchSection').show();
                    $('.searchSection input').each(function(){
                            $(this).attr('aria-required', 'true');
                    });
                }
                else{
                    $('.searchSection').hide();
                    $('.searchSection input').each(function(){
                        $(this).attr('aria-required', 'false');
                    });
                }
                $('.isMandatoryCheck[name="./searchButtonDisplay"]').on('change',function(){
                        if($(this).is(':checked')){
                            $('.searchSection').show();
                            $('.searchSection input').each(function(){
                                $(this).attr('aria-required', 'true');
                            });
                        }else{
                            $('.searchSection').hide();
                            $('.searchSection input').each(function(){
                                $(this).attr('aria-required', 'false');
                            });
                        }
                })
            }

        },

validateRequiredFileExtn =  function(){
		 var $form = $("form.foundation-form"),
            fieldClass = '.isCsv',
            $requiredField = $form.find(fieldClass).not('.coral-NumberInput'),
            ui = $(window).adaptTo("foundation-ui"),
            $fieldElm;

        $requiredField.each(function() {
            $fieldElm = $(this).find("input");
            if($fieldElm.val().lastIndexOf(".csv") === -1) {
                $fieldElm.addClass('is-invalid')
                $fieldElm.attr('aria-invalid', 'true');
                $fieldElm.attr('aria-required', 'true');
                if($fieldElm.val() === "") {
                    ui.alert("Please upload csv file");
                } else {
                    ui.alert("Invalid file extension. Please upload csv file");      
                }
                $fieldElm.val("");
            }
        });
    }

$(document).on("dialog-ready", function() {
    if(!checkAuthorFeatureFlag('jsFramework')){
        return;
    }
    validateMultipleCheckbox();
	validateImageFields();
    $(document).on("click", ".cq-dialog-submit", function(e) {
        //required field validation
        validateRequired();

        // min length check for links
        multiFieldMinLengthCheck(e);

        if($(document).find('.isCsv').length){
		    validateRequiredFileExtn();
		}

        if($(document).find('.article-feed').length){
            //added to validate on sumbit
            validateMultipleCheckbox();

            validateOnsubmit();
        }
		
		if($(document).find('.product-tile').length){
            //added to validate on sumbit
            validateImageFields();	
			validateImageBlockOnsubmit();
        }
    });

    $(document).on("change.articlefeed", ".isMandatoryCheck", function(e) {
        validateMultipleCheckbox();
    });
	$(document).on("change.producttile", ".isMandatoryCheck", function(e) {
        validateImageFields();
    });
    
    $(document).on("selected", ".custom-parent-selector.coral-Select, .custom-dynamic-limit-selector.coral-Select", function (e) {
        multifieldDynamicLimit($(this));
    });

    $('.isMandatoryCheck').on('change', function(){
        validateCheckBoxRequired($(this),'.isMandatoryCheckInp .coral-Textfield');
        validateCheckBoxRequired($(this),'.isMandatoryCheckInp.coral-Textfield');
    });

   
    
    //apply dynamic limit validation on multiField
    multifieldResetFlag = false;
    multifieldDynamicLimit($(".custom-dynamic-limit-selector.coral-Select").not('.hide .custom-dynamic-limit-selector.coral-Select'));

    //validateCheckBoxRequired
    validateCheckBoxRequired('.isMandatoryCheck', '.isMandatoryCheckInp .coral-Textfield');
    validateCheckBoxRequired('.isMandatoryCheck', '.isMandatoryCheckInp.coral-Textfield');

    //multiFieldMaxLengthCheck
    multiFieldMaxLengthCheck();

    //imageRequired
    imageRequired();

    aritcleFilterValidatons();

    salesRepComponentValidations();


    //Validation is added for NAV_03 component.
    /*$(document).on('click.checkPathConfirm','.coral-Pathbrowser-picker .js-coral-pathbrowser-confirm', function(){
    $('.isPathTitleMandatory.is-invalid').each(function(){
        var $this = $(this);

         setTimeout(function(){
             var $titleInp =  $this,
                 $cval = $titleInp.val();
             if($cval != '') {
                 $titleInp.removeClass('is-invalid');
             }
           }, 400);
         })
      });
      
    });*/

});


