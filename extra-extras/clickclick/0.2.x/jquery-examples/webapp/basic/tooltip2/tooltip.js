$(document).ready(function() {
    // select all desired input fields and attach tooltips to them
    $("#form input:not(:hidden)").tooltip({

        // place tooltip on the right edge
        position: ['center', 'right'],

        // a little tweaking of the position
        offset: [-2, 10],

        // custom opacity setting
        opacity: 0.7
    });
});
