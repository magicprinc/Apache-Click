plot = jQuery.jqplot('chart', [$data], {
  title: '$label',
  axes: {
      xaxis:{
          min: 2006,
          renderer:jQuery.jqplot.CategoryAxisRenderer,
          tickOptions: {formatString: '%.0f'}
      },
      yaxis:{
          min: 0,
          max: 100,
          tickOptions: {formatString: '%.0f'}
      }
  },
  seriesDefaults:{renderer:jQuery.jqplot.BarRenderer, rendererOptions:{barPadding:10, barMargin:10}},
  series:[{label:"Internet Explorer", color: "#0FA4D7"}, {label:"Firefox", color:"#FF780F"}, {label:"Safari", color:"#999999"}, {label:"Opera", color:"#C70C00"}],
  legend: {show:true, location: 'ne', xoffset: -140, yoffset: 0}
});
