function saveModifiedId(relationtypeId) {
	document.getElementById("modifiedRelationtypeId").value = relationtypeId;
	document.getElementById("modifiedWeightValue").value = document.getElementById(relationtypeId+"_weight").value;
	//alert("Modified relId: " + relationtypeId);
	//alert("Modified weight value: " + document.getElementById(relationtypeId+"_weight").value);
	document.weightForm.submit();
}