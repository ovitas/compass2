function saveModifiedId(relationtypeId) {
	document.getElementById("modifiedRelationtypeId").value = relationtypeId;
	document.getElementById("modifiedWeightValue").value = document.getElementById(relationtypeId+"_weight").value;
	document.getElementById("modifiedGenWeightValue").value = document.getElementById(relationtypeId+"_genweight").value;
	//alert(relationtypeId);
	//alert(document.getElementById(relationtypeId+"_weight").value);
	//alert(document.getElementById(relationtypeId+"_genweight").value);
	document.weightForm.submit();
}