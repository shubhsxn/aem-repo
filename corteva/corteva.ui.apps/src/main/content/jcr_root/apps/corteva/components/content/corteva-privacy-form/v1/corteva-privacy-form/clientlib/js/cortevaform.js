
$(document).ready(function () {


    $("#cortevaformSubmit").on("click",function(event){ 
       event.preventDefault();
   	   DFV.submit();    

    var flag = 1;
    var phoneno = /^(?=.*?[0-9])[0-9+()-]+$/;
    var pattern = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;   


    $("form .required").each(function( index ) {  

        $( this ).closest('fieldset').find('div.urlmessage ').hide();    
        $( this ).closest('fieldset').find('div.errormessage ').hide();
        $( this ).closest('fieldset').find('div.urlmessage ').remove();

        if(!$( this ).val() || $( this ).val()=="-Select-")  
        {

            $( this ).closest('fieldset').find('div.errormessage ').addClass("requiredtext_form");
            $( this ).closest('fieldset').find('div.errormessage ').show();

            flag = 0;


        }else if($(this).val() && $(this).attr('name') == 'description' && $(this).is(":visible")){  
             console.log("else if");
             var spamPattern = /(telnet|ftp|https?):\/\/(?:[a-z0-9][a-z0-9-]{0,61}[a-z0-9]\.|[a-z0-9]\.)+[a-z]{2,63}/i;

             if($(this).val().trim().match(spamPattern)){                    
                    $( this ).closest('fieldset').append("<div class='errormessage urlmessage' style='margin-top: -1px ;'>URLS are not allowed</div>");    
                    $( this ).closest('fieldset').find('div.urlmessage ').show(); 
                    flag = 0;
         		}	
             	else{
            		$( this ).closest('fieldset').find('div.urlmessage ').hide();    
                    $( this ).closest('fieldset').find('div.errormessage ').hide();
        	    }

        }

    });

	/*$('.bgdss-form *').filter(':input[type!="hidden"]').each(function(){
         var spamPattern = /(telnet|ftp|https?):\/\/(?:[a-z0-9][a-z0-9-]{0,61}[a-z0-9]\.|[a-z0-9]\.)+[a-z]{2,63}/i;
         if($(this).val()){
             if($(this).val().trim().match(spamPattern)){                    
                    $( this ).closest('fieldset').find('div.errormessage ').text("URLs are not allowed");
                    $( this ).closest('fieldset').find('div.errormessage ').show();
                    flag = 0;
         		}	
             	else{
            		$( this ).closest('fieldset').find('div.errormessage ').hide();  
        	    }
            }
	 });*/

    if($( "#phone.required" ).length){


          if(!$(".required#phone").val() || !$(".required#phone").val().trim().match(phoneno)){

            $(".required#phone").closest('fieldset').find('div.errormessage').addClass("requiredtext_form");
            $(".required#phone").closest('fieldset').find('div.errormessage').show();

            flag = 0;


    	}

        }
        else{
            $("#phone").closest('fieldset').find('div.errormessage ').hide();  
        }

   		if($( "#email.required" ).length){


            if($(".required#email").length && !$(".required#email").val().match(pattern)){

                $(".required#email").closest('fieldset').find('div.errormessage').addClass("requiredtext_form");
                $(".required#email").closest('fieldset').find('div.errormessage').show();

                flag = 0;

            }
        }
        else{
            $("#email" ).closest('fieldset').find('div.errormessage ').hide();  
        }        



/*    var phoneno = /^[0-9]*(?:\.\d{1,2})?$/; 
if($(".required#phone").length ){
    if(!$(".required#phone").val() || !$(".required#inputPhone").val().trim().match(phoneno)){

        $(".required#phone").closest('fieldset').find('div.errormessage').addClass("requiredtext_form");
        $(".required#phone").closest('fieldset').find('div.errormessage').show();

        flag = 0;

    }

    else{
        $(".required#phone").closest('fieldset').find('div.errormessage').hide();


    }
} */
    if($(".errormessage").is(":visible")){
		console.log("form error message");

        return;

    }
    else{
		console.log("form submit");

        $(".bgdss-form").submit();
    }
});

    var redirectFullURL = $('.bgdss-contactForm input[name="redirect_FullURL2"]').attr('value');
	if(typeof redirectFullURL!='undefined' ){
    console.log(redirectFullURL,"redirectFullURL");
    if (redirectFullURL.indexOf('http:') < 0 && redirectFullURL.indexOf('https:') < 0) {
            var currentUrl = window.location.href;
            var splitCurrentParts = currentUrl.split("/");
            redirectFullURL = splitCurrentParts[0] + redirectFullURL;
        }

    $('.bgdss-contactForm input[name=redirect_page]').attr('value', redirectFullURL);
	$('.bgdss-contactForm input[name=retURL]').attr('value', redirectFullURL);
	}
     //$('.bgdss-contactForm input[name=redirect_FullURL2]').attr('value', redirectFullURL);
    // $('.bgdss-contactForm input[name=redirect_FullURL]').attr('value', redirectFullURL);

});