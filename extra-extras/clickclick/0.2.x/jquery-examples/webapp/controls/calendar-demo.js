$().ready(function(){
    $('#calendar_field').datepicker({
        showOn: 'button',
        buttonImageOnly: true,
        buttonImage: '$context/click/calendar/calendar.gif',
        constrainInput: true,
        changeMonth: true,
        changeYear: true,
        yearRange: '1930:2050',
        dateFormat: 'dd MM yy'
    });
    $('#calendar_field').next().css("vertical-align", "top");
});