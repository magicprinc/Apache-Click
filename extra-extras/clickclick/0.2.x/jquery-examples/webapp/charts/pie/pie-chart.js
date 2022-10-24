plot = jQuery.jqplot('chart', [[$data]], {
  title: '$label',
  seriesDefaults:{renderer:jQuery.jqplot.PieRenderer, rendererOptions:{sliceMargin:8}},
  legend:{show:true}
});
