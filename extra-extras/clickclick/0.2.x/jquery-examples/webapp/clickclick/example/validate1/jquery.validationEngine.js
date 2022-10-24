/*
 * Inline Form Validation Engine v 1.2.1, jQuery plugin
 *
 * Copyright(c) 2009, Cedric Dugas
 * http://www.position-relative.net
 *
 * Form validation engine witch allow custom regex rules to be added.
 * Licenced under the MIT Licence
 */

(function($){
jQuery.fn.validationEngine = function(settings) {
	if($.validationEngineLanguage){					// IS THERE A LANGUAGE LOCALISATION ?
		allRules = $.validationEngineLanguage.allRules
	}else{
		allRules = {"required":{    			  // Add your regex rules here, you can take telephone as an example
							"regex":"none",
							"alertText":"* This field is required",
							"alertTextCheckboxMultiple":"* Please select an option",
							"alertTextCheckboxe":"* This checkbox is required"},
						"length":{
							"regex":"none",
							"alertText":"* Between ",
							"alertText2":" and ",
							"alertText3": " characters allowed"},
            "minlength":{
							"regex":"none",
							"alertText":"* The field must be at least ",
							"alertText2":" characters"},
            "maxlength":{
							"regex":"none",
							"alertText":"* The field must be no longer than ",
							"alertText2":" characters"},
            "minvalue":{
							"regex":"none",
							"alertText":"* The field value must not be smaller than "},
            "maxvalue":{
							"regex":"none",
							"alertText":"* The field value must not be larger than "},
						"minCheckbox":{
							"regex":"none",
							"alertText":"* Checks allowed Exceeded"},
						"confirm":{
							"regex":"none",
							"alertText":"* Your field is not matching"},
						"telephone":{
							"regex":"/^[0-9\-\(\)\ ]*$/",
							"alertText":"* Invalid phone number"},
						"email":{
							"regex":"/^$|^[a-zA-Z0-9_\.\-]+\@([a-zA-Z0-9\-]+\.)+[a-zA-Z0-9]{2,4}$/",
							"alertText":"* Invalid email address"},
						"date":{
              "regex":"/^$|^[0-9]{4}\-\[0-9]{1,2}\-\[0-9]{1,2}$/",
              "alertText":"* Invalid date, must be in YYYY-MM-DD format"},
						"onlyNumber":{
							"regex":"/^[0-9\ ]*$/",
							"alertText":"* Numbers only"},
						"noSpecialCaracters":{
							"regex":"/^[0-9a-zA-Z]*$/",
							"alertText":"* No special characters allowed"},
						"onlyLetter":{
							"regex":"/^[a-zA-Z\ \']*$/",
							"alertText":"* Letters only"},
            "color":{
							"regex": "/^$|^#[a-fA-F0-9]{3}([a-fA-F0-9]{3})?$/",
							"alertText":"* Not a valid color."},
            "creditcard":{
							"regex": "none",
							"alertText":"* The field is invalid."},
            "picklist":{
							"regex": "none",
							"alertText":"* This field is required."}
					}
	}

 	settings = jQuery.extend({
		allrules:allRules,
		success : false,
		failure : function() {}
	}, settings);


	$("form").bind("submit", function(caller){   // ON FORM SUBMIT, CONTROL AJAX FUNCTION IF SPECIFIED ON DOCUMENT READY
		if(submitValidation(this) == false){
			if (settings.success){
				settings.success && settings.success();
				return false;
			}
		}else{
			settings.failure && settings.failure();
			return false;
		}
	})
	$(this).not("[type=checkbox]").bind("blur", function(caller){loadValidation(this)})
	$(this+"[type=checkbox]").bind("click", function(caller){loadValidation(this)})
  $(this).not("[type=radio]").bind("blur", function(caller){loadValidation(this)})
	$(this+"[type=radio]").bind("click", function(caller){loadValidation(this)})
  $(this).not("[type=select-one]").bind("change", function(caller){loadValidation(this)})
	$(this).not("[type=select-multiple]").bind("change", function(caller){loadValidation(this)})

	var buildPrompt = function(caller,promptText) {			// ERROR PROMPT CREATION AND DISPLAY WHEN AN ERROR OCCUR
		var divFormError = document.createElement('div')
		var formErrorContent = document.createElement('div')
		var arrow = document.createElement('div')


		$(divFormError).addClass("formError")
    var name=$(caller).attr("name");
		$(divFormError).addClass(name);
		$(formErrorContent).addClass("formErrorContent")
		$(arrow).addClass("formErrorArrow")

		$("body").append(divFormError)
		$(divFormError).append(arrow)
		$(divFormError).append(formErrorContent)
		$(arrow).html('<div class="line10"></div><div class="line9"></div><div class="line8"></div><div class="line7"></div><div class="line6"></div><div class="line5"></div><div class="line4"></div><div class="line3"></div><div class="line2"></div><div class="line1"></div>')
		$(formErrorContent).html(promptText)

		callerTopPosition = $(caller).offset().top;
		callerleftPosition = $(caller).offset().left;
		callerWidth =  $(caller).width()
		callerHeight =  $(caller).height()
		inputHeight = $(divFormError).height()

		callerleftPosition = callerleftPosition + callerWidth -30
		callerTopPosition = callerTopPosition  -inputHeight -10

		$(divFormError).css({
			top:callerTopPosition,
			left:callerleftPosition,
			opacity:0
		})
		$(divFormError).fadeTo("fast",0.8);
	};
	var updatePromptText = function(caller,promptText) {	// UPDATE TEXT ERROR IF AN ERROR IS ALREADY DISPLAYED
		updateThisPrompt = $(caller).attr("name")
		$("."+updateThisPrompt).find(".formErrorContent").html(promptText)

    // Handle radio and check which consist of multiple elements
    var elements = $("input[name=" + updateThisPrompt+"]");
    if(elements.size() > 1){
      if($(caller) != elements[0]){
        return;
      }
    }

		callerTopPosition  = $(caller).offset().top;
		inputHeight = $("."+updateThisPrompt).height()

		callerTopPosition = callerTopPosition  -inputHeight -10
		$("."+updateThisPrompt).animate({
			top:callerTopPosition
		});
	}
	var loadValidation = function(caller) {		// GET VALIDATIONS TO BE EXECUTED

		rulesParsing = $(caller).attr('class');
		rulesRegExp = /\[(.*)\]/;
		getRules = rulesRegExp.exec(rulesParsing);
    if (getRules==null||getRules==""||getRules=="undefined") {
        return;
    }
		str = getRules[1]
		pattern = /\W+/;
		result= str.split(pattern);

		var validateCalll = validateCall(caller,result)
		return validateCalll

	};
	var validateCall = function(caller,rules) {	// EXECUTE VALIDATION REQUIRED BY THE USER FOR THIS FILED
		var promptText =""
		var prompt = $(caller).attr("name");
		var caller = caller;
		isError = false;
		callerType = $(caller).attr("type");

		for (i=0; i<rules.length;i++){
			switch (rules[i]){
			case "optional":
				if(!$(caller).val()){
					closePrompt(caller)
					return isError
				}
			break;
			case "required":
				_required(caller,rules);
			break;
			case "custom":
				 _customRegex(caller,rules,i);
			break;
			case "length":
				 _length(caller,rules,i);
			break;
      case "minlength":
				 _minlength(caller,rules,i)
			break;
      case "maxlength":
				 _maxlength(caller,rules,i)
			break;
      case "minvalue":
				 _minvalue(caller,rules,i)
			break;
      case "maxvalue":
				 _maxvalue(caller,rules,i)
			break;
			case "minCheckbox":
				 _minCheckbox(caller,rules,i);
			break;
			case "confirm":
				 _confirm(caller,rules,i);
			break;
      case "creditcard":
				 _creditcard(caller,rules,i);
			break;
      case "picklist":
				 _picklist(caller,rules,i);
			break;
			default :;
			};
		};
		if (isError == true){

			if($("input[name="+prompt+"]").size()> 1 && callerType == "radio") {		// Hack for radio group button, the validation go the first radio
				caller = $("input[name="+prompt+"]:first")
			}
			($("."+prompt).size() ==0) ? buildPrompt(caller,promptText)	: updatePromptText(caller,promptText)
		}else{
			closePrompt(caller)
		}

		/* VALIDATION FUNCTIONS */
		function _required(caller,rules){   // VALIDATE BLANK FIELD
			callerType = $(caller).attr("type")

			if (callerType == "text" || callerType == "password" || callerType == "textarea"
          || callerType == "file"){

				if(!$(caller).val()){
					isError = true
					promptText += settings.allrules[rules[i]].alertText+"<br />"
				}
			}
			else if (callerType == "radio" || callerType == "checkbox" ){
				callerName = $(caller).attr("name");

				if($("input[name="+callerName+"]:checked").size() == 0) {
					isError = true
					if($("input[name="+callerName+"]").size() ==1) {
						promptText += settings.allrules[rules[i]].alertTextCheckboxe+"<br />"
					}else{
						 promptText += settings.allrules[rules[i]].alertTextCheckboxMultiple+"<br />"
					}
				}
			}
			if (callerType == "select-one") { // added by paul@kinetek.net for select boxes, Thank you
        var firstOption = $("#" + $(caller).attr("id") + " option:first").val();
        if($(caller).val() == firstOption){
					isError = true
					promptText += settings.allrules[rules[i]].alertText+"<br />"
				}
			}
			if (callerType == "select-multiple") { // added by paul@kinetek.net for select boxes, Thank you
        var firstOption = $("#" + $(caller).attr("id") + " option:first").val();
         if($(caller).val() == firstOption){
					isError = true
					promptText += settings.allrules[rules[i]].alertText+"<br />"
				}
			}
		}
		function _customRegex(caller,rules,position){		 // VALIDATE REGEX RULES
			customRule = rules[position+1]
			pattern = eval(settings.allrules[customRule].regex)

			if(!pattern.test($(caller).attr('value'))){
				isError = true
				promptText += settings.allrules[customRule].alertText+"<br />"
			}
		}
		function _confirm(caller,rules,position){		 // VALIDATE FIELD MATCH
			confirmField = rules[position+1]

			if($(caller).attr('value') != $("#"+confirmField).attr('value')){
				isError = true
				promptText += settings.allrules["confirm"].alertText+"<br />"
			}
		}
		function _length(caller,rules,position){    // VALIDATE LENGTH

			var startLength = eval(rules[position+1]);
			var endLength = eval(rules[position+2]);
			var feildLength = $(caller).attr('value').length;

      if (feildLength == 0) { // no value entered
        return;
      }

			if(feildLength<startLength || feildLength>endLength){
				isError = true
				promptText += settings.allrules["length"].alertText+startLength+settings.allrules["length"].alertText2+endLength+settings.allrules["length"].alertText3+"<br />"
			}
		}
    function _minlength(caller,rules,position){    // VALIDATE MIN LENGTH

			var minLength = eval(rules[position+1])
			var fieldLength = $(caller).attr('value').length

      if (fieldLength == 0) { // no value entered
        return;
      }

			if(fieldLength<minLength){
				isError = true
				promptText += settings.allrules["minlength"].alertText+minLength+settings.allrules["minlength"].alertText2
            +"<br />"
			}
		}
    function _maxlength(caller,rules,position){    // VALIDATE MAX LENGTH

			var maxLength = eval(rules[position+1])
			var fieldLength = $(caller).attr('value').length

      if (fieldLength == 0) { // no value entered
        return;
      }

			if(fieldLength>maxLength){
				isError = true
				promptText += settings.allrules["maxlength"].alertText+maxLength+settings.allrules["maxlength"].alertText2
            +"<br />"
			}
		}
    function _minvalue(caller,rules,position){    // VALIDATE MIN VALUE

			var minValue = eval(rules[position+1]);
			var fieldValue = $(caller).attr('value');

      if (fieldValue.length == 0) { // no value entered
        return;
      }

			if(fieldValue<minValue){
				isError = true
				promptText += settings.allrules["minvalue"].alertText+minValue+"<br />"
			}
		}
    function _maxvalue(caller,rules,position){    // VALIDATE MAX VALUE

			var maxValue = eval(rules[position+1]);
			var fieldValue= $(caller).attr('value');

      if (fieldValue.length == 0) { // no value entered
        return;
      }

			if(fieldValue>maxValue){
				isError = true
				promptText += settings.allrules["maxvalue"].alertText+maxValue+"<br />"
			}
		}
		function _minCheckbox(caller,rules,position){    // VALIDATE CHECKBOX NUMBER

			nbCheck = eval(rules[position+1])
			groupname = $(caller).attr("name")
			groupSize = $("input[name="+groupname+"]:checked").size()

			if(groupSize > nbCheck){
				isError = true
				promptText += settings.allrules["minCheckbox"].alertText+"<br />"
			}
		}
    function _creditcard(caller,rules,position){    // VALIDATE CREDIT CARD NUMBER

			var value=$(caller).val();
      if (value.length == 0) { // no value entered
        return;
      }

      // Trim spaces
			var buffer = '';
			for(var i=0;i<value.length;i++){
				var c = value.charAt(i);
				if(c != '=' && c != ' '){
					buffer = buffer + c;
				}
			}
			value = buffer;

			var length = value.length;
			if(length < 13){
				isError = true;
				promptText += settings.allrules["creditcard"].alertText+"<br />";
        return;
			}

			var firstdig  = value.charAt(0);
			var seconddig = value.charAt(1);

      var type  = $(caller).next().val();
      var isValid = false;
			if(type=='VISA'){
				isValid = ((length == 16) || (length == 13)) && (firstdig == '4');
			} else if(type=='MASTER'){
				isValid = (length == 16) && (firstdig == '5') && ("12345".indexOf(seconddig) >= 0);
			} else if(type=='AMEX'){
				isValid = (length == 15) && (firstdig == '3') && ("47".indexOf(seconddig) >= 0);
			} else if(type=='DINERS'){
				isValid = (length == 14) && (firstdig == '3') && ("068".indexOf(seconddig) >= 0);
			} else if(type=='DISCOVER'){
				isValid = (length == 16) && value.startsWith("6011");
			}

      if(!isValid){
        isError = true;
				promptText += settings.allrules["creditcard"].alertText+"<br />"
			}
		}
    function _picklist(caller,rules,position){    // VALIDATE PICK LIST REQUIRED

			var selectId = rules[position+1];
      if (!selectId) {
        return;
      }

      var select = document.getElementById(selectId);
      if(!select){
          return;
      }

      if (select.options.length == 0) {
        isError = true
				promptText += settings.allrules["picklist"].alertText+"<br />"
      }
		}

		return(isError) ? isError : false;
	};
	var closePrompt = function(caller) {	// CLOSE PROMPT WHEN ERROR CORRECTED
		closingPrompt = $(caller).attr("name");
		$("."+closingPrompt).fadeTo("fast",0,function(){
			$("."+closingPrompt).remove()
		});
	};
	var submitValidation = function(caller) {	// FORM SUBMIT VALIDATION LOOPING INLINE VALIDATION
		var stopForm = false
		$(caller).find(".formError").remove()
		var toValidateSize = $(caller).find("[class^=validate]").size()

		$(caller).find("[class^=validate]").each(function(){
			var validationPass = loadValidation(this)
			return(validationPass) ? stopForm = true : "";
		});
		if(stopForm){							// GET IF THERE IS AN ERROR OR NOT FROM THIS VALIDATION FUNCTIONS
			destination = $(".formError:first").offset().top;
			$("html:not(:animated),body:not(:animated)").animate({ scrollTop: destination}, 1100)
			return true;
		}else{
			return false
		}
	};
};
})(jQuery);