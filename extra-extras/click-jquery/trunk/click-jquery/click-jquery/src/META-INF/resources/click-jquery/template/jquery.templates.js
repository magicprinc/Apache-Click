/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//
// create closure
//
(function($) {

  // Ensure the Click and jq namespaces exist
  if(typeof Click == 'undefined')
    Click = {};
  if(typeof Click.jq == 'undefined')
    Click.jq = {};

  Click.jq.lang={
    noCssSelector:'ERROR: No CSS selector found. A CSS selector specifies which element to ajaxify',
    unknownError:'Unknown error',
    status404:'Requested URL not found.',
    status500:'Internel Server Error.',
    parseError:'Error.\nParsing Request failed.',
    timeoutError:'Request timed out.',
    defaultError:'Sorry, an error occurred'
  };

  //
  // Template 1 START
  //
  // A generic Ajax function
  Click.jq.ajaxTemplate = function(options) {
    if (!options.cssSelector) {
      alert(Click.jq.lang.noCssSelector);
      return;
    }

    var callback=ajaxTemplateCallback;
    if(options.event) {
      if(options.delay) {
        callback=Click.jq.debounce(ajaxTemplateCallback, options.delay);
      }
      $(options.cssSelector).die(options.event).live(options.event, options, callback);
    } else {
      // For non defined event use a custom 'domready' and force a manual 'trigger'
      $(options.cssSelector).bind('domready', options, callback).trigger('domready');
    }
  }

  function ajaxTemplateCallback(event) {
    // Default options
    var defaults = {
      type: 'GET',
      url: '#',
      data: null,
      timeout: 20000,
      timeoutRetryLimit: 0,
      showBusyIndicator: true,
      busyIndicatorTarget: null,
      busyIndicatorOptions: {}
    };

    var target=event.currentTarget;
    var opts=$.extend(defaults, event.data);

    // Extract parameters from element href/src/target
    var params=Click.jq.extractAjaxRequestData(target, event);

    // Add any parameters passed in from the Page
    var defaultParams=Click.jq.parameterStrToArray(opts.data);
    if (defaultParams && defaultParams!="") {
      // Copy array
      for (var i=0;i<defaultParams.length;i++) {
          params.push(defaultParams[i]);
      }
    }

    // Invoke the Ajax request
    $.ajax({
      type: opts.type,
      url: opts.url,
      data: params,
      timeout : opts.timeout,
      tryCount : 0,
      timeoutRetryLimit : opts.timeoutRetryLimit,
      retrying: false,
      beforeSend: function() {
        if (this.retrying) {
          // If we are still retrying request, keep blocking UI
          return;
        }
        // Show request indicator if enabled
        if(opts.showBusyIndicator){
          if(opts.busyIndicatorTarget){
            $(opts.busyIndicatorTarget).block(opts.busyIndicatorOptions);
          }else{
            $.blockUI(opts.busyIndicatorOptions);
          }
        }
      },
      complete: function() {
        if (this.retrying && this.tryCount <= this.timeoutRetryLimit) {
          // If we are still retrying request, keep blocking UI
          return;
        }

        // Hide request indicator if enabled
        if(opts.showBusyIndicator){
          if(opts.busyIndicatorTarget){
            $(opts.busyIndicatorTarget).unblock();
          }else{
            $.unblockUI();
          }
        }
      },
      success: function() {
        this.retrying=false;
      },
      error: function (xhr, textStatus, errorThrown) {
        if (textStatus == 'timeout') {
          this.retrying=true;
          this.tryCount++;
          if (this.tryCount <= this.timeoutRetryLimit) {
            // Invoking ajax(this) repeatedly leaks the data parameters and duplicates occur.
            // Here we reset data to null
            this.data = null;
            $.ajax(this);
            return;
          }
          return;
        }
        this.retrying=false;
        handleError(xhr, textStatus, errorThrown, opts);
      }
    });

    // cancel link and submit default event
    var tag=target.tagName;
    tag=tag?tag.toLowerCase():tag;
    if(tag=='a'||target.type=='submit'||target.type=='image') {
        event.preventDefault();
    }
  }
  //
  // Template 1 END
  //

  //
  // Template 2 START
  //
  // A generic Ajax form function
  Click.jq.ajaxFormTemplate = function(options) {
    var defaults = {
      type: 'POST',
      beforeSubmit: preSubmit,   // pre-submit callback
      dataType: 'xml',   // must be 'xml' to work with JQuery Taconite support
      complete: postSubmit,   // post-submit callback
      data: {"X-Requested-With" : "XMLHttpRequest"},
      jsValidate: false,
      jsValidateFunc: null,
      clearForm: false,
      resetForm: false,
      timeout: 20000,
      showBusyIndicator: true,
      busyIndicatorTarget: null,
      busyIndicatorOptions: {}
    };

    var opts=$.extend(defaults, options);

    if (!opts.cssSelector) {
      alert(Click.jq.lang.noCssSelector);
      return;
    }

    $(opts.cssSelector).ajaxForm(opts);

    function preSubmit(formData, jqForm, options) {
      if(opts.jsValidate) {
        if(!opts.jsValidateFunc()) return false;
      }
      // Show request indicator if enabled
      if(opts.showBusyIndicator){
        if(opts.busyIndicatorTarget){
          $(opts.busyIndicatorTarget).block(opts.busyIndicatorOptions);
        }else{
          $.blockUI(opts.busyIndicatorOptions);
        }
      }
      return true;
    }

    function postSubmit(xhr, statusText) {
      // Hide request indicator if enabled
      if(opts.showBusyIndicator){
        if(opts.busyIndicatorTarget){
          $(opts.busyIndicatorTarget).unblock();
        }else{
          $.unblockUI();
        }
      }

      if (statusText == "success") {
        // File upload uses an IFrame, so if no XHR was used check for errors in response
        if(!xhr.readyState && xhr.responseText){
          // Check for error id within first 30 characters
          if(xhr.responseText.lastIndexOf('id="errorReport"', 30)>=0){
            // Initialize xhr state for proper Ajax error handling
            xhr.readyState=4; // set readyState to DONE
            xhr.status=1;
            error(xhr, statusText, null);
            return;
          }
        }
        var data = xhr.responseText;
        success(data, statusText, xhr);
      } else {
        // TODO test file upload errors
        error(xhr, statusText, null);
      }
    }

    function success(responseData, statusText, xhr) {
      // Clear errors. We assume Taconite is used to update the DOM
      if(opts.resetForm||opts.clearForm){
        $(opts.cssSelector+"-errorsDiv").empty();
        $(opts.cssSelector+"-errorsTr").css('display', 'none');
        $(opts.cssSelector+' .error').removeClass('error');
      }
    }

    function error(xhr, textStatus, errorThrown) {
      handleError(xhr, textStatus, errorThrown, opts);
    }
  }
  //
  // Template 2 END
  //

  //
  // Template 3 START
  //
  Click.jq.autoCompleteTemplate = function(options) {
    var defaults = {
    };

    var opts=$.extend(defaults, options);

    if (!opts.cssSelector) {
      alert(Click.jq.lang.noCssSelector);
      return;
    }
    var url = opts.url;
    if(opts.data){
      url=url+'?'+opts.data;
    }
    $(opts.cssSelector).unautocomplete().autocomplete(url, opts.options);
  }
  //
  // Template 3 END
  //

  //
  // Template 4 START
  //
  Click.jq.pollTemplate = function(ajaxOptions, pollOptions) {

    var ajaxDefaults = {
      type: 'GET',
      url: '#'
    };
    var ajaxOpts=$.extend(ajaxDefaults, ajaxOptions);

    var pollDefaults = {
      doLog: Click.jq.debug,
      auto_start: true
    }

    var pollOpts=$.extend(pollDefaults, pollOptions);

    // Extract parameters from element href/src/target
    if(ajaxOpts.cssSelector){
      var target = $(ajaxOpts.cssSelector)[0];
      var params=Click.jq.extractAjaxRequestData(target);
      // Add any parameters passed in from the Page
      var defaultParams=Click.jq.parameterStrToArray(ajaxOpts.data);
      if (defaultParams && defaultParams!="") {
        // Copy array
        for (var i=0;i<defaultParams.length;i++) {
          params.push(defaultParams[i]);
        }
      }
      ajaxOpts.data=params;
    }

    var poll=$.poll(ajaxOpts, pollOpts, !pollOpts.auto_start);
    if(!pollOpts.auto_start) poll.stop=true;

    // Store the poll reference
    Click.jq.polls.add(poll);
  }

  // Polls holder and API for accessing polls
  Click.jq.polls={
    holder:{},
    get: function(name){
      return this.holder[name];
    },
    add: function(poll){
      this.holder[poll.name]=poll;
    },
    remove: function(name){
      delete this.holder[name];
    },
    stop: function(name){
      var poll=this.get(name);
      if(poll) {
        poll.stop=true;
        poll.log('stopping');
      } else {
        poll.log("no poll with the name '", name, "' found");
      }
    },
    start: function(name, wait){
      var poll=this.get(name);
      if(poll){
        if(!poll.stop){
            poll.log('poll has already started');
            return;
        }
        poll.stop=false;
        if(wait) {
          poll.log('starting poll with new wait time of ', wait, 'ms');
          poll.setWait(wait);
        } else{
          poll.log('starting poll');
          poll.fetch();
        }
      } else {
        poll.log("no poll with the name '", name, "' found");
      }
    }
  }

  //
  // Template 4 END
  //

  function handleError(xhr, textStatus, errorThrown, opts) {
    if(xhr.readyState==4){ // 4 == DONE
      try{
        Click.jq.log('handleError: ', 'textStatus=', textStatus, ', xhr.status=', xhr.status, ', errorThrown=', errorThrown, ', xhr.responseText=', xhr.responseText);

        if(xhr.status==0){
            // Might be offline. Ignore
        }else if(xhr.status==404){
          alert(Click.jq.lang.status404);
        }else if(xhr.status==500){
          alert(Click.jq.lang.status500);
        }else if(textStatus=='parsererror'){
          alert(Click.jq.lang.parseError);
        }else if(textStatus=='timeout'){
          alert(Click.jq.lang.timeoutError);
        }else{
          alert(Click.jq.lang.defaultError);
        }
      }catch(e){
        Click.jq.log(e);
        alert(Click.jq.lang.unknownError);
      }
    }
  }

//
// end of closure
//
})(jQuery);