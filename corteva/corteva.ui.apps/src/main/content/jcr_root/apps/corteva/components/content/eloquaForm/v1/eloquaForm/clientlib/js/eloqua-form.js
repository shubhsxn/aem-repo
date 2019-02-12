/** Eloqua Form. */
const FormModule = function() {
  /**
   * Initialization function
   */

function initialize() {
    if (!checkFeatureFlag('eloquaForm')) {
      return;
    }

    let $form = $('.elq-form:not(.custom-form .elq-form)'),
      isValueChanged = false;

    createCookie();

    if ($form.length) {
      loadCaptcha();
      $form.on('keyup change blur', 'input, textarea, select', function(e) {
        e.preventDefault();
        let $this = $(this);

        //fieldsValidation
        fieldsValidation($this);

        if ((e.type === 'blur' || e.type === 'focusout') && $this.attr('name') === 'zipPostal') {
          //postalCodeValidation
          if (getCountryRegion().region !== '' && getCountryRegion().region === 'na' && isValueChanged) {
            postalCodeValidation($this);
            isValueChanged = false;
          }
        } else if (e.type === 'change') {
          if ( $this.attr('name') === 'country') {
            let $mobilefield = $this.closest('form').find('[name="mobilePhone"]'),
            $zipPostalField = $this.closest('form').find('[name="zipPostal"]');
            setMinLength($zipPostalField);
            setMaxLength($mobilefield);
            setPattern($mobilefield);
            setPattern($zipPostalField);
            //populate state
            populateState($this);
            //setSiteIdAndFormAction
            setSiteIdAndFormAction($this, $form);
          } else if ($this.attr('name') === 'zipPostal') {
            isValueChanged = true;
          }

        } else if (e.type === 'keyup') {
          if ($this.attr('maxlength')) {
            maxLengthHandlerOnAndroid($this, e);
          }
          if ($this.attr('type') === 'checkbox') {
            const ua = navigator.userAgent.toLowerCase();

            if (ua.indexOf('trident/') === -1) {
              setCheckBoxAccessibility($this,e);
            }
          }
        }
      });

      $form.find('input, select').each(function(e){
        let $this = $(this);
        if ($this.val() !== '') {
          $this.trigger('blur');
        }
      });
      //form submission
      formSubmission();
    }
  }

  /**
   * * coookieHandler
   * create/get/delete Cookie
   * */
   let setCookie = function(name, value, hrs = 2) {
      const date = new Date();
      date.setTime(date.getTime() + (hrs * 60 * 60 * 1000));
      const expires = '; expires=' + date.toUTCString();
      document.cookie = name + '=' + encodeURIComponent(value) + expires + '; path=/';
  },

  getCookie = function(name) {
      return document.cookie.split('; ').reduce((r, v) => {
        const parts = v.split('=')
        return parts[0] === name ? decodeURIComponent(parts[1]) : r
      }, '');
  },

  /**
   * * Get query param
   * create/get/delete Cookie
   * */

  getQueryParam = function(name, url = window.location.href) {
      name = name.replace(/[\[\]]/g, '\\$&');

      let regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);

      if (!results) {
        return null;
      }
      if (!results[2]) {
        return '';
      }

      return decodeURIComponent(results[2].replace(/\+/g, ' '));
  },

  /**
   * * createCookie
   * create cookie if query parameter exist
   * */
  createCookie = function() {
    const utmSrc = 'utm_source',
          utmMedium = 'utm_medium',
          elqCampaignId = 'elqCampaignId',
          src = 'src';

    let payload = {};

    if (getQueryParam(utmSrc) || getQueryParam(utmMedium) || getQueryParam(src) || getQueryParam(elqCampaignId)) {
      payload[utmSrc] = getQueryParam(utmSrc)
      payload[utmMedium] = getQueryParam(utmMedium)
      payload[src] = getQueryParam(src)
      payload[elqCampaignId] = getQueryParam(elqCampaignId)

      if (getCookie('eloquaFormCookie')) {
        let siteCookie = getCookie('eloquaFormCookie');
        siteCookie = JSON.parse(siteCookie);
        if (siteCookie.utm_source !== getQueryParam(utmSrc) ||
          siteCookie.utm_medium !== getQueryParam(utmMedium) ||
          siteCookie.src !== getQueryParam(src) ||
          siteCookie.elqCampaignId !== getQueryParam(elqCampaignId)) {
          setCookie('eloquaFormCookie', JSON.stringify(payload), 2);
        }
      } else {
        setCookie('eloquaFormCookie', JSON.stringify(payload), 2);
      }
    }
    //populateHdnFldFromCookie
    populateHdnFldFromCookie();
  },

  /**
   * * populateHdnFldFromCookie
   * populate values in hidden field from cookie
   * */
  populateHdnFldFromCookie = function() {
    if (getCookie('eloquaFormCookie')) {
      let siteCookie = getCookie('eloquaFormCookie');
      siteCookie = JSON.parse(siteCookie);
      if(siteCookie.utm_source && siteCookie.utm_source.length > 0){
        $('[name="utm_source"]').val(siteCookie.utm_source);
      } else {
        $('[name="utm_source"]').val("Direct");
      }

      if(siteCookie.utm_medium && siteCookie.utm_medium.length > 0){
        $('[name="utm_medium"]').val(siteCookie.utm_medium);
      } else {
        $('[name="utm_medium"]').val("Direct");
      }

      if(siteCookie.src && siteCookie.src.length > 0){
        $('[name="originTrackingCode1"]').val(siteCookie.src);
      }

      if(siteCookie.elqCampaignId && siteCookie.elqCampaignId.length > 0){
        $('[name="elqCampaignId"]').val(siteCookie.elqCampaignId);
      } else {
        // do not change actual value
        $('[name="elqCampaignId"]').val($('[name="elqCampaignId"]').data('author-val'));
      }
    }
    if (document.referrer !== '' && document.referrer !== location.href) {
      $('[name="originURL1"]').val(document.referrer);
    }
  },

  /**
   * * fieldValidation
   * validate fields
   * */

  fieldsValidation = function(field) {
    let $fields = $(field),
      invalidClass = 'invalid',
      filledCl = 'filled';

    function addError($input, message) {
      $input.parent('fieldset').find('.error').html(message).show();
    }

    $fields.each((index, elm) => {
      let $this = $(elm),
          elem = $this.get(0);

      if ($this.val() === '' && $this.attr('required')) {
          $this.addClass(invalidClass).removeClass(filledCl);
          addError($this, $this.data('required-msg'));
      } else {
          if (!$this.hasClass(filledCl)) {
            $this.addClass(filledCl);
          }

          // Check validity
          if ('validity' in elem && elem.validity.valid === false) {
            let validStatus = elem.validity;
            $this.addClass(invalidClass);

            if ($this.data('error')) {
              addError($this, $this.data("error"));
            }
            else if (validStatus.badInput) {
              addError($this, "We don't understand this input");
            }
            else if (validStatus.patternMismatch) {
              addError($this, $this.data('pattern-msg'));
            }
            else if (validStatus.rangeOverflow) {
              addError($this, "That's too much");
            }
            else if (validStatus.rangeUnderflow) {
              addError($this, "That's too little");
            }
            else if (validStatus.typeMismatch) {
              addError($this, `Please enter a valid ${$this.prop("type")}`);
            }
            else if (validStatus.valueMissing) {
              addError($this, "This field is required");
            }
          } else if ("validity" in elem && elem.validity.valid === true) {
            if (!$this.hasClass('server-rejected')) {
              $this.removeClass(invalidClass).nextAll('.error').hide();
            }
          }
      }
    });
  },

  /**
   * * serializeForm
   * concate form elements value in json format
   * */
  serializeForm = function(form) {
    if (!form || form.nodeName !== "FORM") {
        return;
    }

    let i, j, q = [];

    for (i = form.elements.length - 1; i >= 0; i = i - 1) {
      if (form.elements[i].name === '') {
        continue;
      }
      switch (form.elements[i].nodeName) {
        case 'INPUT':
          switch (form.elements[i].type) {
            case 'text':
            case 'tel':
            case 'email':
            case 'hidden':
            case 'password':
            case 'button':
            case 'reset':
            case 'submit':
              if(form.elements[i].name === 'mobilePhone'){
                q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements.countryCode.value+' '+form.elements[i].value.replace(/[\D]/g, "")));
              }
              else{
                q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
              }
              break;
            case 'checkbox':
            case 'radio':
              if (form.elements[i].checked) {
                q.push(form.elements[i].name + "= on");
              } else {
                q.push(form.elements[i].name + "= off");
              }
              break;
          }
          break;
          case 'file':
          break;
        case 'TEXTAREA':
            q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
            break;
        case 'SELECT':
          if (form.elements[i].name !== 'countryCode'){
            switch (form.elements[i].type) {
            case 'select-one':
              q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
              break;
            case 'select-multiple':
              for (j = form.elements[i].options.length - 1; j >= 0; j = j - 1) {
                if (form.elements[i].options[j].selected) {
                  q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].options[j].value));
                }
              }
              break;
            }
          }
          break;
        case 'BUTTON':
          switch (form.elements[i].type) {
            case 'reset':
            case 'submit':
            case 'button':
              q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
              break;
          }
          break;
        }
      }
    q = q.reverse();
    return q.join('&');
  },

  /**
   * * scrollTo
   * scroll to the field
   * */

  scrollTo = function(field) {
     $('html, body').animate({
          scrollTop: $(field).offset().top - 30 - $('.global-nav').height()
      }, 500);
  },

   /**
   * captchaSubmitCallback
   * captcha validation
   * */

  captchaSubmitCallback = function(form) {
    let $captcha = $(form).find('.g-recaptcha'),
        $errField = $captcha.siblings('.error');

    if (typeof grecaptcha !== 'undefined' && grecaptcha.getResponse() === '') {
      $captcha.addClass('invalid');
      if ($errField.html() === '') {
        $errField.html($captcha.data('required-msg'));
      }
      $errField.show();
    } else {
      handleErrCases($captcha);
    }
  },

  /**
   * * createCookie
   * create cookie if query parameter exist
   * */
  formSubmission = function() {
    let $form = $('.elq-form'),
      $fields = $form.find('input, textarea, select'),
      $apiErrorFld = $form.find('.api-error'),
      invalidClass = 'invalid',
      $postalCodeFld = $('[name=zipPostal]'),
      $successMsg = $('.success-msg'),
      $formWrap = $successMsg.siblings('.frm01-form:not(.custom-form)');

    $('.c-button--submit').on('click', (e) => {
      e.preventDefault();

      captchaSubmitCallback($form);

      //fieldsValidation
      fieldsValidation($fields);

      $apiErrorFld.addClass('hide');

      let $invalidClass = $('.'+invalidClass+ ', .server-rejected');

      if ($invalidClass.length) {
        scrollTo($invalidClass.eq(0));
        $invalidClass.eq(0).focus();
        return false;
      }

      //refetch zip code details
      if ($postalCodeFld.val() !== '' && !$postalCodeFld.is('.server-rejected')) {
        $postalCodeFld.blur();
      }

      $($form).find('[name=g-recaptcha-response]').remove();

      fetch($form.attr('action'), {
          method: 'POST',
          mode: 'cors',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
          },
          body: serializeForm($form.get(0))
      })
      .then(response => {
        return response.text().then(function(text) {
         return $.trim(text)
        });
        //return response.json();
      })
      .then(responseData => {
          if (responseData.indexOf($form.attr('data-confirmation-url')) !== -1) {
            $successMsg.removeClass('hide').fadeIn(500);
            $formWrap.fadeOut(500);
            $([document.documentElement, document.body]).animate({
              scrollTop: $successMsg.offset().top
          }, 1000);
          } else {
            formError(responseData, $form);
          }
      })
      .catch(error => {
        if (error && !$('.api-fields-error').length) {
          $apiErrorFld.removeClass('hide');
          scrollTo($apiErrorFld);
          //resetCaptcha
          resetCaptcha();
        }
      });
    });
  },

  /**
   * * formError
   * form success method handling
   * */

  formError = function (error, $form) {
    if (error) {
      let errorMsg = '<p>' + $(error).find('div:eq(0)').text() + '</p>' + $(error).find('div:eq(1)').html(),
          apiFldErr = 'api-fields-error',
          $apiErrField = $('.'+apiFldErr),
          errorMsgDiv = '<div class="api-error '+apiFldErr+'">' + errorMsg + '</div>'

      if ($apiErrField.length) {
        $apiErrField.remove();
      }

      $form.prepend(errorMsgDiv);
      scrollTo($apiErrField);

      //resetCaptcha
      resetCaptcha();
    }
  },

  /**
   * * handleErrCases
   * show/hide errors and error messages
   * */
  handleErrCases = function(field) {
    let $field = $(field),
        elm = $field.get(0),
        invalidClass = 'invalid',
        filledCl = 'filled';

    if ($field.is('.'+invalidClass) && $field.val() !== '') {
      $field
      .removeClass(invalidClass+ ' '+ filledCl)
      .siblings('.error')
      .hide()
      .addClass(filledCl);
      elm.setCustomValidity('');
    }
    if ($field.val() !== '' && !$field.is('.'+filledCl)) {
      $field.addClass(filledCl);
    }
  },


  /**
   * * resetCaptcha
   * reset captcha on form submission error
   * */

  resetCaptcha = function() {
    try {
        if (typeof grecaptcha !== 'undefined') {
          grecaptcha.reset();
        }
    } catch (error) {
        throw new Error(error);
    }
  },

  /**
   * * postalCodeValidation
   * postal Code validation through bingmap API
   * */

  postalCodeValidation = function(field) {
      let $this = $(field),
          state = '[name="stateProv"]',
          city = '[name="city"]',
          $errorField = $this.siblings('.error'),
          invalidClass = 'invalid';

      if ($this.is('.filled') && !$this.is('.'+invalidClass)) {
        let postalCode = $this.val(),
            countryRegion = $.trim($('[name="country"]').val()),
            queryParam = '&countryRegion=' + countryRegion + '&postalCode=' + postalCode,
            apiURL = $this.data('api-url') + queryParam;

        fetch(apiURL)
        .then(response => {
          return response.json();
        })
        .then(responseData => {
           if (responseData) {
              const resData = responseData.resourceSets[0].resources[0].address;
              if (resData.postalCode === postalCode) {
                if ($(state).length) {
                  $(state).val(resData.adminDistrict);
                  handleErrCases(state);
                }
                if ($(city).length) {
                  $(city).val(resData.locality);
                  handleErrCases(city);
                }

                $this.removeClass('server-rejected');
                $errorField.hide();
              } else {
                $this.addClass('server-rejected');
                $errorField.html($this.data('apierror')).show();
                if ($(state).length) {
                  $(state).blur();
                }

                if ($(city).length) {
                  $(city).val('').blur();
                }
              }
           }
        })
        .catch(error => {
          if (error) {
            console.log('There is an error with service: ' + error);
          }
        });
      }
  },

  /**
   * * replaceFields
   * replaceFields based on country
   * */

  replaceFields = function(field, element) {
    let $fieldAttrs = field.prop('attributes'),
        $elm = $(element),
        analyticsType = (element === '<input />') ? 'form-text' : 'form-list';

    $.each($fieldAttrs, function() {
        $elm.attr(this.name, this.value);
    });

    $elm.attr('data-analytics-type', analyticsType);

    field.replaceWith(
      $elm.bind('change')
    );
  },

  /**
   * * populateState
   * populateState based on country
   * */

  populateState = function(field) {
    let $country = $(field),
      $form = $('.elq-form'),
      $state = $form.find('[name="stateProv"]'),
      $countryCode = $form.find('[name="countryCode"]'),
      $postalCodeFld = $('[name=zipPostal]');

    const responseData = $country.data('statemapping');

      if (responseData) {
          let options = '';
          const selectedMap  = responseData.filter((item) => item.title === $country.val());

          $state.html('').val('');
          $countryCode.val('');

          if (selectedMap.length) {

            // country code validation and selection
            if (selectedMap[0].description) {
              $countryCode.val(selectedMap[0].description);
            } else {
              $countryCode.val('');
            }

            handleErrCases($countryCode);

            // state population
            if ($state.length) {
              //#fix add a country change
              if (getCountryRegion().region === 'na' && $(selectedMap[0].childTags).length) {
                if ($state.get(0).nodeName === 'INPUT') {
                  replaceFields($state, '<select />');
                }

                $(selectedMap[0].childTags).each((index, elm) => {
                  options += '<option value="'+elm.value+'">' + elm.localizedTitle + '</option>';
                });

                $form.find('[name="stateProv"]').html('<option value=""></option>' + options);
                handleErrCases($state);
              } else {
                if ($state.get(0).nodeName === 'SELECT') {
                  replaceFields($state, '<input />');
                }
                handleErrCases($state);
              }
            }
          }
          if ($postalCodeFld.val() !== '') {
            $postalCodeFld.blur();
          }
      }
  },

  /**
   * * maxLengthHandlerOnAndroid
   * max length is not supported on Android so this is the manaual fix on Android
   * */
  maxLengthHandlerOnAndroid = function(context, e) {
    const ua = navigator.userAgent.toLowerCase(),
          isAndroid = ua.indexOf('android') > -1; //&& ua.indexOf("mobile");

    if (isAndroid) {
      let $this = $(context),
          maxVal = parseInt($this.attr('maxlength'), 10);

      if ($this.val().length > maxVal) {
          $this.val($this.val().substring(0, maxVal));
          return false;
      }
    }
  },

  getCountryRegion = function() {
    let fieldVal = $('form select[name="country"]').find('option:selected').data('country-code'),
        countryVal = 'global';

    if(fieldVal){
      fieldVal = fieldVal.toLowerCase();
      if ($.isArray(fieldVal.match(/^.*?us|ca$/i))) {
        countryVal = 'na';
      }
    }

    return {
      'region':countryVal,
      'country': fieldVal
    };
  },

  setMinLength = function($field) {
    let minLength = 1;
    if (getCountryRegion().country !== '') {
      if (getCountryRegion().country === 'ca') {
        minLength = 6;
      } else if (getCountryRegion().country === 'us') {
        minLength = 5;
      }
    }
    minLength && $field.attr('minlength', minLength);
  },

  setMaxLength = function($field, maxLength = 35){
    if (getCountryRegion().region !== '' && (getCountryRegion().region === 'na' || getCountryRegion().region === 'ca')) {
      maxLength = 10;
    }
    $field.attr('maxlength', maxLength);
  },

  setPattern = function($field){
    if ($field.length > 0) {
      if($field.attr('name').includes('zip')){
        if(getCountryRegion().country !== '' && getCountryRegion().country ==='ca'){
          $field.attr('pattern',$field.data('pattern'));
        } else if (getCountryRegion().country !== '' && getCountryRegion().country ==='us') {
          $field.attr('pattern',$field.data('patternus'));
        }
        else{
          $field.removeAttr('pattern');
        }
      }
      else if($field.attr('name').includes('mobile')){
        if (getCountryRegion().region !== '' && getCountryRegion().region === 'na') {
          $field.attr('pattern',$field.data('pattern'));
        }
        else{
          $field.removeAttr('pattern');
        }
      }
    }
  },

   /**
   * * setSiteIdAndFormAction
   * max length is not supported on Android so this is the manaual fix on Android
   * */
  setSiteIdAndFormAction = function(field, $form) {
    //const fieldVal = $(field).find('option:selected').data('country-code').toLowerCase();
    let countryVal = getCountryRegion().region;

    $form.attr('action', $form.data(countryVal+'-submit-url'));
    $form.find('[name="elqSiteId"]').val($form.data(countryVal+'-siteid'));
  },

  /**
   * * checkboxAccesibility
   * accessibility for checkbox
   * */
  setCheckBoxAccessibility = function(context, e) {
    let event = e || window.event,
        $this = context;
    event.stopImmediatePropagation();

    if (event.keyCode === 32 || event.charCode === 32) {
        if ($this.is(":checked")) {
            $this.prop("checked", false);
        }
        else {
            $this.prop("checked", true);
        }
    }
  },
   //load captcha
   loadCaptcha = function() {
    let path = "https://www.google.com/recaptcha/api.js?hl=";
    let lang = $('.g-recaptcha').data('language');
    $('head').append('<script type="text/javascript" src='+path+''+lang+'"><\/script>');
}


  return initialize();

};

FormModule();

$(document).ready(function(){

  setTimeout(()=>{
    try{
      if (_satellite){
        let mcid = _satellite.getVisitorId().getMarketingCloudVisitorID()
        $('[name="adobe_mcid"]').val(mcid);
      } else {
        console.log('Adobe Cloud ID does not have a visitor ID');
      }
    } catch (e){
      console.log('Adobe Cloud ID object Error');
    }
  }, 2000);
})
