window.addEvent('domready', function(){ 
  $('myForm').addEvent('submit', function(e) {
	/**
	 * Prevent the submit event
	 */
	new Event(e).stop();
 
	/**
	 * This empties the log and shows the spinning indicator
	 */
	var log = $('response').empty().addClass('ajax-loading');
 
	/**
	 * load takes care of encoding and returns the Ajax instance.
	 * onComplete removes the spinner from the log.
	 */
        this.set('load', {
            url: this.get("action"),
            method: this.get("method"),
            update: log,
            onComplete: function() {
                log.removeClass('ajax-loading');
            }
        });
        this.load(); 
  });
});
