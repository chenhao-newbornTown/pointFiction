<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page pageEncoding="UTF-8"%>
<%@ include file="taglibsnew.jsp"%>
<%@ include file="glbVariable.jsp"%>
<%@ include file="easyUItag.jsp"%>

<html>
<head>
    <title></title>

    <link rel="stylesheet" type="text/css" href="<c:url value='/ps/style/comm.css'/>"/>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='${otherPath}/displaytag.css'/>" />

</head>

<body>
<div align="center" style="width:100%;height: auto">
    <form name="fictionDetailForm"  method="post">

        <display:table name="fictionDetailBeanList" cellspacing="0" cellpadding="0" style="width:100%"
                       requestURI="" id="fdb"
                       class="list reportsList" >
            <display:column sortable="true" headerClass="sortable" style="width:10%" title="角色名"><c:out value="${fdb.actor_name}"/></display:column>
            <display:column sortable="true" headerClass="sortable" style="width:90%" title="内容"><c:out value="${fdb.actor_fiction_detail}"/></display:column>

        </display:table>
    </form>
</div>

</body>

</html>
