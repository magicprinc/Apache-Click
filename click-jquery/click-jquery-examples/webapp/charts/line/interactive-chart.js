plot = jQuery.jqplot('chart', [[$data]], {
    title: '$label',
    axes:{
        xaxis:{
            renderer:jQuery.jqplot.DateAxisRenderer,
            min:'January 1, 2009',
            tickInterval:'1 month',
            rendererOptions:{
                tickRenderer:jQuery.jqplot.CanvasAxisTickRenderer},
            tickOptions:{formatString:'%b %#d, %Y', fontSize:'10pt', fontFamily:'Tahoma', angle:-40, fontWeight:'normal', fontStretch:1},
            numberTicks: 4
        },
        yaxis: {
            tickOptions: {
                formatString: '$%.2f'
            }
        }
    },
    series:[{lineWidth:4}],
    highlighter: {
       sizeAdjust: 10,
       tooltipLocation: 'n',
       tooltipAxes: 'y',
       tooltipFormatString: '<b><i><span style="color:red;">revenue</span></i></b> %.2f',
       useAxesFormatters: false
   },
   cursor: {
       show: true
   }

});
