/*!
 * jQuery Taconite plugin - A port of the Taconite framework by Ryan Asleson and
 *     Nathaniel T. Schutta: http://taconite.sourceforge.net/
 *
 * Examples and documentation at: http://malsup.com/jquery/taconite/
 * Copyright (c) 2007-2011 M. Alsup
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * Thanks to Kenton Simpson for contributing many good ideas!
 *
 * @version: 3.64  16-JUN-2011
 * @requires jQuery v1.3.2 or later
 */

(function($) {
var version = '3.64';

$.taconite = function(xml) {
    processDoc(xml);
};

$.taconite.debug = 0;  // set to true to enable debug logging to window.console.log
$.taconite.autodetect = true;
$.taconite.defaults = {
    cdataWrap: 'div'
};

// add 'replace' and 'replaceContent' plugins (conditionally)
$.fn.replace = $.fn.replace || function(a) {
    this.after(a);
    this.remove();
};
$.fn.replaceContent = $.fn.replaceContent || function(a) {
    return this.empty().append(a);
};

$.expr[':'].taconiteTag = function(a) {
    return a.taconiteTag === 1;
};

// allow auto-detection to be enabled/disabled on-demand
$.taconite.enableAutoDetection = function(b) {
    $.taconite.autodetect = b;
    if (origHttpData)
        $.httpData = b ? origHttpData : detect;
};

var logCount = 0;
function log() {
    if (!$.taconite.debug || !window.console || !window.console.log) return;
    !logCount++ && log('Plugin Version: ' + version);
    window.console.log('[taconite] ' + [].join.call(arguments,''));
}

var parseJSON = $.parseJSON || function(s) {
    return window['eval']('(' + s + ')');
};

function httpData( xhr, type, s ) {
    var ct = xhr.getResponseHeader('content-type') || '',
        xml = type === 'xml' || !type && ct.indexOf('xml') >= 0,
        data = xml ? xhr.responseXML : xhr.responseText;

    if (xml && data.documentElement.nodeName === 'parsererror') {
        $.error && $.error('parsererror');
    }
    if (s && s.dataFilter) {
        data = s.dataFilter(data, type);
    }
    if (typeof data === 'string') {
        if (type === 'json' || !type && ct.indexOf('json') >= 0) {
            data = parseJSON(data);
        } else if (type === "script" || !type && ct.indexOf("javascript") >= 0) {
            $.globalEval(data);
        }
    }
    return data;
}

function getResponse(xhr, type, s) {
    if (origHttpData)
        return origHttpData(xhr, type, s);
    return xhr.responseXML || xhr.responseText;
}

function detect(xhr, type, s) {
    var ct = xhr.getResponseHeader('content-type');
    if ($.taconite.debug) {
        log('[AJAX response] content-type: ', ct, ';  status: ', xhr.status, ' ', xhr.statusText, ';  has responseXML: ', xhr.responseXML != null);
        log('type arg: ' + type);
//        log('responseXML: ' + xhr.responseXML);  // IE9 doesn't like xhr.toString()
    }
    var data = getResponse(xhr, type, s);
    if (data && data.documentElement && data.documentElement.nodeName != 'parsererror') {
        $.taconite(data);
    }
    else if (typeof data == 'string') {
        // issue #4 (don't try to parse plain text or html responses
        if ( /taconite/.test(data) )
            $.taconite(data);
    }
    else {
        log('jQuery core httpData returned: ' + data);
        log('httpData: response is not XML (or not "valid" XML)');
    }
    return data;
}

// 1.5+ hook
$.ajaxPrefilter && $.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
    jqXHR.success(function( data, status, jqXHR ) {
        if ($.taconite.autodetect)
            detect(jqXHR, options.dataType, options);
    });
});

// < 1.5 hook
var origHttpData = $.httpData;
if ($.httpData)
    $.httpData = detect;  // replace jQuery's httpData method

// custom data parsers
var parsers = { 'json': jsonParser }, rawData, rawDataIndic;

