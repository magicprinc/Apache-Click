plot = jQuery.jqplot('chart', [[$data]], {
    title: '$label',
    axes:{
        yaxis: {
            tickOptions: {
                formatString: '%.0f'
            }
        },
        xaxis: {
            min: 2006,
            tickInterval:1,
            tickOptions: {
                formatString: '%.0f'
            }
        }
    }
});
