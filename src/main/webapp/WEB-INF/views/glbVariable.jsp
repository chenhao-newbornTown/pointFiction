<c:set var="hostPrefix"><c:out value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}"/></c:set>
<c:set var="contextPath"><c:out value="${pageContext.request.contextPath}"></c:out></c:set>
<c:set var="systemName">
	<%=request.getContextPath().replaceAll("^/", "")%>
</c:set>
<c:set var="imgPath">/theme/default/skin01/imgs</c:set>
<c:set var="cssPath">/theme/default/skin01/style</c:set>
<c:set var="otherPath">/ps/style</c:set>
<c:set var="eaUIPath">/ps/framework/easyui</c:set>
<c:set var="ecPath">/ps/framework/echarts</c:set>
<c:set var="tableCss">/ps/framework/common</c:set>
<c:set var="bootStrapCss">/ps/framework/bootstrap</c:set>

<c:set var="util">/ps/uitl</c:set>