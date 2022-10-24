/**
 * Provides jQuery Taconite public support for Click applications. This file
 * includes two jQuery plugins namely:
 *
 * 1 - jQuery Taconite plugin
 * 2 - jQuery LiveQuery plugin (NOTE: once jQuery.live() supports all events, this plugin can be removed)
 *
 * This file also includes Click specific functions to enable Click applications
 * to interact seamlessly with jQuery Taconite.
 */

/*
 * jQuery Taconite plugin - A port of the Taconite framework by Ryan Asleson and
 *     Nathaniel T. Schutta: http://taconite.sourceforge.net/
 *
 * Examples and documentation at: http://malsup.com/jquery/taconite/
 * Copyright (c) 2007-2009 M. Alsup
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * Thanks to Kenton Simpson for contributing many good ideas!
 *
 * $Id: jquery.taconite.js 2457 2007-07-23 02:43:46Z malsup $
 * @version: 3.06  26-MAY-2009
 * @requires jQuery v1.2.6 or later
 */

(function($) {

$.taconite = function(xml) {processDoc(xml);};

$.taconite.debug = 0;  // set to true to enable debug logging to Firebug
$.taconite.version = '3.06';
$.taconite.defaults = {
    cdataWrap: 'div'
};

// add 'replace' and 'replaceContent' plugins (conditionally)
if (typeof $.fn.replace == 'undefined')
    $.fn.replace = function(a) {return this.after(a).remove();};
if (typeof $.fn.replaceContent == 'undefined')
    $.fn.replaceContent = function(a) {return this.empty().append(a);};

$.expr[':'].taconiteTag = function(a) {return a.taconiteTag === 1;};

$.taconite._httpData = $.httpData; // original jQuery httpData function

// replace jQuery's httpData method
$.httpData = $.taconite.detect = function(xhr, type) {
    var ct = xhr.getResponseHeader('content-type');
    if ($.taconite.debug) {
        log('[AJAX response] content-type: ', ct, ';  status: ', xhr.status, ' ', xhr.statusText, ';  has responseXML: ', xhr.responseXML != null);
        log('type: ' + type);
        log('responseXML: ' + xhr.responseXML);
    }
    var data = $.taconite._httpData(xhr, type); // call original method
    if (data && data.documentElement) {
		$.taconite(data);
    }
    else {
        if(data.length < 2000){
            log('jQuery core httpData returned: ' + data);
        } else {
            log('Returned data is very large: ' + data.length + ' characters. Trimming logged result: ' + data.substring(0, 2000));
        }
        log('httpData: response is not XML (or not "valid" XML)');
    }
    return data;
};

// allow auto-detection to be enabled/disabled on-demand
$.taconite.enableAutoDetection = function(b) {
    $.httpData = b ? $.taconite.detect : $.taconite._httpData;
};

var logCount = 0;
function log() {
    if (!$.taconite.debug || !window.console || !window.console.log) return;
    if (!logCount++)
        log('Plugin Version: ' + $.taconite.version);
    window.console.log('[taconite] ' + [].join.call(arguments,''));
};

function processDoc(xml) {
    var status = true, ex;
    try {
		if (typeof xml == 'string')
			xml = convert(xml);
		if (!xml) {
			log('$.taconite invoked without valid document; nothing to process');
			return false;
		}

		var root = xml.documentElement.tagName;
		log('XML document root: ', root);

		var taconiteDoc = $('taconite', xml)[0];

		if (!taconiteDoc) {
			log('document does not contain <taconite> element; nothing to process');
			return false;
		}

		$.event.trigger('taconite-begin-notify', [taconiteDoc])
        status = go(taconiteDoc);
    } catch(e) {
        status = ex = e;
    }
    $.event.trigger('taconite-complete-notify', [xml, !!status, status === true ? null : status]);
    if (ex) throw ex;
};

// convert string to xml document
function convert(s) {
	var doc;
	log('attempting string to document conversion');
	try {
		if (window.DOMParser) {
			var parser = new DOMParser();
			doc = parser.parseFromString(s, 'text/xml');
		}
		else {
			doc = $("<xml>")[0];
			doc.async = 'false';
			doc.loadXML(s);
		}
	}
	catch(e) {
		if (window.console && window.console.error)
			window.console.error('[taconite] ERROR parsing XML string for conversion: ' + e);
		throw e;
	}
	var ok = doc && doc.documentElement && doc.documentElement.tagName != 'parsererror';
	log('conversion ', ok ? 'successful!' : 'FAILED');
	return doc;
};


function go(xml) {
    var trimHash = {wrap: 1};

    try {
        var t = new Date().getTime();
        // process the document
        process(xml.childNodes);
        $.taconite.lastTime = (new Date().getTime()) - t;
        log('time to process response: ' + $.taconite.lastTime + 'ms');
    } catch(e) {
        if (window.console && window.console.error)
            window.console.error('[taconite] ERROR processing document: ' + e);
        throw e;
    }
    return true;

// process the taconite commands
    function process(commands) {
        var doPostProcess = 0;
        for(var i=0; i < commands.length; i++) {
            if (commands[i].nodeType != 1)
                continue; // commands are elements
            var cmdNode = commands[i], cmd = cmdNode.tagName;
            if (cmd == 'eval') {
                var js = (cmdNode.firstChild ? cmdNode.firstChild.nodeValue : null);
                log('invoking "eval" command: ', js);
                if (js) $.globalEval(js);
                continue;
            }
            //*** CLICK STARTS
            if (cmd == 'addHeader') {
                log('invoking "addHeader" command');
                for (var j=0; j < cmdNode.childNodes.length; j++) {
                    var child = cmdNode.childNodes[j];
                    $().addHeader(child);
                }
                continue;
            }
            if (cmd == 'custom') {
                continue;
            }
            //*** CLICK ENDS
            var q = cmdNode.getAttribute('select');
            var jq = $(q);
            if (!jq[0]) {
                log('No matching targets for selector: ', q);
                continue;
            }
            var cdataWrap = cmdNode.getAttribute('cdataWrap') || $.taconite.defaults.cdataWrap;

            var a = [];
            if (cmdNode.childNodes.length > 0) {
                doPostProcess = 1;
                for (var j=0,els=[]; j < cmdNode.childNodes.length; j++)
                    els[j] = createNode(cmdNode.childNodes[j]);
                a.push(trimHash[cmd] ? cleanse(els) : els);
            }

            // remain backward compat with pre 2.0.9 versions
            var n = cmdNode.getAttribute('name');
            var v = cmdNode.getAttribute('value');
            if (n !== null) a.push(n);
            if (v !== null) a.push(v);

            // @since: 2.0.9: support arg1, arg2, arg3...
            for (var j=1; true; j++) {
                v = cmdNode.getAttribute('arg'+j);
                if (v === null)
                    break;
                a.push(v);
            }

            if ($.taconite.debug) {
                var arg = els ? '...' : a.join(',');
                log("invoking command: $('", q, "').", cmd, '('+ arg +')');
            }
            jq[cmd].apply(jq,a);

            // Unwrap cdataWrap element contents
            $(".taconiteUnwrap").each(function() {
                var jel = $(this);
                jel.after(jel.html()).remove();
            });
        }
        // apply dynamic fixes
        if (doPostProcess)
            postProcess();

        function postProcess() {
            if ($.browser.mozilla) return;
            // post processing fixes go here; currently there is only one:
            // fix1: opera, IE6, Safari/Win don't maintain selected options in all cases (thanks to Karel Fučík for this!)
            $('select:taconiteTag').each(function() {
                var sel = this;
                $('option:taconiteTag', this).each(function() {
                    this.setAttribute('selected','selected');
                    this.taconiteTag = null;
                    if (sel.type == 'select-one') {
                        var idx = $('option',sel).index(this);
                        sel.selectedIndex = idx;
                    }
                });
                this.taconiteTag = null;
            });
        };

        function cleanse(els) {
            for (var i=0, a=[]; i < els.length; i++)
                if (els[i].nodeType == 1) a.push(els[i]);
            return a;
        };

        function createNode(node) {
            var type = node.nodeType;
            if (type == 1) return createElement(node);
            if (type == 3) return fixTextNode(node.nodeValue);
            if (type == 4) return handleCDATA(node.nodeValue);
            return null;
        };

        function handleCDATA(s) {
            var el = document.createElement(cdataWrap);
            // Add class which indicates div must be unwrapped later
            el.className = "taconiteUnwrap";
            el.innerHTML = s;
            return el;
        };

        function fixTextNode(s) {
            if ($.browser.msie) s = s.replace(/\n/g, '\r').replace(/\s+/g, ' ');
            return document.createTextNode(s);
        };

        function createElement(node) {
            var e, tag = node.tagName.toLowerCase();
            // some elements in IE need to be created with attrs inline
            if ($.browser.msie) {
                var type = node.getAttribute('type');
                if (tag == 'table' || type == 'radio' || type == 'checkbox' || tag == 'button' ||
                    (tag == 'select' && node.getAttribute('multiple'))) {
                    e = document.createElement('<' + tag + ' ' + copyAttrs(null, node, true) + '>');
                }
            }
            if (!e) {
                e = document.createElement(tag);
                // copyAttrs(e, node, tag == 'option' && $.browser.safari);
                copyAttrs(e, node);
            }

            // IE fix; colspan must be explicitly set
            if ($.browser.msie && tag == 'td') {
                var colspan = node.getAttribute('colspan');
                if (colspan) e.colSpan = parseInt(colspan);
            }

            // IE fix; script tag not allowed to have children
            if($.browser.msie && !e.canHaveChildren) {
                if(node.childNodes.length > 0)
                    e.text = node.text;
            }
            else {
                for(var i=0, max=node.childNodes.length; i < max; i++) {
                    var child = createNode (node.childNodes[i]);
                    if(child) e.appendChild(child);
                }
            }
            if (! $.browser.mozilla) {
                if (tag == 'select' || (tag == 'option' && node.getAttribute('selected')))
                    e.taconiteTag = 1;
            }
            return e;
        };

        function copyAttrs(dest, src, inline) {
            for (var i=0, attr=''; i < src.attributes.length; i++) {
                var a = src.attributes[i], n = $.trim(a.name), v = $.trim(a.value);
                if (inline) attr += (n + '="' + v + '" ');
                else if (n == 'style') { // IE workaround
                    dest.style.cssText = v;
                    dest.setAttribute(n, v);
                }
                else {
                    // IE workaround for inline event handlers
                    if(n.toLowerCase().substring(0, 2) == 'on') {
                        if ($.browser.msie) {
                            eval("dest." + n.toLowerCase() + "=function(){" + v + "}");
                        } else {
                            dest.setAttribute(n, v);
                        }
                    } else {
                        $.attr(dest, n, v);
                    }
                }
            }
            return attr;
        };
    };
};

})(jQuery);


