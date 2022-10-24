(function() {
// prepare the form when the DOM is ready
var formOptions;
var validationOptions;
var valid=true;

jQuery(document).ready(function() {

    var container = jQuery('#errors');

     validationOptions = {
         errorClass: "jq-error", // specify custom error class
         errorPlacement: function(error, element) { // radio error label should be placed next to first radio
           if ( element.is(":radio") ) {
             error.appendTo( element.next() )
           } else {
               error.insertAfter(element);
           }
         },

      // To render error messages inline comment the two lines below
      errorLabelContainer: "#errors ul",
      wrapper: "li",

      // Don't highlight radio or checkboxes, it looks strange in IE8
      highlight: function(element, errorClass) {
          if(jQuery(element).is(":radio,:checkbox")){
                 jQuery(element).removeClass(errorClass);
             } else {
                 $( element ).addClass( errorClass );
             }
         },

      // enable Form submission
      submitHandler: function(form) {
          valid = true;
			},

      rules: {
          ${rules} // render field rules
      },
      messages: {
          ${messages} // render rule messages
      },

      // disable Form submission
      invalidHandler: function(form, validator) {
          valid = false;
      }
    }

    formOptions = {
        beforeSubmit:  preSubmit,   // pre-submit callback
        dataType:      'xml',   // must be 'xml' to work with JQuery Taconite support
        complete:      postSubmit,   // post-submit callback
        data: {"X-Requested-With" : "XMLHttpRequest"} // add the special "X-Requested-With" parameter
        // which instructs Click to handle the request is an Ajax request. We do this because
        // if a FileField is present in the Form, the JQuery Form plugin uses an
        // IFrame to submit that data instead of Ajax. Since an IFrame submit is
        // just a normal request, Click won't process the request as Ajax and
        // simply return the Page template. By passing "X-Requested-With" Click handles
        // the request as an Ajax request
    };

    ${validators} // render custom validators

    jQuery("$selector").validate(validationOptions);

    // bind the form submit event
    jQuery('$selector').ajaxForm(formOptions);
});

// pre-submit callback
function preSubmit(formData, jqForm, options) {
    if (!valid) {
        return false;
    }
    // Show Ajax load indicator
    #if($showIndicator == "true")
       #if($indicatorTarget) jQuery('$indicatorTarget').block({ $!{indicatorOptions} });
       #else jQuery.blockUI({ $!{indicatorOptions} });
       #end
    #end
    return true;
}

function postSubmit(xhr, statusText) {
    // Hide Ajax load indicator
    #if($showIndicator == "true")
      #if($indicatorTarget) jQuery('$indicatorTarget').unblock();
      #else jQuery.unblockUI();
      #end
    #end
    if (statusText == "success") {
        var data = xhr.responseText;
        onSuccess(data, statusText, xhr);
    } else {
        onError(statusText, xhr);
    }
}

function onSuccess(responseData, statusText, xhr) {
    // bind the form submit event
    jQuery("$selector").validate(validationOptions);
    jQuery('$selector').ajaxForm(formOptions);
}

function onError(statusText, xhr) {
    alert('$errorMessage' #if ($productionMode != "true") + '\n\n' + xhr.responseText #end);
}
})();
