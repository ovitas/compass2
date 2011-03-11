function updateLink(formId, link) {
	var form = document.getElementById(formId + 'Form');
	var anchor = document.getElementById(formId + 'A');
	var params = link.match(/{[^}]*}/g);

	for (var i = 0; i < params.length; i++) {
		var param = params[i];
		var paramName = param.substring(1, param.length - 1);
		var paramValue = document.getElementById(formId + '_' + paramName).value;

		if (paramValue != '')
			link = link.replace(param, paramValue);

		if (link.indexOf('{') > -1) {
			anchor.href = '#';
		} else {
			anchor.href = link;
		}
		anchor.innerHTML = link;
	}
}

function showDiv(divId) {
	var divs = document.getElementsByTagName('div');
	
	for (var i = 0; i < divs.length; i++) {
		var div = divs[i];
		
		div.style.display = 'none';
	}
	
	document.getElementById(divId).style.display = 'block';
}

function updateConfSearchType(type) {
	var form = document.getElementById('ConfSearchForm');
	form.action = type;
	
	var div = document.getElementById('ConfSearch_TopicQuery'); 
	if (type == 'compass2/topicList' || type == 'compass2/topicTree' || type == 'compass2/search') {
		div.style.display = 'block';
	} else {
		div.style.display = 'none';
	}
	
	var div = document.getElementById('ConfSearch_FullTextQuery'); 
	if (type == 'compass2/search') {
		div.style.display = 'block';
	} else {
		div.style.display = 'none';
	}
}