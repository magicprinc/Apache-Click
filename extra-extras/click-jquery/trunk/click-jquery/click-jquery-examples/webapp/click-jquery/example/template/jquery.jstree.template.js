//jQuery.taconite.debug = true;
jQuery(document).ready(function(){
    // Make sure that the Click namespace exists
    if( typeof Click == 'undefined' )
        Click = {};

    // Make sure that the Click.jquery namespace exists
    if( typeof Click.jquery == 'undefined' )
        Click.jquery = {};

    // Make sure that the Click.jquery.jstree namespace exists
    if( typeof Click.jquery.jstree == 'undefined' )
        Click.jquery.jstree = {};

    var applyPreSelect = false;
    var id = jQuery("$selector").attr("id");
    var callbackUrl = '$context$path?'+id+'=1&callback=OPEN';
		var tree = jQuery("$selector").tree({
        rules : {
          multiple : 'ctrl'
        },

        types : {
           "file" : {
                    creatable : true,
                    valid_children : [ "none" ],
                    icon : {
                        image : "$context/click-jquery/example/jstree/images/file.png"
                    }
           },
           "drive" : {
                    creatable : true,
                    valid_children : [ "driver" ],
                    icon : {
                        image : "$context/click-jquery/example/jstree/images/drive.png"
                    }
           }
        },

        //languages : [ "en", "af" ],

        data : {
           type : "html",
           opts : {
           }
        },

        selected: [$selected],

        callback : {

          /*
           * Used for passing custom parameters to the server. Receives two
           * parameters - a node (if applicable) and a reference to the tree
           * instance.
           */
          beforedata: function(NODE, TREE){
              return { nodeId : $(NODE).attr("id") || 0 };
          }

          /*
           * Triggered when a node is created. Receives five parameters - the node
           * that was created, the reference node in the create operation, the new
           * position relative to the reference node (one of "before", "after" or "inside"),
           * a reference to the tree instance and a rollback object that you can use
           * with jQuery.tree.rollback(RB).
           */
          /*
          // oncreate is broken in JsTree 0.99a as it is fired before the node value is set
          oncreate : function(NODE, REF_NODE, TYPE, TREE, RB){
              var nodeValue = TREE.get_text(NODE);
              var refNodeId = $(REF_NODE).attr("id");
              var parameters = 'type='+TYPE+'&nodeValue='+value+'&refNodeId='+refNodeId;
              notifyServer('CREATE', parameters);
          }
          */

          #if ($jstree.closeListener)
          /*
           * Triggered when a node is closed. Receives two parameters
           *  - the node that was closed and a reference to the tree instance.
           */
            ,onclose: function(NODE, TREE){
              var nodeId = $(NODE).attr("id");
              var parameters = 'nodeId='+nodeId;
              notifyServer('CLOSE', parameters);
            }
          #end

          #if ($jstree.deleteListener)
          /*
           * Triggered when a node is deleted. Receives three parameters
           * - the node that was deleted, a reference to the tree instance and a
           * rollback object that you can use with jQuery.tree.rollback(RB).
           */
            ,ondelete: function(NODE, TREE, RB){
              var nodeId = $(NODE).attr("id");
              var parameters = 'nodeId='+nodeId;
              notifyServer('DELETE', parameters);
            }
          #end

          #if ($jstree.changeListener)
          /**
           * Triggered when selection is changed in any way (select or deselect).
           * Receives two parameters - the node involved and a reference to the
           * tree instance.
           */
            ,onchange : function (NODE) {
              // guard against infinite loop
              if(!applyPreSelect) {
                return;
              }
              var nodeId = $(NODE).attr("id");
              var href = $(NODE).children("a:eq(0)").attr("href");
              if (nodeId==null || nodeId=="undefined" || nodeId=="") {
                // Probably a newly created node - do nothing
              } else if (href==null||href=="undefined"||href==""||href.charAt(href.length-1)=='#') {
                // Notify server of selection
                var parameters = 'nodeId='+nodeId;
                notifyServer('CHANGE', parameters);
              } else {
                // If an href is specified, follow the link
                document.location.href = href;
              }
            }
          #end

          #if ($jstree.moveListener)
          /*
           * Triggered when a node is moved. Receives five parameters - the node
           * that was moved, the reference node in the move, the new position
           * relative to the reference node (one of "before", "after" or "inside"),
           * a reference to the tree instance and a rollback object that you can
           * use with jQuery.tree.rollback(RB).
           */
            ,onmove: function(NODE, REF_NODE, TYPE, TREE, RB){
              var nodeId = $(NODE).attr("id");
              var refNodeId = $(REF_NODE).attr("id");
              var parameters = 'nodeId='+nodeId+'&type='+TYPE+'&refNodeId='+refNodeId;
              notifyServer('MOVE', parameters);
            }
          #end

          /*
           * Triggered when the tree is loaded with data for the first time or
           * refreshed. Receives one parameter - a reference to the tree instance.
           */
          ,onload : function(TREE) {

              #if ($jstree.openListener)
                TREE.settings.data.async = true;
                TREE.settings.data.opts.url = callbackUrl;
              #end

              applyPreSelect = true;

              // Clear the float:left CSS style
              TREE.container.append($("<div>").css({ "clear" : "both" }));
          }

          #if ($jstree.renameListener)
          /*
           * Triggered when a node is renamed. Receives three parameters
           * - the renamed node, a reference to the tree instance and a rollback
           *  object that you can use with jQuery.tree.rollback(RB).
           */
            ,onrename: function(NODE, TREE, RB){
              var nodeId = $(NODE).attr("id");
              var nodeValue = TREE.get_text(NODE);
              var parameters = 'nodeId='+nodeId+'&nodeValue='+nodeValue;
              if (nodeId == null || nodeId == "undefined" || nodeId == "") {
                notifyServerAndUpdateId(NODE, 'CREATE', parameters);
              } else {
                notifyServer('RENAME', parameters);
              }
            }
          #end

          // -------------------------------------------------- Unused callbacks

          #if ($jstree.selectListener)
          /**
           * Triggered when a node is selected. Receives two parameters
           * - the selected node and a reference to the tree instance.
           */
            ,onselect : function(NODE, TREE) {
              // guard against infinite loop
              if(!applyPreSelect) {
                return;
              }
              var href = $(NODE).children("a:eq(0)").attr("href");
              if (href==null || href=="undefined" || href=="") {
                // Probably a newly created node - do nothing
              } else if (href.charAt(href.length-1)=='#') {
                // Notify server of selection
                var nodeId = $(NODE).attr("id");
                var parameters = 'nodeId='+nodeId;
                notifyServer('SELECT', parameters);
              } else {
                // If an href is specified, follow the link
                document.location.href = href;
              }
            }
          #end

          #if ($jstree.deselectListener)
          /*
           * Triggered when a node is deselected. Receives two parameters
           * - the deselected node and a reference to the tree instance.
           */
            ,ondeselect : function(NODE, TREE) {
              var href = $(NODE).children("a:eq(0)").attr("href");
              if (href && href.charAt(href.length-1)=='#') {
                // Notify server of deselection
                var nodeId = $(NODE).attr("id");
                var parameters = 'nodeId='+nodeId;
                notifyServer('DESELECT', parameters);
              }
            }
          #end

          /*
           * Used for passing custom parameters to the server. Receives two
           * parameters - a node (if applicable) and a reference to the tree instance.
           */
          /*
          ,beforedata : function(NODE, TREE) {
              return { id : $(n).attr("id") || 0 }
          }
          */

          /*
           * Triggered when the tree is initialized and before any data is
           * requested or parsed. Receives one parameter - a reference to the
           * tree instance.
           */
          /*
          ,oninit : function(TREE) {
          }
          */
        },

        plugins: {
            #if(${jstree.isContextMenuEnabled()})
              contextmenu: {
                items : {
                    create : {
                        label : "Create",
                        action : function (NODE, TREE) {
                            TREE.create(false, TREE.get_node(NODE[0]));
                        }
                    }
                }
            }
            #end
        }
    });

    function notifyServer(callback, args) {
      var data = 'callback='+callback+'&'+args;
      jQuery.ajax({
        'type' : "GET",
				'url'	: '$context$path?'+id+'=1',
				'data' : data,
				'success'	: function (d, textStatus) {
          // Empty callback
				},
				'error' : function (xhttp, textStatus, errorThrown) {
					tree.error(errorThrown + " " + textStatus);
				}
      });
    }

    function notifyServerAndUpdateId(NODE, callback, args) {
      var data = 'callback='+callback+'&'+args;
      jQuery.ajax({
        'type' : "GET",
				'url'	: '$context$path?'+id+'=1',
				'data' : data,
				'success'	: function (xml, textStatus) {
          var nodeId = extractNodeId(xml);

          if (nodeId) {
              $(NODE).attr("id", nodeId);
          }
				},
				'error' : function (xhttp, textStatus, errorThrown) {
					tree.error(errorThrown + " " + textStatus);
				}
      });
    }

    function extractNodeId(xml) {
      var nodeId = null;
      jQuery(xml).find("custom").each(function() {
          var custom = $(this);
          if (custom.attr("name") == "nodeId") {
              nodeId = custom.attr("value");
              return false;
          }
      });
      return nodeId;
    }
    //jQuery.tree.reference("#basic_html").set_lang("af");
});
