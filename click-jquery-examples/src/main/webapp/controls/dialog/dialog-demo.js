jQuery(document).ready(function(){

    jQuery('#dialog').dialog({title: 'Login', autoOpen: false, modal: true});

    jQuery('#login-link').click(function(){
        jQuery('#dialog').dialog('open');
        return false;
    });

    jQuery('#form_login, #form_close').click(function(){
        jQuery('#dialog').dialog('close');
        return false;
    });
});