$.taconite.registerParser = function(type, fn) {
    parsers[type] = fn;
};

function parseRawData(type, data) {
    var d = data, parser = parsers[type];
    if ($.isFunction(parser))
        return parser(data);
    else
        throw 'No parser registered for rawData of type "' + type + '"';
}

function jsonParser(json) {
    return parseJSON(json);
}


function processDoc(xml) {
    var status = true, ex;
    try {
        if (typeof xml == 'string')
            xml = convert(xml);
        if (! ( xml && xml.documentElement) ) {
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

        $.event.trigger('taconite-begin-notify', [taconiteDoc]);
        status = go(taconiteDoc);
    } catch(e) {
        status = ex = e;
    }
    rawDataIndic && $.event.trigger('taconite-rawdata-notify', [rawData]);
    $.event.trigger('taconite-complete-notify', [xml, !!status, status === true ? null : status]);
    if (ex)
        throw ex;
}

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
}

function go(xml) {
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
}

// process the taconite commands
function process(commands) {
    rawData = {};
    rawDataIndic = false;
    var trimHash = { wrap: 1 };
    var doPostProcess = 0;
    var a, n, v, i, j, js, els, raw, type, q, jq, cdataWrap;

    for(i=0; i < commands.length; i++) {
        if (commands[i].nodeType != 1)
            continue; // commands are elements
        var cmdNode = commands[i], cmd = cmdNode.tagName;
        if (cmd == 'eval') {
            js = (cmdNode.firstChild ? cmdNode.firstChild.nodeValue : null);
            log('invoking "eval" command: ', js);
            if (js)
                $.globalEval(js);
            continue;
        }
        if (cmd == 'rawData') {
            raw = (cmdNode.firstChild ? cmdNode.firstChild.nodeValue : null);
            type = cmdNode.getAttribute('type');
            log('rawData ('+type+'): ', raw);

            var namespace = cmdNode.getAttribute('namespace') || 'none';

            !rawData[namespace] && (rawData[namespace] = []);

            rawData[namespace].push({
                data: parseRawData(type, raw),
                type: type,
                name: cmdNode.getAttribute('name') || null,
                raw: raw
            });
            !rawDataIndic && (rawDataIndic = true);
            continue;
        }
        q = cmdNode.getAttribute('select');
        jq = $(q);
        if (!jq[0]) {
            log('No matching targets for selector: ', q);
            continue;
        }
        cdataWrap = cmdNode.getAttribute('cdataWrap') || $.taconite.defaults.cdataWrap;

        a = [];
        if (cmdNode.childNodes.length > 0) {
            doPostProcess = 1;
            for (j=0,els=[]; j < cmdNode.childNodes.length; j++)
                els[j] = createNode(cmdNode.childNodes[j], cdataWrap);
            a.push(trimHash[cmd] ? cleanse(els) : els);
        }

        // remain backward compat with pre 2.0.9 versions
        n = cmdNode.getAttribute('name');
        v = cmdNode.getAttribute('value');
        if (n !== null) a.push(n);
        if (v !== null) a.push(v);

        // @since: 2.0.9: support arg1, arg2, arg3...
        for (var j=1; true; j++) {
            v = cmdNode.getAttribute('arg'+j);
            if (v === null)
                break;
            // support numeric primitives
            if (v.length) {
                var n = Number(v);
                if (v == n)
                    v = n;
            }
            a.push(v);
        }

        $.taconite.debug && logCommand(q, cmd, a, els);
        jq[cmd].apply(jq,a);
    }

    // apply dynamic fixes
    doPostProcess && postProcess();
}

function logCommand(q, cmd, a, els) {
    var args = '...';
    if (!els) {
        args = '';
        for (var k=0, val=a[0]; k < a.length, val=a[k]; k++) {
            k > 0 && (args += ',');
            typeof val == 'string' ? (args += ("'" + val + "'")) : (args += val);
        }
    }
    log("invoking command: $('", q, "').", cmd, '('+ args +')');
}

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
}

