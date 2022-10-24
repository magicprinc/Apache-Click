$(document).ready(function(){
    var toggleChk = $('#form_toggle');

    // Initial state
    toggle(!toggleChk.is(':checked'));

    toggleChk.click(function(){
        if($(this).is(':checked')) {
            toggle(false);
        } else {
            toggle(true);
        }
    });

    function toggle(disable) {
       var checkbox = $('#form_check1');

       //var hidden = $('<input type="hidden" name="check1" value="0">');
       if (disable) {
            checkbox.attr('disabled', 'disabled').attr('class', 'disabled');
            $('#form_check1_hidden').remove();
        } else {
            // Append a hidden field with the same name as the checkbox
            // so that the server assumes the checkbox is enabled
            //alert($('<input type="hidden" name="check1" value="0">'));
            checkbox.after('<input id="form_check1_hidden" type="hidden" name="check1" value="0">');
            checkbox.removeAttr('disabled').removeAttr('class');
        }
    }
})
