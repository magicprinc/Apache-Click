$(document).ready(function(){

    /* Demo 1 */
    $("#demo1-but").click(function(event) {
        $("#demo1-box").slideToggle(function(){
            // Switch button description based on whether the div is visible or not
            $("#demo1-box").is(':visible') ? $("#demo1-but").html("Close") : $("#demo1-but").html("Open")
        });
    });

    /* Demo 2 */
    $("#demo2-but").click(function(event){
        $("#demo2-box")
        .animate({width: "+=70%", opacity: 0.4, marginLeft: "+=0.6in", fontSize: "+=3em", borderWidth: "10px"})
        .animate({width: "-=70%", opacity: 1,   marginLeft: "-=0.6in", fontSize: "-=3em", borderWidth: "2px"})
    });

    /* Demo 3 */
    $(".demo3-but").click(function(event){
        $("#demo3-box")
        .animate({opacity: "0.5"}, "slow")
        .animate({height: "-=50", width: "-=50", left: "+=150"})
        .animate({top: "+=150"})
        .animate({height: "+=50", width: "+=50", left: "0"})
        .animate({top: "0"})
        .animate({opacity: "1"}, "slow")
        return false;
    });
});
