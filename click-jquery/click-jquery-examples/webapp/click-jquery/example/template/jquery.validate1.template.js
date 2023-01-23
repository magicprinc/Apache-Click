(function() {
// prepare the form when the DOM is ready
var formOptions;
var valid=true;

jQuery(document).ready(function() {

    var container = jQuery('#errors');

    formOptions = {
        beforeSubmit:  preSubmit,   // pre-submit callback
        dataType:      'xml',   // must be 'xml' to work with JQuery Taconite support
        complete:      postSubmit,   // post-submit callback
        data: {"X-Requested-With" : "XMLHttpRequest"}
    };

    // bind the form validation
	  jQuery("[class*=validate]").validationEngine({
  		success :  function(){valid = true},
  		failure : function(){valid = false}
	  })

    // bind the form submit event
    jQuery('$cssSelector').ajaxForm(formOptions);
});

// pre-submit callback
function preSubmit(formData, jqForm, options) {
    if (!valid) {
        return false;
    }

    // Show Ajax load indicator
    #if($showBusyIndicator == "true")
       #if($busyIndicatorTarget) jQuery('$busyIndicatorTarget').block({ $!{busyIndicatorOptions} });
       #else jQuery.blockUI({ $!{busyIndicatorOptions} });
       #end
    #end
    return true;
}

function postSubmit(xhr, statusText) {
    // Hide Ajax load indicator
    #if($showBusyIndicator == "true")
      #if($busyIndicatorTarget) jQuery('$busyIndicatorTarget').unblock();
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
    // bind the form validation
	  jQuery("[class*=validate]").validationEngine({
  		success :  function(){valid = true},
  		failure : function(){valid = false}
	  })

    // bind the form submit event
    jQuery('$cssSelector').ajaxForm(formOptions);
}

function onError(statusText, xhr, errorThrown) {
  Click.jq.log('handleError: ', 'textStatus=', statusText, ', errorThrown=', errorThrown, ', xhr.responseText=', xhr.responseText);
  alert('Sorry, an error occurred');
}
})();
