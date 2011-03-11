$(document).ready(function() {	
    $("#autocomplete").autocomplete({
        minLength: 2,
        focus: function(event, ui) {
            return false;
        },
        select: function(event, ui) {
            var formInput = document.getElementById("autocomplete");
            formInput.value = ui.item.value;
            $("#searchForm").submit();
        }, source : "suggestionJson"
    }).data("autocomplete")._renderItem = function(ul, item) {
        return $("<li></li>")
                .data("item.autocomplete", item)
                .append("<a>" + item.value + "</a>")
                .appendTo(ul);
    };
});

function doSubmit() {
    $("#searchForm").submit();
}
