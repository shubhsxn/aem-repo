/*global window, parent, navigator, $, DFV, DFCountries */
$('.generic-form-container form').ready(function () {
    "use strict";

    window.DFV = {
        wrapper: $('.bgdss-contactForm form'),
        init: function () {
            DFV.WebCentralCode.includeFields();
            //Add more apprends or include on top of the form here
        },
        submit: function () {

            DFV.genericValidation.optIn();
            //DFV.genericValidation.companyCheck();
            DFV.genericValidation.checkRedirectUrl();
            // Populating Origin Tracking Code
            $('input[name=00N30000002RIVW]').val((sessionStorage.getItem('otc') != null) ? sessionStorage.getItem('otc'): '');
                                                if(DFV.genericValidation.emailCheck()){
                                                                return true;
                                                } else{
                                                                return false;
                                                }

            //Add more validations here
        },
        submitRouting: function () {
            var submitResult = DFV.submit();
            if (typeof (DFCountries) !== 'undefined' && typeof (DFCountries.countries) !== 'undefined') {
                DFV.routingValidation.routing(DFCountries.countries);
            }
            return submitResult;
            //Add more validations here
        }
    };

    (function ($, DFV) {
        function elementExist(element) {
            return element.length !== 0;
        }

        function getInputByName(name, checked) {
            return DFV.wrapper.find('input[name="' + name + '"]' + (checked ? ':checked' : ''));
        }

        function getSelectByName(name) {
            return DFV.wrapper.find('select[name="' + name + '"]');
        }

        function getInputByNameValue(name, checked) {
            var item = getInputByName(name, checked);
            return elementExist(item) && item.val() !== '' ? item.val() : null;
        }

        function addField(object) {
            var item,
                newItem;

            item = DFV.wrapper.find('input[name="' + object.name + '"]');

            if (!elementExist(item)) {
                newItem = $('<input>').attr({
                    type: 'hidden',
                    id: object.name,
                    name: object.name,
                    value: object.value
                });

                DFV.wrapper.append(newItem);
            } else {
                item.attr({
                    value: object.value
                });
            }
        }

        function getQueryParameterByName(name) {
            name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
            var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
                results = regex.exec(location.search);
            return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
        }


        DFV.WebCentralCode = {
            includeFields: function () {
                //Populating Origin URL
                addField({
                    name: '00N30000001Yh27',
                    value: parent.document.referrer
                });

                //Populating Origin Form
                addField({
                    name: '00N30000008QxPj',
                    value: window.location.hostname + window.location.pathname
                });

                //Populating Direction
                addField({
                    name: '00N30000001UjbA',
                    value: "Inbound"
                });

                //Populating Origin Channel Type
                addField({
                    name: '00N30000001Gow6',
                    value: "Website"
                });

                //Populating Extended Data
                addField({
                    name: '00N300000067cCK',
                    value: "Web2L_CentralCode_V1"
                });

                /* Temporally commented since the field is already
                 * populated with Form Builder
                // Populating Origin Name 
                if (!getInputByNameValue('00N30000001V0Js')) {
                    addField({
                        name: '00N30000001V0Js',
                        value: windowLocation.hostname + '/' + path.substring(0, path.indexOf(('/'), path.indexOf('/') + 1))
                    });
                }
                 */
            },   

        };

        DFV.genericValidation = {
            joinCheckboxes: function () {
                var item,
                    checkboxes = {},
                    key,
                    name,
                    value;

                DFV.wrapper.find('input:checked').each(function () {
                    name = $(this).attr("name");
                    value = $(this).val();

                    if (value.toLowerCase() === 'other') {
                        item = DFV.wrapper.find('input[type="text"][name="' + name + '_input"]');
                        value = (elementExist(item) && item.val() !== "") ? item.val() : value;
                    }

                    if (checkboxes[name]) {
                        checkboxes[name].values += ';' + value;
                    } else {
                        checkboxes[name] = {
                            input: name.slice(0, name.lastIndexOf("_")),
                            values: value
                        };
                    }
                });

                for (key in checkboxes) {
                    if (checkboxes.hasOwnProperty(key)) {
                        addField({
                            name: checkboxes[key].input,
                            value: checkboxes[key].values
                        });
                    }
                }
            },

                                                emailCheck: function () {                                                              
                var item1 = getInputByName('email', false);
                                                                var item2 = getInputByName('confirm_email', false);

                if (getInputByNameValue('email', false) && getInputByNameValue('confirm_email', false)) {
                    if(getInputByNameValue('email', false) != getInputByNameValue('confirm_email', false)){
                                                                                                $('#confirm_email_error').html('Emails do not match');
                                                                                                $('#confirm_email_error').css('display', 'inline-block'); 

                                                                                                return false;
                                                                                } else {
                                                                                                return true;                                                                        
                                                                                }
                } else {
                    return true;
                }
            },

            optIn: function () {
                var item = getInputByName('00N30000001Tvd8', true),
                    multipleCampaign = getInputByNameValue('00N30000003vYTt'),
                    campaignID = getInputByNameValue('opt-in_campaign_id'),
                    memberStatus = getInputByNameValue('member_status_value'),
                    differentOptInName = getInputByNameValue('opt-in_different_name'),
                    campaign2 = getInputByNameValue('campaign2'),
                    hiddenName;

               /* if (elementExist(item) && multipleCampaign) {
                    getInputByName('00N30000003vYTt', false).val(multipleCampaign + ';' + campaign2);
                }*/

                if (  elementExist(item) && getInputByName('00N30000003vYTt')   ) { 
                   getInputByName('00N30000003vYTt').val(getInputByNameValue('oid')+campaignID+':in'); 
                }
                if (elementExist(item) && campaignID) {

                    //Different Opt-in
                    if (differentOptInName) {
                        hiddenName = getInputByNameValue(differentOptInName);

                        if (hiddenName) {
                            addField({
                                name: differentOptInName,
                                value: item.val()
                            });
                        }
                    }

                    //Standard opt-in
                    if (!multipleCampaign) {
                        addField({
                            name: 'Campaign_ID',
                            value: campaignID
                        });
                    }

                    addField({
                        name: 'member_status_value',
                        value: memberStatus
                    });
                }
            },
            companyCheck: function () {
                var item = getInputByName('company', false);
                if (!elementExist(item) || !getInputByNameValue('company', false)) {
                    addField({
                        name: 'company',
                        value: 'Not Provided'
                    });
                }
            },

            checkRedirectUrl: function() {
                var thanks2= $('#redirectFullURL2').val();
                if($('#00N30000001Tvd8-0').is(":checked") && $('#00N30000001Tvd8-0').length > 0 ){
                    if(thanks2.length > 7) {
                        $('.generic-form-container input[name=redirect_page]').attr('value', $('#redirectFullURL2').val());
                    }    
                }
            }
        };

        DFV.routingValidation = {
            routing: function (countryqueue) {
                var element = getSelectByName('00N30000001UlfP'),
                    itemSelected = elementExist(element) && element.val() !== '' ? element.val() : null,
                    key;

                if (itemSelected) {
                    for (key in countryqueue) {
                        if (countryqueue.hasOwnProperty(key) && ((countryqueue[key].abbr).toUpperCase() === itemSelected.toUpperCase())) {
                            addField({
                                name: '00N30000001V1vY',
                                value: countryqueue[key].region
                            });

                            addField({
                                name: '00N30000001H5sV',
                                value: countryqueue[key].queue
                            });
                            break;
                        }
                    }
                }
            }
        };
    }($, DFV));

    DFV.init();
});

$(document).ready(function () {
    $('.generic-form-container form').ready(function () {
        var referrer =  document.referrer;
        console.log("referrer page is: "+referrer);
        var vars = [], hash;
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        for(var i = 0; i < hashes.length; i++){
            hash = hashes[i].split('=');
            if (hash[0].indexOf("checkbox") >= 0){
                vars[hash[0]] = hash[1];
                var loopTrue = true;
                var index = 0;
                while(loopTrue){
                    var idCheck = $("#"+hash[0] +'-'+index );
                    var valueCB = idCheck.val();
                    var spValuesHash = hash[1].split("%3B");

                    for (var item in spValuesHash) {
                        if(spValuesHash[item] == valueCB){
                            idCheck.prop('checked', true);
                        }
                    }

                    index++;
                    if($("#"+hash[0] +'-'+index ).length === 0){
                        loopTrue = false;
                    }
                }
            }
        }
    });
});