/* Copyright (c) 2007 Brandon Aaron (brandon.aaron@gmail.com || http://brandonaaron.net)
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *
 * Version: 1.0.2
 * Requires jQuery 1.1.3+
 * Docs: http://docs.jquery.com/Plugins/livequery
 */

(function($) {

$.extend($.fn, {
	livequery: function(type, fn, fn2) {
		var self = this, q;

		// Handle different call patterns
		if ($.isFunction(type))
			fn2 = fn, fn = type, type = undefined;

		// See if Live Query already exists
		$.each( $.livequery.queries, function(i, query) {
			if ( self.selector == query.selector && self.context == query.context &&
				type == query.type && (!fn || fn.$lqguid == query.fn.$lqguid) && (!fn2 || fn2.$lqguid == query.fn2.$lqguid) )
					// Found the query, exit the each loop
					return (q = query) && false;
		});

		// Create new Live Query if it wasn't found
		q = q || new $.livequery(this.selector, this.context, type, fn, fn2);

		// Make sure it is running
		q.stopped = false;

		// Run it
		$.livequery.run( q.id );

		// Contnue the chain
		return this;
	},

	expire: function(type, fn, fn2) {
		var self = this;

		// Handle different call patterns
		if ($.isFunction(type))
			fn2 = fn, fn = type, type = undefined;

		// Find the Live Query based on arguments and stop it
		$.each( $.livequery.queries, function(i, query) {
			if ( self.selector == query.selector && self.context == query.context &&
				(!type || type == query.type) && (!fn || fn.$lqguid == query.fn.$lqguid) && (!fn2 || fn2.$lqguid == query.fn2.$lqguid) && !this.stopped )
					$.livequery.stop(query.id);
		});

		// Continue the chain
		return this;
	}
});

$.livequery = function(selector, context, type, fn, fn2) {
	this.selector = selector;
	this.context  = context || document;
	this.type     = type;
	this.fn       = fn;
	this.fn2      = fn2;
	this.elements = [];
	this.stopped  = false;

	// The id is the index of the Live Query in $.livequery.queries
	this.id = $.livequery.queries.push(this)-1;

	// Mark the functions for matching later on
	fn.$lqguid = fn.$lqguid || $.livequery.guid++;
	if (fn2) fn2.$lqguid = fn2.$lqguid || $.livequery.guid++;

	// Return the Live Query
	return this;
};

$.livequery.prototype = {
	stop: function() {
		var query = this;

		if ( this.type )
			// Unbind all bound events
			this.elements.unbind(this.type, this.fn);
		else if (this.fn2)
			// Call the second function for all matched elements
			this.elements.each(function(i, el) {
				query.fn2.apply(el);
			});

		// Clear out matched elements
		this.elements = [];

		// Stop the Live Query from running until restarted
		this.stopped = true;
	},

	run: function() {
		// Short-circuit if stopped
		if ( this.stopped ) return;
		var query = this;

		var oEls = this.elements,
			els  = $(this.selector, this.context),
			nEls = els.not(oEls);

		// Set elements to the latest set of matched elements
		this.elements = els;

		if (this.type) {
			// Bind events to newly matched elements
			nEls.bind(this.type, this.fn);

			// Unbind events to elements no longer matched
			if (oEls.length > 0)
				$.each(oEls, function(i, el) {
					if ( $.inArray(el, els) < 0 )
						$.event.remove(el, query.type, query.fn);
				});
		}
		else {
			// Call the first function for newly matched elements
			nEls.each(function() {
				query.fn.apply(this);
			});

			// Call the second function for elements no longer matched
			if ( this.fn2 && oEls.length > 0 )
				$.each(oEls, function(i, el) {
					if ( $.inArray(el, els) < 0 )
						query.fn2.apply(el);
				});
		}
	}
};

$.extend($.livequery, {
	guid: 0,
	queries: [],
	queue: [],
	running: false,
	timeout: null,

	checkQueue: function() {
		if ( $.livequery.running && $.livequery.queue.length ) {
			var length = $.livequery.queue.length;
			// Run each Live Query currently in the queue
			while ( length-- )
				$.livequery.queries[ $.livequery.queue.shift() ].run();
		}
	},

	pause: function() {
		// Don't run anymore Live Queries until restarted
		$.livequery.running = false;
	},

	play: function() {
		// Restart Live Queries
		$.livequery.running = true;
		// Request a run of the Live Queries
		$.livequery.run();
	},

	registerPlugin: function() {
		$.each( arguments, function(i,n) {
			// Short-circuit if the method doesn't exist
			if (!$.fn[n]) return;

			// Save a reference to the original method
			var old = $.fn[n];

			// Create a new method
			$.fn[n] = function() {
				// Call the original method
				var r = old.apply(this, arguments);

				// Request a run of the Live Queries
				$.livequery.run();

				// Return the original methods result
				return r;
			}
		});
	},

	run: function(id) {
		if (id != undefined) {
			// Put the particular Live Query in the queue if it doesn't already exist
			if ( $.inArray(id, $.livequery.queue) < 0 )
				$.livequery.queue.push( id );
		}
		else
			// Put each Live Query in the queue if it doesn't already exist
			$.each( $.livequery.queries, function(id) {
				if ( $.inArray(id, $.livequery.queue) < 0 )
					$.livequery.queue.push( id );
			});

		// Clear timeout if it already exists
		if ($.livequery.timeout) clearTimeout($.livequery.timeout);
		// Create a timeout to check the queue and actually run the Live Queries
		$.livequery.timeout = setTimeout($.livequery.checkQueue, 20);
	},

	stop: function(id) {
		if (id != undefined)
			// Stop are particular Live Query
			$.livequery.queries[ id ].stop();
		else
			// Stop all Live Queries
			$.each( $.livequery.queries, function(id) {
				$.livequery.queries[ id ].stop();
			});
	}
});

// Register core DOM manipulation methods
$.livequery.registerPlugin('append', 'prepend', 'after', 'before', 'wrap', 'attr', 'removeAttr', 'addClass', 'removeClass', 'toggleClass', 'empty', 'remove');

// Run Live Queries when the Document is ready
$(function() {$.livequery.play();});


// Save a reference to the original init method
var init = $.prototype.init;

// Create a new init method that exposes two new properties: selector and context
$.prototype.init = function(a,c) {
	// Call the original init and save the result
	var r = init.apply(this, arguments);

	// Copy over properties if they exist already
	if (a && a.selector)
		r.context = a.context, r.selector = a.selector;

	// Set properties
	if ( typeof a == 'string' )
		r.context = c || document, r.selector = a;

	// Return the result
	return r;
};

// Give the init function the jQuery prototype for later instantiation (needed after Rev 4091)
$.prototype.init.prototype = $.prototype;

})(jQuery);

