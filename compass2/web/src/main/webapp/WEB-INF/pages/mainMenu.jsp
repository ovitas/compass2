<%@ include file="/common/taglibs.jsp"%>


<head>
	<sx:head />
    <title><fmt:message key="mainMenu.title"/></title>
    <%--<meta name="heading" content="<fmt:message key='mainMenu.heading'/>"/>--%>
    <meta name="menu" content="MainMenu"/>
    <script type="text/javascript" src="<c:url value="/scripts/relationtype.js"/>"></script>
    <link rel=stylesheet href="<c:url value="/styles/mainmenu.css"/>" type="text/css" media=all />
</head>
<div id="mainDiv">
	<div id="leftSideDiv">
		<h1><fmt:message key="search.title"/></h1>
		<s:form action="mainMenu">
			<s:textfield key="search"  required="true" />
			<s:textfield key="hopCount" />
			<s:textfield key="maxTopicNumberToExpand" />
			<s:textfield key="expansionThreshold" />
			<s:textfield key="resultThreshold" />
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
		
		<s:set name="relationTypes"  value="relationTypes" scope="request"/>
		<c:if test="${not empty relationTypes}">
			<h1><fmt:message key="relationTypes.result"/></h1>
			<display:table class="table" name="relationTypes" defaultsort="1" defaultorder="ascending" export="false" id="relationType" requestURI="mainMenu.do">
				<display:column property="relationName" title="Relation name"/>
				<display:column title="Weight">
					<input type="text" id="${relationType.id}_weight" value="${relationType.weight}" />
				</display:column>
				<display:column>
					<a href class="link" onclick="saveModifiedId('${relationType.id}');"><fmt:message key="modifyButton" /></a>
				</display:column>
			</display:table>
		</c:if>
		<br />
	
		<s:form name="weightForm" action="mainMenu" theme="ajax">
			<input type="text" id="modifiedRelationtypeId" name="modifiedRelationtypeId" value="" style="display: none;" />
			<input type="text" id="modifiedWeightValue" name="modifiedWeightValue" value="" style="display: none;" />
		</s:form>
		
	</div>
	
	<div id="rightSideDiv">
		<s:set name="result"  value="filteredResult" scope="request"/>
		<c:if test="${not empty result}">
			<h1><fmt:message key="topic.expansion"/></h1>
			<%@ include file="/common/tree.jsp" %>
			<br />

			<h3><fmt:message key="search.result"/>
				<s:property value="resultSize"/>/
				<s:property value="filteredResultSize"/>
			</h3>
			<display:table class="table" name="result" defaultsort="2" defaultorder="descending" export="false" id="row" requestURI="mainMenu.do">
				<display:column title="Title"><a href="<c:url value="${row.URI}"/>" target="_blank"><c:out value="${row.title}"/></a> </display:column>
				<display:column property="scoreStr" title="Score"/>
			</display:table>
		</c:if>
		<c:if test="${empty result}">	
		 <h3><fmt:message key="no.result"/></h3>	
		</c:if>

	</div>
	
</div>