(function (document, $) {
  "use strict";

  // listen for dialog injection
  // $(document).on("foundation-contentloaded", function (e) {
  //   $(".showhideCheckBox").each(function () {
  //     showHide($(this));
  //   });
  // });

  $(document).on('dialog-ready', function(e){
    if(!checkAuthorFeatureFlag('jsFramework')){
        return;
    }

    $(document).on("selected", ".coral-Select", function(e) {
       
    });

    $(".showhideCheckBox").each(function () {
      showHide($(this));
    });

    // listen for toggle change
    $(document).on("change", ".showhideCheckBox", function (e) {
      showHide($(this));
    });
  });

  // show/hide our target depending on toggle state
  function showHide(el) {
    var target = el.data("showhideTarget"),
      value = el.prop("checked") ? el.val() : "";
      if(value==""){
		$(target).find("input[type=checkbox]").prop("checked","");
          $(target).find("input[type=checkbox]").prop("checked",false);
      }
    // hide all targets by default
    $(target).not(".hide").addClass("hide");

    // show any targets with a matching target value

    $(target).filter("[data-showhide-target-value=\"" + value + "\"]").removeClass("hide");


  }

})(document, Granite.$);