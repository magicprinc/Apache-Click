/* This jQuery based script contains common utility functions used in Ajax requests.
 */
(function($) {

    // Make sure that the Click namespace exists
    if( typeof Click == 'undefined' )
        Click = {};

    // Make sure that the Click.jq namespace exists
    if( typeof Click.jq == 'undefined' )
        Click.jq = {};

    /* Config */
    Click.jq.sessionTimeoutHttpCode=403; // The HTTP status returned by the server when a session timeout occurs.

    Click.jq.debug = 0;  // set to true to enable logging to the console

    /* Utility methods */

    /*
     * Extract all elements from the given data. The returned object will contain
     * a property for every extracted element. The property name will be set to the
     * name or the extracted element. Usage:
     *
     * var elements = Click.jq.extractElements(data);
     * var headElements = elements.headElements;
     * var scripts = elements.scripts;
     * var form = elements.form;
     * var msg = elements.msg;
     */
    Click.jq.extractElements=function(data) {
        var elements={};
        var start=data.indexOf("<!--* Clk");
        while(start != -1) {
            var name=extractName(data, start+9);
            var end = data.indexOf("<!--* Clk", start+10);
            if (end == -1) {
                end = data.length;
            }
            // Remove header
            start=data.indexOf('\n',start)+1;
            var element=data.substring(start, end);
            if(typeof(elements[name])!='undefined'){
                alert('Error: Duplicate element found called "'+name+'"');
                return elements;
            }
            elements[name]=element;
            Click.jq.log("extracted '", name, "':", element);
            start=(end==data.length)?-1:end;
        }
        return elements;
    }

    /*
     * Add the headElements to page <head> section. If the element is a <link>
     * or <script> that refers to an external resource that resource will be
     * retrieved.
     *
     * If the given elements is not a String the elements will be assumed to be
     * a JavaScript object with a property called 'headElements'.
     *
     * Usage:
     *
     * $.ajax(url: url,
     *     success: function(data) {
     *         var elements = Click.jq.extractElements(data);
     *         Click.jq.addHeadElements(elements);
     * });
     */
    Click.jq.addHeadElements=function(elements) {
        var headElements=typeof(elements)=='string'?elements:elements.headElements;
        if(isBlank(headElements)) {
            log('No headElements to add');
            return;
        }
        var doc=Click.jq.stringToXml('<root>'+headElements+'</root>').documentElement;
        var length=doc.childNodes.length;
        log('headElements to add: ', length);
        for (var j=0; j < length; j++) {
            var child = doc.childNodes[j];
            Click.jq.addHeader(child);
        }
    }

    /*
     * Evaluate the given script elements.
     *
     * If the given elements is not a String the elements will be assumed to be
     * a JavaScript object with a property called 'scripts'.
     *
     * Usage:
     *
     * $.ajax(url: url,
     *     success: function(data) {
     *         var elements = Click.jq.extractElements(data);
     *         Click.jq.evalScripts(elements);
     * });
     */
    Click.jq.evalScripts=function(elements) {
        var scripts=typeof(elements)=='string'?elements:elements.scripts;
        if(isBlank(scripts)) {
            log('No scripts to evaluate');
            return;
        }
        var doc=Click.jq.stringToXml('<root>'+scripts+'</root>').documentElement;
        var length=doc.childNodes.length;
        log('scripts to evaluate: ', length);
        for (var j=0; j < length; j++) {
            var child = doc.childNodes[j];
            Click.jq.addHeader(child);
        }
    }

    /* Error handling*/
  Click.jq.lang={
    noCssSelector:'ERROR: No CSS selector found. A CSS selector specifies which element to ajaxify',
    unknownError:'Unknown error',
    status404:'Requested URL not found.',
    status500:'Internel Server Error.',
    parseError:'Error.\nParsing Request failed.',
    timeoutError:'Request timed out.',
    defaultError:'Sorry, an error occurred',
    sessionTimeoutError:'Your session appears to have expired.  You will asked to log in again and returned here.'
  };

    /*
     * Provides the following error handling for Ajax requests:
     * - 404, Not Found error message
     * - 500, Internal Server Error
     * - parseerror, if the response could not be parsed
     * - timeout, if the request timed out
     *  - sessionTimeoutError, if a Ajax request is made and the server session
     *  has timed out, this error is displayed. Note that a session timeout error
     *  is identified through the sessionTimeoutHttpCode variable which defaults
     *  to 403.
     * - defaultError, if an error occurs which isn't one of the above
     *
     * If the given elements is not a String the elements will be assumed to be
     * a JavaScript object with a property called 'headElements'.
     *
     * Usage:
     *
     * $.ajax(url: url,
     *     error: function (xhr, textStatus, errorThrown) {
     *         var elements = Click.jq.extractElements(data);
     *         Click.jq.addHeadElements(elements);
     * });
     */
    Click.jq.handleError=function(xhr, textStatus, errorThrown) {
        if(xhr.readyState==4){ // 4 -> DONE
            try{
                log('handleError: ', 'textStatus=', textStatus, ', xhr.status=', xhr.status, ', errorThrown=', errorThrown, ', xhr.responseText=', xhr.responseText);

                if(xhr.status==0){
                // 0 -> Might be offline. Ignore
                }else if(xhr.status == Click.jq.sessionTimeoutHttpCode) {
                    alert(Click.jq.lang.sessionTimeoutError);
                    window.location.reload();
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

    /*
     * Retrieve data as an array from the given options. The array will consist of
     * JavaScript objects with the following properties:
     * - name: 'name'
     * - value: 'value'
     *
     * The data array can be passed directly to the jQuery Ajax function to be sent
     * to the server.
     *
     * Options is a JavaScript object with the following properties:
     *   - source:Element
     *   - event:Event
     *   - extraData:Array (extraData should be an array of JS objects: {name='X', value='Y'}
     *       or a string of key/value pairs.)
     *
     *   The following data will be returned in the array:
     *   - if a source element is passed in it's ID, NAME and VALUE is included
     *   - if an event is passedd in it's type (click, blur etc.) will be included. If
     *   no event is passed in the event defaults to 'domready'
     *   - if a keypress occurred the 'which' value indicate the specific key.
     *   - if extraData is passed in it will be appended to the array
     *
     *  Usage:
     *  var data = Click.jq.getData( {source:this, event: event, extraData: 'extraParam=hello'} );
     *  $.ajax({
     *  url: '/mycorp/mypage.htm',
     *  data: data
     *  });
     */
    Click.jq.getData=function(options) {
        var defaults = {
            source: null,
            extraData: null
        }
        var opts=$.extend(defaults, options);

        var data = new Array();
        if (!opts.source) {
            alert('You must provide a source element when invoking Click.jq.getData');
            return data;
        }

        addBasicData(data,opts.event);

        var excludeName=isExcludeName(opts.source);

        // Add the Control attributes 'name', 'value' and 'id' as parameters
        addNameValueIdPairs(opts.source, data, excludeName);

        var extra=opts.extraData;
        // Copy extraData to data
        if(extra) {
            addExtraData(data, extra);
        }

        return data;
    }

    /*
     * Retrieve form data as an array from the given options. The array will consist of
     * JavaScript objects with the following properties:
     * - name: 'name'
     * - value: 'value'
     *
     * The data array can be passed directly to the jQuery Ajax function to be sent
     * to the server.
     *
     * Options is a JavaScript object with the following properties:
     *   - source:Element
     *   - form:Element
     *   - event:Event
     *   - extraData:Array (extraData should be an array of JS objects: {name='X', value='Y'}
     *       or a string of key/value pairs.)
     *
     *   The following data will be returned in the array:
     *   - if a form element is passed in it's ID and all Field name/value pairs is included.
     *   getFormData uses jQuery.serializeArray to retrieve the form field data. File fields
     *   are not included
     *   - if a source element is passed in it's NAME and VALUE is included
     *   - if an event is passedd in it's type (click, blur etc.) will be included. If
     *   no event is passed in the event defaults to 'domready'
     *   - if a keypress occurred the 'which' value indicate the specific key.
     *   - if extraData is passed in it will be appended to the array
     *
     *  Usage:
     *  var form = $('#form');
     *  var button = $('#form_button');
     *  var data = Click.jq.getFormData( {form: form[0], source:button[0], event: event, extraData: 'extraParam=hello'} );
     *  $.ajax({
     *  url: '/mycorp/mypage.htm',
     *  data: data
     *  });
     */
    Click.jq.getFormData=function(options) {
        var defaults = {
            form: null,
            source: null,
            extraData: null
        }
        var opts=$.extend(defaults, options);

        var data = new Array();
        if (!opts.form) {
            alert('You must provide a form element when invoking Click.jq.getFormData');
            return data;
        }

        addBasicData(data,opts.event);

        // Add the form 'id' attribute and the source Control 'name' and 'value' attributes as parameters
        var jqform=$(opts.form);
        var formId=jqform.attr('id');
        if (isNotBlank(formId)) {
            data.push({'name':formId, 'value':'1'});
        }

        if (opts.source) {
            var excludeName=isExcludeName(opts.source);
            var jqe=$(opts.source);
            var name = jqe.attr('name');
            var value = jqe.attr('value')||'';
            if (isNotBlank(name) && !excludeName) {
                data.push({'name':name, 'value':value});
            }
        }
        // Add form field data
        var fieldData=jqform.serializeArray();
        $.merge(data, fieldData);

        var extra=opts.extraData;
        // Copy extraData to data
        if(extra) {
            addExtraData(data, extra);
        }
        return data;
    }

    /*
     * Convert the given paramter key/value pairs to an array of JS objects, for
     * example, the string: 'key1=value1&key2=value2' becomes:
     * [{name: key1, value: value1}, {name: key2, value: value2}]
     */
    Click.jq.parametersToArray = function(params) {
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

    /*
     * Return the url for the given source element as a string. Elements which
     * have a URL include: links, forms and images.
     *
     * Usage:
     * $('#linkId').click(function(e) {
     *     // jQuery assigns the clicked element to the 'this' variable
     *     var source = this;
     *     var url = Click.jq.getUrl(source);
     *     $.ajax({
     *     url: url
     *     });
     * });
     */
    Click.jq.getUrl=function(source) {
        var attr;
        if (source && source.attributes) {
            attr = source.attributes.href || source.attributes.src || source.attributes.action;
        }
        return attr ? attr.value : null;
    }

    /**
     * Merge multiple callbacks into a single callback if they are invoked
     * within the given threshold (specified in milliseconds). Useful when
     * binding to mouse or key events. A threshold of less than or equal to 0
     * executes immediately.
     *
     * Copied from here: http://unscriptable.com/index.php/2009/03/20/debouncing-javascript-methods/
     */
    Click.jq.debounce=function(func, threshold) {
        if(threshold<= 0){
            return func;
        }
        var timeout;
        return function debounced () {
            var obj = this, args = arguments;
            function delayed () {
                if (threshold > 0)
                    func.apply(obj, args);
                timeout = null;
            };

            if (timeout) {
                clearTimeout(timeout);
            } else if (threshold <= 0) {
                func.apply(obj, args);
            }
            timeout = setTimeout(delayed, threshold || 100);
        };
    }

    /*
     * Convert and return the given string as an XML document.
     */
    Click.jq.stringToXml=function(s) {
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
                window.console.error('[click] ERROR parsing XML string for conversion: ' + e);
            throw e;
        }
        var ok = doc && doc.documentElement && doc.documentElement.tagName != 'parsererror';
        log('conversion ', ok ? 'successful!' : 'FAILED');
        return doc;
    }

    /*
     * Convert and return the given node as a string.
     */
    Click.jq.xmlToString=function(node) {
        if (typeof XMLSerializer != "undefined"){
            // Gecko-based browsers, Safari, Opera.
            return (new XMLSerializer()).serializeToString(node);
        } else if (node.xml) {
            // Internet Explorer.
            return node.xml;
        }
        else {
            Click.jq.log('XML serialization is not supported');
            return '';
        }
    }

        /* Logging */

    // Define methods for logging
    Click.jq.log = log = function() {
        if (!Click.jq.debug || !window.console || !window.console.log) return;
        window.console.log('[click] ' + [].join.call(arguments,''));
    }

    /* Add Head Elements*/

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
            element = Click.jq.stringToXml(element).documentElement;
        }
        if(element.nodeType != 1) {
            return;
        }

        var text = Click.jq.xmlToString(element);
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

// private methods ----------

/**
 *Add extra data to the given data array
 */
function addExtraData(data, extra) {
        if (jQuery.isArray(extra)) {
            jQuery.merge(data, extra);
        } else {
            if (jQuery.isPlainObject(extra)) {
                data.push(extra);
            } else if (typeof(extra)=='string') {
                var ar=Click.jq.parametersToArray(extra);
                jQuery.merge(data, ar);
            } else {
                alert('extraData should be a JS object or a string of key/value pairs');
            }
        }
    }

    /**
     * For the given Element, add the 'name', 'value' and 'id' attributes to
     * the given params as key/value pairs.
     * If excludeName is true, the name/value pair will be excluded
     */
    function addNameValueIdPairs(source, params, excludeName) {
        excludeName=excludeName||false;

        var jqe=jQuery(source);
        // Add attributes name, value and id as parameters
        var name = jqe.attr('name');
        var value = jqe.attr('value')||'';
        var id = jqe.attr('id');

        if (isNotBlank(name)) {
            if(!excludeName){
                params.push({'name':name, 'value':value});
            }
            if (name != id || excludeName) {
                if (isNotBlank(id)) {
                    params.push({'name':id, 'value':'1'});
                }
            }
        } else if (isNotBlank(id)) {
            params.push({'name':id, 'value':'1'});
        }
    }

    function extractName(text, start) {
        start=text.indexOf("'", start);
        if(start>=0) {
            start+=1;
            var end = text.indexOf("'", start);
            if (end >= 0) {
                return text.substring(start, end);
            }
        }
        alert('Cannot extract name from the string: ' + text);
        return null;
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
     * Determine whether or not the source name/value pair should be added.
     */
    function isExcludeName(source){
        if(!source) return false;
        var type=jQuery(source).attr("type");
        var excludeName=false;
        if(type=="checkbox" || type=="radio") {
            excludeName=!jQuery(source).attr('checked');
            // A two state control (checkbox/radio) that was unchecked in this event,
            // must not send name/value param to server, since that will indicate
            // to the server that the control was checked

            // TODO Click Checkbox and Radio should be made
            // smarter. The Checkbox and Radio should take the incoming request
            // parameter value into account, not only whether or not it is available
            //excludeNameParam = !toggleSelected;
        }
        return excludeName;
    }

    /**
     * Add basic data to the dat array.
     */
    function addBasicData(data, event) {
        // Add event type. If no event is available default to domready
        var eventType = event ? event.type : "domready";
        data.push({
            name:'event',
            value: eventType
        });

        // Add event which value.
        var which = event ? event.which : null;
        if(which && typeof which != 'undefined') {
            if (which != 1) {
                data.push({
                    name:'which',
                    value: which
                });
            }
        }
    }
}) (jQuery)