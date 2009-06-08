<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="mainMenu.title"/></title>
    <%--<meta name="heading" content="<fmt:message key='mainMenu.heading'/>"/>--%>
    <meta name="menu" content="MainMenu"/>
    <h3>Results</h3>
    <s:label name="search" label="Search" />
    <s:label name="hopCount" label="Hop Count" />
	<s:textfield key="maxTopicNumberToExpand" />
    
    <s:label name="thresholdWeight" label="Threshold Weight" />
    <s:label name="prefixMatch" label="Prefix Match" />
    <s:label name="fuzzyMatch" label="Fuzzy Match" />
    
    <s:set name="result"  value="result" scope="request"/>
    <display:table name="result" export="true" id="row" requestURI="mainMenu.do">
  		<display:column property="title"/>
  		<display:column property="URI"/>
	</display:table>
    
</head>
	