function cleanse(els) {
    for (var i=0, a=[]; i < els.length; i++)
        if (els[i].nodeType == 1) a.push(els[i]);
    return a;
}

function createNode(node, cdataWrap) {
    var type = node.nodeType;
    if (type == 1) return createElement(node, cdataWrap);
    if (type == 3) return fixTextNode(node.nodeValue);
    if (type == 4) return handleCDATA(node.nodeValue, cdataWrap);
    return null;
}

function handleCDATA(s, cdataWrap) {
    var el = document.createElement(cdataWrap);
    var $el = $(el)[cdataWrap == 'script' ? 'text' : 'html'](s);
    var $ch = $el.children();

    // remove wrapper node if possible
    if ($ch.size() == 1)
        return $ch[0];
    return el;
}

function fixTextNode(s) {
    if ($.browser.msie) s = s.replace(/\n/g, '\r').replace(/\s+/g, ' ');
    return document.createTextNode(s);
}

function createElement(node, cdataWrap) {
    var e, tag = node.tagName.toLowerCase();
    // some elements in IE need to be created with attrs inline
    if ($.browser.msie && $.browser.version < 9) {
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
            var child = createNode (node.childNodes[i], cdataWrap);
            if(child) e.appendChild(child);
        }
    }
    if (! $.browser.mozilla) {
        if (tag == 'select' || (tag == 'option' && node.getAttribute('selected')))
            e.taconiteTag = 1;
    }
    return e;
}

function copyAttrs(dest, src, inline) {
    for (var i=0, attr=''; i < src.attributes.length; i++) {
        var a = src.attributes[i], n = $.trim(a.name), v = $.trim(a.value);
        if (inline) attr += (n + '="' + v + '" ');
        else if (n == 'style') { // IE workaround
            dest.style.cssText = v;
            dest.setAttribute(n, v);
        }
        else $.attr(dest, n, v);
    }
    return attr;
}

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
 * To enable FireBug debugging use: Click.jq.debug = true;
 */
