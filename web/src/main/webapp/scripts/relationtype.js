function updateRecord(id) {
	// Load data into form
	arr = id.split("_");
	document.getElementById(arr[0] + "_form_" + arr[1]).value = document.getElementById(id).value;
	
	// Highlight modified input
	document.getElementById(id).style.border = "solid 1px #FF0000";
}

function resetHighlight(object) {
	div= object.parentNode;
    inputs = div.parentNode.getElementsByTagName('input');
    //alert(inputs[1].id);
    recordID = inputs[1].id.split("_")[0];
	document.getElementById(recordID + "_weight").style.border = "solid 1px #74B3DC";
	document.getElementById(recordID + "_genweight").style.border = "solid 1px #74B3DC";
}