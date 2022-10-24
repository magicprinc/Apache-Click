jQuery(document).ready(function(){
    var focusedTree = jQuery.tree.focused();
    if (!focusedTree && focusedTree.selected) {
        // If no focused tree and node, exit early
        return;
    }
    var focusedNode = focusedTree.selected;
    var nodeName = jQuery('#form_name').val();
    var nodeType = jQuery('#form_nodeType').val();
    var href = jQuery('#form_path').val();

    // On successful form submission, we create tree node if id is returned
    // else we update the selected node
    var nodeId;
    #if ($nodeId)
        nodeId = '$nodeId';
    #end

    if (nodeId) {
      focusedTree.create({ attributes: {id : nodeId, rel : nodeType, href : href}, data : nodeName });
    } else {
      focusedTree.rename(focusedNode, nodeName);
      focusedTree.set_type(nodeType, focusedNode);
      jQuery(focusedNode).children("a:eq(0)").attr("href", href);
    }
})
