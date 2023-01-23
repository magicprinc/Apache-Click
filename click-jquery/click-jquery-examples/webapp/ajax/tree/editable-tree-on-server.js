jQuery(document).ready(function(){
    jQuery("#form_save, #form_delete").live("click", function(){
        var focusedTree = jQuery.tree.focused();
        if(!focusedTree || !focusedTree.selected) {
            alert("Please select a node first.");
            return false;
        }
    })
})