(function($) {

    // Make sure that the Click namespace exists
    if( typeof Click == 'undefined' )
        Click = {};

    // Make sure that the Click.jq namespace exists
    if( typeof Click.jq == 'undefined' )
        Click.jq = {};

    Click.jq.debug = 0;  // set to true to enable debug logging to Firebug

    // Define method for logging
    Click.jq.log = function() {
        if (!Click.jq.debug || !window.console || !window.console.log) return;
        window.console.log('[click] ' + [].join.call(arguments,''));
    }

    // serialize the node children to xml
    Click.jq.toXmlChildren = function(xmlNode) {
        if (xmlNode == null) {
            return "";
        }
        var text = "";
        for (var i = 0; i < xmlNode.childNodes.length; i++) {
          var thisNode = xmlNode.childNodes[i];
          text += Click.jq.toXml(thisNode);
        }
        return text;
    }

    // serialize the node to xml
    Click.jq.toXml = function(node) {
        if (typeof XMLSerializer != "undefined"){
            // Gecko-based browsers, Safari, Opera.
            return (new XMLSerializer()).serializeToString(node);
        } else if (node.xml) {
            // Internet Explorer.
            return node.xml;
        }
        else {
            Click.jq.log('XML serialization is not supported');
        }
    };

    // parse given string to xml document
    Click.jq.fromXml = function(xml) {
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
        Click.jq.log('deserialization: ', ok ? 'successful!' : 'FAILED for : ' + xml);
        return doc;
    }

    // register addHeader function as JQuery plugin
    $.fn.addHeader = function(elems) {
        if(elems == null) {
            Click.jq.log('the header element is null');
            return jQuery( [] );
        }
        if ($.isArray(elems)) {
            for (var j=0; j < elems.length; j++) {
                var elem = elems[j];
                Click.jq.addHeader(elem);
            }
        } else {
            Click.jq.addHeader(elems);
        }
        return this;
    };

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
    Click.jq.addHeader = function (element) {
        if (element == null) {
            Click.jq.log('the header element to add to the page is null');
            return;
        }
        if (typeof element == 'string') {
            element = Click.jq.fromXml(element).documentElement;
        }
        if(element.nodeType != 1) {
            return;
        }
        var text = Click.jq.toXml(element);
        var tag = element.tagName.toLowerCase();

        // indicates whether the element should be added to head
        var append = false;

        // indicates whether a place holder script should added to head as
        // JQuery only evaluates scripts
        var shouldAppendScriptPlaceHolder = false;
        var evaluateOnly = false;

        if (tag == 'link') {
            append = canAddLinkHeader(element);
        } else if (tag == 'style') {
            append = canAddStyleHeader(element);
        } else if ((tag == 'script')) {
            var id = element.getAttribute("id");
            var src = element.getAttribute("src");
            // If no id nor src attribute is available to check against, only
            // evaluate the script
            if (isBlank(id) && isBlank(src)) {
                append = true;
                evaluateOnly = true;
            } else {
                append = canAddScriptHeader(element);
                shouldAppendScriptPlaceHolder = true;
            }
        }
        if(append) {
            if (evaluateOnly) {
                Click.jq.log('evaluating ',element.nodeName,': ',text);
            } else {
                Click.jq.log('adding ',element.nodeName,' to <head>: ',text);
            }

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
        if (isBlank(id) && isBlank(src)) {
            // If no id or src attribute is defined there is no need for placeholder
            return;
        }
        if (isNotBlank(id)) {
            script.id = element.getAttribute("id");
        }
        if (isNotBlank(src)) {
            // GOTCHA_1
            // Note the fake source attribute below. We cannot set the real
            // src attribute as that would trigger the browser to download
            // the script a second time
            script.setAttribute("src_", element.getAttribute("src"));
        }
        script.type = "text/javascript";
        appendToScript(script, "<!-- This script act as a placeholder only. *** Added by Click *** -->");
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
                Click.jq.log(element.nodeName+' with id: "'+id+'" already exists in the page');
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if the given script can be added to the document,
     * false otherwise.
     */
    function canAddScriptHeader(script) {
        if (containsElement(script)) return false;

        // When appending the script to head (see #GOTCHA_1 above), we cannot set the
        // src attribute as that would trigger the browser to download the script
        // a second time. Instead we set a fake src attribute. scriptExists
        // is aware of the fake src and will check against both src and src_
        // attributes. (This idea was adapted from Apache Wicket)
        var src = script.getAttribute("src");
        if(isNotBlank(src)) {
            // Check for existence of both src and src_ properties
            if($('script[src$='+src+']').length || $('script[src_$='+src+']').length) {
                Click.jq.log('script with src: "' + src + '" already exists in document');
                return false;
            }
        }
        return true;
    }

    /**
     * Return true if the given link is can be added to the document <head>,
     * false otherwise.
     */
    function canAddLinkHeader(link) {
        var href = link.getAttribute("href");
        if(isBlank(href)) {
            Click.jq.log('the link href attribute is not defined');
            return false;
        }
        if($('head link[href='+href+']').length) {
            Click.jq.log('link with href: "' + href + '" already exists in document');
            return false;
        }
        return true;
    }

    /**
     * Return true if the given style can be added to the document,
     * false otherwise.
     */
    function canAddStyleHeader(style) {
        if (containsElement(style)) return false;
        return true;
    }

    /**
     * Return true if the given value is null, "" or "undefined".
     */
    function isBlank(value) {
        if (value == null || value == "" || typeof(value) == "undefined")
            return true;
        else
            return false;
    }

    /**
     * Return true if the given value is not null, not "" and not "undefined".
     */
    function isNotBlank(value) {
        return !isBlank(value);
    }

    /**
     * Return the url of the given Element or null if no url is available.
     *
     * Elements which provides a URL include: href, form, img. This function
     * also caters for onclick handlers using 'location.href'.
     */
    function extractUrl(el) {
        var attr;
        if (el && el.attributes) {
            attr = el.attributes.href || el.attributes.src || el.attributes.action;
        }
        return attr ? attr.value : null;
    }

    /**
     * Return the url parameters of the given Element as an array of key/value
     * pairs or an empty Array if no parameters can be extracted.
     *
     * Elements which provides a URL include: href, form, img
     */
    function extractParameters(url) {
        if(url) {
            url = unescape(url);
            var start = url.indexOf('?')
            if (start == -1) {
                return new Array();
            }
            url=url.substring(start + 1);
            return Click.jq.parameterStrToArray(url);
        }
        return new Array();
    }

    /**
     * For the given Element, add the 'name', 'value' and 'id' attributes to
     * the given params as key/value pairs.
     * If excludeName is true, the name/value pair will be excluded
     */
    function addNameValueIdPairs(el, params, excludeName) {
        excludeName=excludeName||false;

        var jqe=jQuery(el);
        // Add attributes name, value and id as parameters
        var name = jqe.attr('name');
        var value = jqe.attr('value')||'';
        var id = jqe.attr('id');

        if (isNotBlank(name)) {
            if(!excludeName){
              params.push({
                'name':name,
                'value':value
              });
            }
            if (name != id || excludeName) {
                if (isNotBlank(id)) {
                    params.push({
                        'name':id,
                        'value':'1'});
                }
            }
        } else if (isNotBlank(id)) {
            params.push({
                'name':id,
                'value':'1'
            });
        }
    }

    /**
     * Split the given url paramters into key/value pairs.
     */
    Click.jq.parameterStrToArray = function(params) {
        if(!params) {
            return null;
        }
        var pairs=params.split("&");
        var ar=new Array();
        for (var i=0;i<pairs.length;i++) {
            var param = new Object();
            var pos = pairs[i].indexOf('=');
            if (pos >= 0) {
                param.name = pairs[i].substring(0,pos);
                param.value = pairs[i].substring(pos+1);
                ar.push(param);
            }
        }
        return ar;
    }

    /**
     * Extract all data from the given element that might be relevant to an
     * Ajax request. URL, name, value, id etc.
     */
    Click.jq.extractAjaxRequestData = function(el, event) {
        var url = extractUrl(el);
        var params = extractParameters(url);

        // Add event type. If no event is available default to domready
        var eventType = event ? event.type : "domready";
        params.push({name:'event', value: eventType});

        // Is input checkbox/radio etc
        //var toggleControl = false;
        //var toggleSelected = false;
        var excludeName= false;
        var type=jQuery(el).attr("type");
        if(type=="checkbox" || type=="radio") {
            excludeName=!jQuery(el).attr('checked');
            //toggleControl=true;
            // A two state control (checkbox/radio) that was unchecked in this event,
            // must not send name/value param to server, since that will indicate
            // to the server that the control was checked

            // TODO Click Checkbox and Radio should be made
            // smarter. The Checkbox and Radio should take the incoming request
            // parameter value into account, not only whether or not it is available
            //excludeNameParam = !toggleSelected;
        }

        // Add the Control attributes 'name', 'value' and 'id' as parameters
        addNameValueIdPairs(el, params, excludeName);
        return params;
    }

    /**
     * Return the url path of the given Element as a String.
     *
     * Elements which provides a URL include: href, form, img
     */
    Click.jq.extractUrlPath = function(el) {
        var url = Click.jq.extractUrl(el);
        if(url) {
            var i = url.indexOf('?');
            return (i > 0) ? url.substring(0, i): url;
        }
        return '';
    }

    /**
     * Merge multiple callbacks into a single callback if they are invoked
     * within the given delay. Useful when binding to mouse or key events.
     * A delay of less than or equal to 0 executes immediately.
     *
     * Copied from here: http://unscriptable.com/index.php/2009/03/20/debouncing-javascript-methods/
     */
    Click.jq.debounce=function(func, delay) {
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