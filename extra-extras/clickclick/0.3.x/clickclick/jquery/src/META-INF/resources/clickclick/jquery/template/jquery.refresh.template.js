// Ensure Click namespace exists
if ( typeof Click == 'undefined' )
    Click = {};

// Ensure Click.refresh namespace exist
if ( typeof Click.refresh == 'undefined' )
    Click.refresh = {
        queue : {},
        start : function(refreshId, interval){
            Click.refresh.stop(refreshId);
            var holder = setInterval(Click.refresh[refreshId], interval);
            Click.refresh.queue[refreshId] = holder;
        },
        stop : function(refreshId){
            var curr = Click.refresh.queue[refreshId];
            if(curr) {
                clearInterval(curr);
            }
        },
        update : function(refreshId, interval){
            start(refreshId, interval);
        }
    };

// Generate a fuction that is named according to a unique ID. Each template can
// have a new method for the given ID
Click.refresh.$refreshId = function() {
     // Retrieve the underlying element
    var target = jQuery('$selector')[0];

    // Extract parameters from link href
    var params = Click.params(target);

    // Add any parameters passed in from the Page
    var defaultParams = Click.urlPairs('$!{parameters}');
    if (Click.isNotBlank(defaultParams)) {
        params.push(defaultParams);
    }

    // Add the Control attributes 'name', 'value' and 'id' as parameters
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
          } catch (e) { alert("Network error"); }
        }
      }
    });
    // Return false to override default event behavior
    return false;
}

jQuery(document).ready(function(){
  var interval = $interval;
  if(interval > 0) {
    Click.refresh.start('$refreshId', $interval);
  }
});
