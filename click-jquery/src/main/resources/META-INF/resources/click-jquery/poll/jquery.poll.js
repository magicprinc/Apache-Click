/**
* poll - jQuery plugin for dynamic polling.
*
* Project home page:
* http://launchpad.net/jquery.poll
* http://canhas.biz/2010/01/10/jquery-poll/
*
* Inspired by:
* http://enfranchisedmind.com/blog/posts/jquery-periodicalupdater-ajax-polling/
*
* Copyright (c) 2009 by Ethan Fremen <mindlace@canhas.biz>
*
* Licensed under the MIT license:
* http://www.opensource.org/licenses/mit-license.php
*
* Version: 0.2.2
* Changes: Correct handling of data option
*
* Click enhancements: pass Ajax context reference, enhanced logging, fixed start error,
* guard against duplicate starts, added support for max_polls and stop_after_failures
*/
/*
Usage:
 All poll options are optional.
var pollopts = {
  //A random name will be assigned if you don't assign one
  name: 'name-of-poll',
  //minimum wait between calls in msec, defaults to 5000
  min_wait: 6000,
  //maximum wait between calls in msec, defaults to 30000
  max_wait: 12000,
  //amount to multiply the wait by if the data is unchanged. defaults to 2
  wait_multiplier: 4,
  // log poll events to console.log , default true
  doLog: false,
  // A function that decides how to adjust the wait period after each call.
  // Should return an integer
  adjustWait: function(xhr, textStatus, current_wait) { ... },
  // maximum number of calls after which the poller stops. 0 or less = infinite. defaults to 0
  this.max_polls=2,
  // automatically stop requests after this many failed polls. 0 = disabled. defaults to 0
  this.stop_after_failures=2
};
// simplest way to start polling.
// ajaxopts are defined as per http://docs.jquery.com/Ajax/jquery.ajax#toptions
var mypoll = $.poll(ajaxopts);
// start polling with custom poll options
var mypoll = $.poll(ajaxopts,pollopts);
// get a poll object, but don't start polling
var mypoll = $.poll(ajaxopts,pollopts,true);
// can also do $.poll(ajaxopts,null,true);
// stop the poll
mypoll.stop = true;
// make a one-time change to the poll wait interval
mypoll.setWait(500);
// change the ajax options for the live poll
mypoll.setAjaxOptions(ajaxopts);
*/
(function($) {
    var randomString = function(numchars) {
        var chars = "0123456789ABCDEFGHJKMNPQRSTUVWXYZabcdefghkmnpqrstuvwxyz";
        var randomstring = '';
        for (var i=0; i<numchars; i++) {
            var rnum = Math.floor(Math.random() * chars.length);
            randomstring += chars.substring(rnum,rnum+1);
        }
        return randomstring;
    };

    var Poller = function(ajaxopt, pollopt) {
        this.setAjaxOptions(ajaxopt);
        this.name = (typeof(pollopt.name) != 'undefined') ? pollopt.name : randomString(4);
        this.min_wait = (typeof(pollopt.min_wait) != 'undefined') ? pollopt.min_wait : 5000;
        this.max_wait = (typeof(pollopt.max_wait) != 'undefined') ? pollopt.max_wait : 30000;
        this.current_wait = this.min_wait;
        this.wait_multiplier = (typeof(pollopt.wait_multiplier) != 'undefined') ? pollopt.wait_multiplier: 2;
        this.stop = false;
        this.doLog = (typeof(pollopt.doLog) != 'undefined') ? pollopt.doLog : true;
        this.current_poll = false;
        if (typeof(pollopt.adjustWait) != 'undefined') {
            this.adjustWait = pollopt.adjustWait;
        }
        this.max_polls=(typeof(pollopt.max_polls) != 'undefined') ? pollopt.max_polls : 0;
        this.stop_after_failures=(typeof(pollopt.stop_after_failures) != 'undefined') ? pollopt.stop_after_failures : 0;
        this.calls=0; // number of calls made
        this.fails=0; // number of failed calls made
    };

    Poller.prototype.log = function() {
        if (this.doLog && window.console && window.console.log){
            window.console.log('[poll:'+this.name+'] ' + [].join.call(arguments,''));
        }
    };

    /**
     * Set the ajax options, copying the default ajaxSettings so we can override
     * the 'complete' callback as necesary, and to allow data to be a callback.
     * You can adjust settings of a "live" poll with this function.
     */
     Poller.prototype.setAjaxOptions = function(options) {
         if (typeof(options) == 'string') {
             options = { url: options };
         }
         if (this.settings) {
             oldsettings = this.settings;
         } else {
             oldsettings = {};
         }
         var newSettings = $.extend(true, {}, $.ajaxSettings, oldsettings);
         if (options) {
             $.extend(true, newSettings, options);
         }
         if (typeof(newSettings.data) != 'undefined') {
             if ($.isFunction(newSettings.data)) {
                 this.dataCallback = newSettings.data;
             } else {
                 var userdata = newSettings.data;
                 this.dataCallback = function() {
                      return userdata;
                  };
             }
             delete newSettings.data;
         }
         if ($.isFunction(newSettings.complete)) {
             this.userComplete = newSettings.complete;
             delete newSettings.complete;
         }
         this.settings = newSettings;
     };
     /**
      * Check to see if we should really fetch now.
      * This is where we setTimeout for repeated calling, so that
      * the wait period may be manipulated by other javascript
      * using the setWait() function.
      */
     Poller.prototype.fetch = function() {
         var self = this;
         if (!this.stop) {
             if (!this.current_poll) {
                 this.fetchNow();
             } else {
                 this.log('last XHR not complete, skipping fetch');
             }
             var anotherFetch = function() {
                 self.fetch();
             }
             this.log('next check in ',this.current_wait,'ms');
             this.current_poll = setTimeout(anotherFetch, this.current_wait);
         } else {
             this.log('stopped.');
             this.calls=0;
             this.fails=0;
         }
     };

     /**
      * Actually perform the ajax request.
      */
     Poller.prototype.fetchNow = function() {
         var options = {
             ifModified: true,
             context: this,
             complete: this.completeCallback,
             data: this.dataCallback()
         };
         var opts = $.extend(true, options, this.settings);
         if(this.max_polls <= 0){
            $.ajax(opts);
         } else if(this.max_polls > 0 && this.calls < this.max_polls){
            $.ajax(opts);
            this.calls++;
            if(this.calls >= this.max_polls){
              this.log('stopping - max number of polls reached: ', this.max_polls);
              this.stop=true;
            }
         }
     };

     /**
      * Provide a custom complete callback so we can continue to poll.
      * nb. using self since this is a callback - self is set to this
      * in fetch().
      */
     Poller.prototype.completeCallback = function(xhr, textStatus) {
         this.log('complete: ',textStatus);
         this.current_poll = false;
         if ($.isFunction(this.adjustWait)) {
             this.current_wait = this.adjustWait(xhr, textStatus, this.current_wait);
         } else if (textStatus != 'success') {
             // increase the wait for server error, timeout, notmodified, or parser error.
             var next_wait = this.current_wait * this.wait_multiplier;
             if (next_wait <= this.max_wait) {
                 this.current_wait = next_wait;
             } else {
                 this.current_wait = this.max_wait;
             }
             if ($.isFunction(this.userError)) {
                 this.userError(xhr, textStatus);
             }
         }
         if(textStatus == 'success'){
           this.fails=0;
         } else {
           if(this.stop_after_failures > 0){
             this.fails++;
             if(this.fails >= this.stop_after_failures){
               this.log('stopping - max number of poll failures reached: ', this.stop_after_failures);
               this.stop=true;
             }
           }
         }
         if ($.isFunction(this.userComplete)) {
             this.userComplete(xhr, textStatus);
         }
     };

     /**
      * This function allows you to change the polling period on a one-time basis.
      */
     Poller.prototype.setWait = function(wait) {
         // if the time is set to less than the current wait
         // and we have an active timer, clear it.
         if (wait < this.current_wait && this.current_poll) {
             clearTimeout(this.current_poll);
             this.current_poll = false;
         }
         if (wait) { this.current_wait = wait; }
         this.fetch();
     };

    $.poll = function(ajaxopt, pollopt, dontstart) {
        if (typeof(pollopt) == 'undefined' || pollopt == null) { pollopt = {}; }
        var poller = new Poller(ajaxopt, pollopt);
        if (!dontstart) {
            poller.fetch();
        }
        return poller;
    };

 })(jQuery);