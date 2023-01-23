$().ready(function(){
    $('#slider_container').slider({
        change: function() {
            var val = $('#slider_container').slider("value");
            $("#slider").val(val);
        },
        range: false,
        value: $('#slider').val()
    });
});