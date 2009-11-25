<%@ include file="/common/taglibs.jsp"%>

<head>
	<sx:head />
	<%--<sx:head debug="true" cache="false" compressed="false"/>--%>
    <title><fmt:message key="mainMenu.title"/></title>
    <script type="text/javascript" src="<c:url value="/scripts/relationtype.js"/>"></script>
    <link rel=stylesheet href="<c:url value="/styles/mainmenu.css"/>" type="text/css" media=all />
</head>
<div id="mainDiv">
	<div id="leftSideDiv">
		<h1><fmt:message key="search.title"/></h1>
		<s:form action="mainMenu" >
			<s:textfield key="search"  required="true" />
			<s:textfield key="hopCount" />
			<s:textfield key="maxTopicNumberToExpand" />
			<s:textfield key="expansionThreshold" />
			<s:textfield key="resultThreshold" />
			<s:textfield key="maxNumberOfHits" />
			<li>
				<s:checkbox key="prefixMatch"/>
			</li>
			<li>
				<s:checkbox key="fuzzyMatch"/>
			</li>
			<li>
				<s:submit key="submitButton" method="search" />
			</li>
		</s:form>
		<br />
		
		<s:set name="relationTypes"  value="relationTypes" scope="request"/>
		<c:if test="${not empty relationTypes}">
			<h1><fmt:message key="relationTypes.result"/></h1>
			<display:table class="table" name="relationTypes" defaultsort="2" defaultorder="ascending" export="false" id="relationType" requestURI="mainMenu.do">
				<display:column property="id" title="Id" style="width:90px;"/>
				<display:column property="relationName" title="Relation name"/>
				<display:column title="Weight ahead">
					<input type="text" id="${relationType.id}_weight" value="${relationType.weight}" onkeyup="updateRecord(this.id)" class="editable" />
				</display:column>
				<display:column title="Weight aback">
					<input type="text" id="${relationType.id}_genweight" value="${relationType.generalizationWeight}" onkeyup="updateRecord(this.id)" class="editable" />
				</display:column>
				<display:column>
					<s:form action="updateWeight" method="post">
						<li>
							<input type="text" style="display: none;" name="modifiedRelationtypeId" value="${relationType.id}" />
							<input type="text" style="display: none;" id="${relationType.id}_form_weight" name="modifiedWeightValue" value="${relationType.weight}" />
							<input type="text" style="display: none;" id="${relationType.id}_form_genweight" name="modifiedGenWeightValue" value="${relationType.generalizationWeight}" />
							<sx:submit value="Modify" onmouseup="resetHighlight(this)"/>
						</li>
					</s:form>
				</display:column>
			</display:table>
		</c:if>
		
	</div>
	
	<div id="rightSideDiv">
	<s:set name="firstTime" value="firstTime" scope="request"/> 
	<s:set name="treeEmpty" value="treeEmpty" scope="request"/> 
	
		<s:set name="result"  value="result" scope="request"/>
		<s:set name="resultSize"  value="resultSize" scope="request"/>
		<s:set name="filteredResultSize"  value="filteredResultSize" scope="request"/>
		<c:if test="${not firstTime }">
		    <c:if test="${not treeEmpty }">
			  <h1><fmt:message key="topic.expansion"/></h1>
			  <s:set name="treeJson"  value="treeJson" scope="request"/>
			  <%@ include file="/common/tree.jsp" %>
			  <br />
		    </c:if>
		    <c:if test="${not empty result}">
			<h3><fmt:message key="search.result"/>
				<s:property value="resultSize"/>/
				<s:property value="filteredResultSize"/>
			</h3>
			<display:table class="table" name="result" defaultsort="2" defaultorder="descending" export="false" id="row" requestURI="mainMenu.do">
				<display:column title="Title"><a href="<c:url value="${row.URI}"/>" target="_blank"><c:out value="${row.title}"/></a> </display:column>
				<display:column property="scoreStr" title="Score"/>
			</display:table>
		</c:if>
		</c:if>
		<c:if test="${empty result and not firstTime }">	
		 <h3><fmt:message key="no.result"/></h3>	
		</c:if>

	</div>
	
</div>