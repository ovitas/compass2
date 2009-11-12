function updateRecord(id) {
	// Load data into form
	arr = id.split("_");
	document.getElementById(arr[0] + "_form_" + arr[1]).value = document.getElementById(id).value;
	
	// Highlight modified input
	document.getElementById(id).style.border = "solid 1px #FF0000";
}

function resetHighlight(id) {
	//alert(id);
	inputArray = id.getElementsByTagName("input");
	alert(inputArray[0].id);
	/*document.getElementById(id + "_weight").style.border = "solid 1px #FEFEFE";
	document.getElementById(id + "_genweight").style.border = "solid 1px #FEFEFE";*/
}