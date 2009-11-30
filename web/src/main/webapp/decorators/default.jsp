<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
        <%@ include file="/common/meta.jsp" %>
        <title><decorator:title/> | <fmt:message key="webapp.name"/></title>

        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/theme.css'/>" />
        <link rel="stylesheet" type="text/css" media="print" href="<c:url value='/styles/${appConfig["csstheme"]}/print.css'/>" />
        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/ext-all.css'/>" />

        <script type="text/javascript" src="<c:url value='/scripts/prototype.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/scriptaculous.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/global.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/ext/ext-base.js'/>"></script>        
        <script type="text/javascript" src="<c:url value='/scripts/ext/ext-all.js'/>"></script>
        <decorator:head/>
    </head>
<body<decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" writeEntireProperty="true"/>>


	<%@ include file="/common/messages.jsp" %>
	<h1><decorator:getProperty property="meta.heading"/></h1>
	<decorator:body/>


	<div id="footer" class="clearfix">
		<jsp:include page="/common/footer.jsp"/>
	</div>

</body>
</html>