<%@ include file="/common/taglibs.jsp"%>


<head>
    <title><fmt:message key="mainMenu.title"/></title>
    <%--<meta name="heading" content="<fmt:message key='mainMenu.heading'/>"/>--%>
    <meta name="menu" content="MainMenu"/>
    <style type="text/css" media="all">
    	div.wwlbl{
    		display: inline !important;
    	}
    </style>
</head>
<h1><fmt:message key="search.title"/></h1>	
<p>
	<s:form action="mainMenu">
		<s:textfield key="search"  required="true" />
		<s:textfield key="hopCount" />
		<s:textfield key="maxTopicNumberToExpand" />
		<s:textfield key="thresholdWeight" />
		<li>
			<s:checkbox key="prefixMatch"/>
		</li>
		<li>
			<s:checkbox key="fuzzyMatch"/>
		</li>
		<li>
			<s:submit key="submitButton" method="search"  />
		</li>
	</s:form>
	<br />

	<s:set name="result"  value="result" scope="request"/>
	<c:if test="${not empty result}">
		<h3><fmt:message key="search.result"/></h3>
	    <display:table class="table" name="result" export="false" id="row" requestURI="mainMenu.do">
	  		<display:column title="Title"><a href="<c:url value="fr.do?docID=${row.ID}" />" target="_blank"><c:out value="${row.title}"/></a> </display:column>
	        <display:column title="URI"><a href="<c:out value="${row.URI}"/>" target="_blank"><c:out value="${row.URI}"/></a> </display:column>
	  		<display:column property="fileType" title="File Type"/>
	  		<display:column property="scoreStr" title="Score"/>
	  		<display:column property="lastModified" title="Last Modified"/>
		</display:table>
		<h3><fmt:message key="topic.expansion"/></h3>
		<%@ include file="/common/tree.jsp" %>
	</c:if>
	<c:if test="${empty result}">	
     <h3><fmt:message key="no.result"/></h3>	
	</c:if>
	
</p>	
	



