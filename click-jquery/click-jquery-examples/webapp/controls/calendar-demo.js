$(document).ready(function(){
    $('#form_calendar').datepicker({
        showOn: 'button',
        buttonImageOnly: true,
        buttonImage: '$context/click/calendar/calendar.gif',
        constrainInput: true,
        changeMonth: true,
        changeYear: true,
        yearRange: '1930:2050',
        dateFormat: 'dd MM yy'
    });
    $('#form_calendar').next().css("vertical-align", "top");
});