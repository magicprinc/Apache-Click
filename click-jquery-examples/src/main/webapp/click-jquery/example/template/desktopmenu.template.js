jQuery(document).ready(function(){
	var options = {minWidth: 120,
      hoverOpenDelay: 500,
      openDelay: 500,
      hideDelay: 800,
      offsetTop: 0,
      arrowSrc: '$context/click-jquery/example/desktopmenu/arrow_right.gif',
      copyClassAttr: true,
      onClick: function(e, menuItem){
          jQuery.Menu.closeAll();
      }
	};
	jQuery('$selector').menu(options);
  jQuery('$selector li.empty').hover(function(){
    jQuery.Menu.closeAll();
    $(this).addClass('activetarget');
  }, function(){
    $(this).removeClass('activetarget');
  });
});
