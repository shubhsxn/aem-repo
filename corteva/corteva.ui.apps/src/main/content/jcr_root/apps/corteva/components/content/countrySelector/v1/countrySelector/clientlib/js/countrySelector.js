$(document).ready(function() {
  if (typeof checkFeatureFlag !== 'undefined' && !checkFeatureFlag('countrySelector')) {
    return;
  }
  var $form = $(".det31-country-selector-dropdown form");
  var $accordion = $(".det31-country-selector-list");
  $form.find("select.regions").val('0').trigger('change.select2');
  if ($form.length) {
    $form.find("select.regions").val('0').trigger('change.select2');
    dropdownList();
  }
  if($accordion.length) {
    accordionList();
  }

  function dropdownList() {
    var $regionField = $form.find("select.regions");
    var $countryFields = $form.find("select.countries");
    var $defaultField = $countryFields.filter(".disabled");
    var $goButton = $form.find(".c-button");

    function clearFields() {
        $countryFields.find("option:selected").prop("selected", false);
        $countryFields.trigger("change.select2");
        $form.find(".select2-selection__rendered.flag-icon").removeClass().addClass("select2-selection__rendered");
    }

    // Show corresponding countries dropdown once a region is selected
    $regionField.on("change", function() {
        var id = $(this).val();
        var $targetField;

        if (id === '0') {
            $targetField = $defaultField;
            $goButton.prop("disabled", true);
        } else {
            $targetField = $countryFields.filter(`#region-${id}`);
        }
        $countryFields.removeClass("active");
        $targetField.addClass("active");
        $goButton.prop("disabled", true);
        clearFields();
    });

    // When a country/language is selected:
    $countryFields.on("change", function() {
        var $this = $(this);
        var languageHref = $this.val();
        var $chosen = $this.next(".select2-container");
        var $chosenTitle = $chosen.children(".select2-selection__rendered");
        var countryCode = $this.find(":selected").data("country-code");
        var idCountry = $this.val();
        if (idCountry === 0) {
            $goButton.prop("disabled", true);
            console.log(idCountry);
        } else {
            $goButton.prop("disabled", false);
            console.log(idCountry);
        }
        // Update the class of the chosen title so that it has the correct flag icon:
        var newClass = `select2-container flag-icon flag-icon-${countryCode}`;
        $chosenTitle.removeClass(); // Remove all classes
        $chosenTitle.addClass(newClass);

        // Update the button to take you to the correct address
        $goButton.off("click");
        $goButton.on("click", function() {
            window.location.href = languageHref;
        });
        //$goButton.prop("disabled", false);

    });
    // Init
    $(".det31-country-selector-dropdown form > fieldset .select2-results .select2-results__options > li").addClass("active-result");
    $form.on("submit", function(e) {
        e.preventDefault();
    });
    $goButton.prop("disabled", true);
  }

    //accordion js
  function accordionList () {
    $(".det01-accordion-list").on("click", "h3", function(e) {
        e.preventDefault();
        var $this = $(this);
        var $parentItem = $this.parent(".item");
        $parentItem.toggleClass("active");
        $parentItem.siblings(".active").removeClass("active");
    });
  }
});
