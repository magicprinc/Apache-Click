(function($) {
    // Ensure Click namespace exists
    if ( typeof Click == 'undefined' )
        Click = {};

    var intervalId = null;

    if ( typeof Click.progress == 'undefined' ) {
        Click.progress = {
            start : function() {
                intervalId = setInterval(Click.progress.impl, 100);
            },
            stop : function() {
                if (intervalId) {
                    clearInterval(intervalId);
                }
            }
        }
    }

    Click.progress.impl = function() {
        // Retrieve the underlying element
        var target = jQuery('$selector')[0];
        if (!target) {
            stop();
            return;
        }

        // Add the Control attributes 'name', 'value' and 'id' as parameters
        var params = new Array();
        Click.addNameValueIdPairs(target, params);
        // Invoke the Ajax request
        jQuery.ajax({
            type: '$!{type}',
            url: '$!{url}',
            data: params,
            error: function (xhr, textStatus, errorThrown) {
                if(xhr.readyState == 4) {
                    try {
                        if(xhr.status != 0) {
                            alert('$errorMessage' #if ($productionMode != "true") + '\n\n' + xhr.responseText #end);
                        }
                    } catch (e) {
                        alert("Network error");
                    }
                }
            }
        });
    }
})(jQuery);