// *** CLICK STARTS

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

/**
 * This script integrates Click and JQuery Taconite plugin.
 *
 * To enable FireBug debugging use: Click.debug = true;
 */
(function($) {

    // Make sure that the Click namespace exists
    if( typeof Click == 'undefined' )
        Click = {};

    // Make sure that the Click.jquery namespace exists
    if( typeof Click.jquery == 'undefined' )
        Click.jquery = {};

    Click.debug = 0;  // set to true to enable debug logging to Firebug

    // Define method for logging
    Click.log = function() {
        if (!Click.debug || !window.console || !window.console.log) return;
        window.console.log('[click] ' + [].join.call(arguments,''));
    }

    // serialize the node children to xml
    Click.toXmlChildren = function(xmlNode) {
        if (xmlNode == null) {
            return "";
        }
        var text = "";
        for (var i = 0; i < xmlNode.childNodes.length; i++) {
          var thisNode = xmlNode.childNodes[i];
          text += Click.toXml(thisNode);
        }
        return text;
    }

    // serialize the node to xml
    Click.toXml = function(node) {
      if (typeof XMLSerializer != "undefined"){
        // Gecko-based browsers, Safari, Opera.
        return (new XMLSerializer()).serializeToString(node);
      }
      else if (node.xml) {
          // Internet Explorer.
          return node.xml;
      }
      else {
          Click.log('XML serialization is not supported');
      }
    };

    // parse given string to xml document
    Click.fromXml = function(xml) {
        var doc;
        try {
            if (window.ActiveXObject) {
                doc = $("<xml>")[0];
                doc.async = 'false';
                doc.loadXML(xml);
            } else {
                var parser = new DOMParser();
                doc = parser.parseFromString(xml, 'text/xml');
            }
        }
        catch(e) {
            if (window.console && window.console.error)
                window.console.error('[click] ERROR parsing XML string: ' + e);
            throw e;
        }
        var ok = doc && doc.documentElement && doc.documentElement.tagName != 'parsererror';
        Click.log('deserialization: ', ok ? 'successful!' : 'FAILED for : ' + xml);
        return doc;
    }

    // register addHeader function as JQuery plugin
    $.fn.addHeader = function(elems) {
        if(elems == null) {
            Click.log('the header elements to add to the page is null');
            return jQuery( [] );
        }
        if (isArray(elems)) {
            for (var j=0; j < elems.length; j++) {
                var elem = elems[j];
                Click.addHeader(elem);
            }
        } else {
            Click.addHeader(elems);
        }
        return this;
    };

    // Determines if the given object is an array or not.
    function isArray(o) {
        if (o.constructor == Array) {
            return true;
        }
        // TODO When object created in one window or frame is used in another
        // the check above might fail. We chould check for the availability of
        // specific functions: typeof myArray.sort == 'function'.
        // For now we return false
        return false;
    }

    /*
     * Add the given element to the page head section. If the given element is
     * a string it will be parsed into a DOM. This function ensures
     * the element is unique to the Page.
     *
     * Depending on the type of element (script,link,style) different rules
     * apply to determine if the element is unique or not. This rules are:
     *
     * #1. <link> - link uniqueness depends on the 'href' attribute
     * #2. <style> - style uniqueness depends on the 'id' attribute
     * #3. <script src='...'> - script with src attribute uniqueness depends on the 'src' attribute
     * #4. <script> - script without src attribute uniqueness depends on the 'id' attribute
     */
    Click.addHeader = function (element) {
        if (element == null) {
            Click.log('the header element to add to the page is null');
            return;
        }
        if (typeof element == 'string') {
            element = Click.fromXml(element).documentElement;
        }
        if(element.nodeType != 1) {
            return;
        }
        var text = Click.toXml(element);
        var tag = element.tagName.toLowerCase();

        // indicates whether the element should be added to head
        var append = false;

        // indicates whether a place holder script should added to head as
        // JQuery only evaluates scripts
        var shouldAppendScriptPlaceHolder = false;

        if (tag == 'link') {
            append = canAddLink(element);
        } else if (tag == 'style') {
            append = canAddStyle(element);
        } else if ((tag == 'script')) {
            var id = element.getAttribute("id");
            var src = element.getAttribute("src");
            // If no id nor src attribute is available to check against, only
            // evaluate the script
            if (Click.isBlank(id) && Click.isBlank(src)) {
                append = true;
            } else {
                append = canAddScript(element);
                shouldAppendScriptPlaceHolder = true;
            }
        }
        if(append) {
            Click.log('SUCCESS - '+element.nodeName+': '+text+' will be added to head');

            $('head').append(text);
            if (shouldAppendScriptPlaceHolder) {
                appendScriptPlaceHolder(element);
            }
        }
    }

    /**
     * Append an empty <script> element to the document <head> section.
     */
    function appendScriptPlaceHolder(element) {
        // If the script id or src attribute is defined we must ensure the
        // <script> tag gets added to head.
        // Reason we have to add <script> manually is because JQuery
        // does not append <script> tags to head, it only evaluates them
        var script = document.createElement("script");
        var id = element.getAttribute("id");
        var src = element.getAttribute("src");
        if (Click.isBlank(id) && Click.isBlank(src)) {
            // If no id or src attribute is defined there is no need for placeholder
            return;
        }
        if (Click.isNotBlank(id)) {
            script.id = element.getAttribute("id");
        }
        if (Click.isNotBlank(src)) {
            // GOTCHA_1
            // Note the fake source attribute below. We cannot set the real
            // src attribute as that would trigger the browser to download
            // the script a second time
            script.setAttribute("src_", element.getAttribute("src"));
        }
        script.type = "text/javascript";
        appendToScript(script, "<!-- This script act as a placeholder only. *** Added by Click *** -->");
        //alert(script.text);
        appendToHead(script);
    }

    // append the data to the given script in a browser compatible way
    function appendToScript(script, data) {
        if ( $.browser.msie ) {
            script.text = data;
        } else {
            script.appendChild( document.createTextNode( data ) );
        }
    }

    /**
     * Append the given element to the document <head> section.
     */
    function appendToHead(element) {
        var head = document.getElementsByTagName("head");
        if (head[0]) {
            head[0].appendChild(element);
        }
    }

    /**
     * Returns true if the given element exists in the document.
     */
    function containsElement(element) {
        var id = element.getAttribute("id");
        if (id) {
            // If an element exists with same id, the element exists
            if($('#'+id).length) {
                Click.log(element.nodeName+' with id: "'+id+'" already exists in the page');
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if the given script can be added to the document,
     * false otherwise.
     */
    function canAddScript(script) {
        if (containsElement(script)) return false;

        // When appending the script to head (see #GOTCHA_1 above), we cannot set the
        // src attribute as that would trigger the browser to download the script
        // a second time. Instead we set a fake src attribute. scriptExists
        // is aware of the fake src and will check against both src and src_
        // attributes. (This idea was adapted from Apache Wicket)
        var src = script.getAttribute("src");
        if(Click.isNotBlank(src)) {
            // Check for existence of both src and src_ properties
            if($('script[src$='+src+']').length || $('script[src_$='+src+']').length) {
                Click.log('script with src: "' + src + '" already exists in document');
                return false;
            }
        }
        return true;
    }

    /**
     * Return true if the given link is can be added to the document <head>,
     * false otherwise.
     */
    function canAddLink(link) {
        var href = link.getAttribute("href");
        if(Click.isBlank(href)) {
            Click.log('the link href attribute is not defined');
            return false;
        }
        if($('head link[href='+href+']').length) {
            Click.log('link with href: "' + href + '" already exists in document');
            return false;
        }
        return true;
    }

    /**
     * Return true if the given style can be added to the document,
     * false otherwise.
     */
    function canAddStyle(style) {
        if (containsElement(style)) return false;
        return true;
    }

    /**
     * Return true if the given value is null, "" or "undefined".
     */
    Click.isBlank = function(value) {
        if (value == null || value == "" || typeof(value) == "undefined")
            return true;
        else
            return false;
    }

    /**
     * Return true if the given value is not null, not "" and not "undefined".
     */
    Click.isNotBlank = function(value) {
        return !Click.isBlank(value);
    }

    /**
     * Return the url of the given Element or null if no url is available.
     *
     * Elements which provides a URL include: href, form, img. This function
     * also caters for onclick handlers using 'location.href'.
     */
    Click.url = function(el) {
        var attr;
        if (!el.attributes) {
            return null;
        }
        if(attr = el.attributes.href) {
        } else if(attr = el.attributes.src) {
        } else if(attr = el.attributes.action) {
        }
        if(attr) return attr.value;
        return null;
    };

    /**
     * Return the url path of the given Element as a String.
     *
     * Elements which provides a URL include: href, form, img
     */
    Click.path = function(el) {
        var url = Click.url(el);
        if(url) {
            var i = url.indexOf('?');
            return (i > 0) ? url.substring(0, i): url;
        }
        return '';
    };

    /**
     * Return the url parameters of the given Element as an array of key/value
     * pairs or null if no parameters can be extracted.
     *
     * Elements which provides a URL include: href, form, img
     */
    Click.params = function(el) {
        var url = Click.url(el);
        if(url) return Click.urlParams(url);
        return new Array();
    };

    /**
     * Return the url parameters as an array of key/value pairs or null
     * if no parameters can be extracted.
     */
    Click.urlParams = function(url) {
        if (url == null || url == '' || url == 'undefined') {
            return new Array();
        }
        url = unescape(url);
        var start = url.indexOf('?')
        if (start == -1) {
            return new Array();
        }
        url=url.substring(start + 1);
        return Click.urlPairs(url);
    };

    /**
     * Split the given url paramters into key/value pairs.
     */
    Click.urlPairs = function(urlParams) {
        var pairs=urlParams.split("&");
        var params = new Array();
        for (var i=0;i<pairs.length;i++) {
            var param = new Object();
            var pos = pairs[i].indexOf('=');
            if (pos >= 0) {
                param.name = pairs[i].substring(0,pos);
                param.value = pairs[i].substring(pos+1);
                params.push(param);
            }
        }
        return params;
    }

    /**
     * For the given Element, add the 'name', 'value' and 'id' attributes to
     * the given params as key/value pairs.
     * If excludeName is true, the name/value pair will be excluded
     */
    Click.addNameValueIdPairs = function(el, params, excludeName) {
        excludeName=excludeName||false;

        // Add attributes name, value and id as parameters
        var controlName = jQuery(el).attr('name');
        var controlValue = jQuery(el).attr('value');
        var controlId = jQuery(el).attr('id');

        if (Click.isBlank(controlValue)) {
            controlValue='';
        }

        if (Click.isNotBlank(controlName)) {
            if(!excludeName){
              params.push({
                'name':controlName,
                'value':controlValue
              });
            }
            if (controlName != controlId || excludeName) {
                if (Click.isNotBlank(controlId)) {
                    params.push({
                        'name':controlId,
                        'value':'1'});
                }
            }
        } else if (Click.isNotBlank(controlId)) {
            params.push({
                'name':controlId,
                'value':'1'
            });
        }
    }

    /**
     * Merge multiple callbacks into a single callback if they are invoked
     * within the given delay. Useful when binding to mouse or key events.
     * A delay of less than or equal to 0 executes immediately.
     *
     * Copied from here: http://unscriptable.com/index.php/2009/03/20/debouncing-javascript-methods/
     */
    Click.debounce=function(func, delay) {
        if(delay<= 0){
            return func;
        }
        var timeout;
        return function debounced () {
            var obj = this, args = arguments;
            function delayed () {
                if (delay > 0)
                    func.apply(obj, args);
                timeout = null;
            };

            if (timeout) {
                clearTimeout(timeout);
            } else if (delay <= 0) {
                func.apply(obj, args);
            }
            timeout = setTimeout(delayed, delay || 100);
        };
    }

// Close function and execute it, passing JQuery object as argument
}) (jQuery);
//*** CLICK ENDS