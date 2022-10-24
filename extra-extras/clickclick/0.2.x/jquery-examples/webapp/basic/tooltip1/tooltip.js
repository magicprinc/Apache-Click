$(document).ready(function() {
  $('#form input').cluetip({
    splitTitle: '|', // use the invoking element's title attribute to populate the clueTip...
                     // ...and split the contents into separate divs where there is a "|"
    showTitle: false, // hide the clueTip's heading
    arrows: true
  });
});
