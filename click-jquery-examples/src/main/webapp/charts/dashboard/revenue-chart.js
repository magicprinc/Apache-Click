plot = jQuery.jqplot('$selector', [[$data]], {
    title: '$label',
    axes:{
        xaxis:{
            renderer:jQuery.jqplot.DateAxisRenderer,
            min:'January 1, $year',
            tickInterval:'1 month',
            rendererOptions:{
                tickRenderer:jQuery.jqplot.CanvasAxisTickRenderer},
            tickOptions:{formatString:'%b %#d, %Y', fontSize:'10pt', fontFamily:'Tahoma', angle:-40, fontWeight:'normal', fontStretch:1}
        },
        yaxis: {
            tickOptions: {
                formatString: '$%.2f'
            }
        }
    },
    series:[{lineWidth:4, markerOptions:{style:'square'}}]
});
