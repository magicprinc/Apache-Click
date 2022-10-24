jQuery(document).ready(function(){
    jQuery('#dialog').dialog({title: 'Hello'});
    jQuery('#dialog').dialog('close');
    jQuery('#table th').click(function(event) {
      var target = jQuery(event.target);
      var offset = target.offset();
      //jQuery('#dialog').css({"display":"block"});
      //jQuery('#dialog').dialog('open');
      //jQuery('#dialog').dialog("close");
      //jQuery('#dialog').dialog({position: [offset.left + target.width(), offset.top]});
      jQuery('#dialog').dialog('option', 'position', [offset.left+target.width(), offset.top]);
      jQuery('#dialog').dialog('open');
    });

    jQuery('#chk_firstname').click(function() {
        return toggleColumn(this, 1);
    });

    jQuery('#chk_lastname').click(function() {
        return toggleColumn(this, 2);
    });

    jQuery('#chk_age').click(function() {
        return toggleColumn(this, 3);
    });
  
    jQuery('#chk_street').click(function() {
        return toggleColumn(this, 4);

    });
  
    function toggleColumn(checkbox, column) {
        // ensure at least one column stays visible
        var total = jQuery('#table th').length;
        var count = 0;
        jQuery('#table th').each(function() {
            if (jQuery(this).is(':hidden')) {
                count++;
            }
        });
        if (count == total - 1 && checkbox.checked) {
            return false;
        }

        if (checkbox.checked) {
            jQuery('#table th:nth-child(' + column + '), #table td:nth-child(' + column + ')').hide();
        } else {
            jQuery('#table th:nth-child(' + column + '), #table td:nth-child(' + column + ')').show();
        }
        return true;
    }
});