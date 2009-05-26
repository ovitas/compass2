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
	
<p>
	<s:form action="mainMenu">
		<s:textfield key="search"  required="true" />
		<s:textfield key="hopCount" />
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
	    <display:table class="table" name="result" export="false" id="row" requestURI="mainMenu.do">
	  		<display:column property="title"/>
	  		<display:column property="URI"/>
	  		<display:column property="fileType"/>
	  		<display:column property="score"/>
	  		<display:column property="lastModified"/>
		</display:table>
		<%@ include file="/common/tree.jsp" %>
	</c:if>
</p>	
	
<%--<p><fmt:message key="mainMenu.message"/></p>--%>

<%--<div class="separator"></div>--%>

<%--<ul class="glassList">
    <li>
        <a href="<c:url value='/editProfile.html'/>"><fmt:message key="menu.user"/></a>
    </li>
    <li>
        <a href="<c:url value='/uploadFile.html'/>"><fmt:message key="menu.selectFile"/></a>
    </li>
</ul>--%>


