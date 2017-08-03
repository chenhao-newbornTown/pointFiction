<!DOCTYPE html>
<html>
<%@ page pageEncoding="UTF-8"%>
<%@ include file="taglibs.jsp"%>
<%@ include file="glbVariable.jsp"%>
<head>
<title></title>

	<link rel="stylesheet" href="<c:url value='ps/uitl/jmenu/css/styles.css'/>" type="text/css" />
	<link rel="stylesheet" href="<c:url value='ps/uitl/jmenu/css/jquery-tool.css'/>" type="text/css" />
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value='${eaUIPath}/themes/bootstrap/easyui.css'/>" />
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value='${eaUIPath}/themes/icon.css'/>" /> 

	<script type="text/javascript" src="<c:url value='ps/uitl/jmenu/js/jquery.tools.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='ps/uitl/jmenu/js/main.js'/>"></script>
	
	<script type="text/javascript" src="<c:url value='${eaUIPath}/jquery.min.js'/>"></script> 
	<script type="text/javascript" src="<c:url value='${eaUIPath}/jquery.easyui.min.js'/>"></script>  

<script type="text/javascript">

 function addPanel(tname,url){
	window.parent.window.addPaneltab(tname,url);
} 
</script>
</head>
<body>
	
	<div id="menucontent" class="menucontent">
	
		<ul id="expmenu-freebie">
			<li>
				<ul class="expmenu" style="width:180px">
					<li>
						<div class="jmenuheader">
						<span class="arrow up"></span>
						<span class="label" style="background-image: url(<c:url value='/ps/uitl/jmenu/images/messages.png'/>);">小说管理</span>
							
						</div>
						<ul class="jmenumenu">
							<li class="submenu" onclick="addPanel('热门小说管理','<c:out value='${contextPath}'/>/hotfictionlist')"><a target="mainFrame">热门故事管理</a></li>
							<li class="submenu" onclick="addPanel('上传小说','<c:out value='${contextPath}'/>/editfictionall')"><a target="mainFrame">新建/编辑 故事</a></li>
							<li class="submenu" onclick="addPanel('图片管理','<c:out value='${contextPath}'/>/uploadpic')"><a target="mainFrame">图片管理</a></li>
						</ul>
					</li>
					<%--<li>--%>
						<%--<div class="jmenuheader">--%>
						<%--<span class="arrow up"></span>--%>
						<%--<span class="label" style="background-image: url(<c:url value='/ps/uitl/jmenu/images/messages.png'/>);">系统参数</span> --%>
							<%----%>
						<%--</div>--%>
						<%--<ul class="jmenumenu">--%>
							<%--&lt;%&ndash;<li class="submenu" onclick="addPanel('收益统计','<c:out value='${contextPath}'/>/siaContro/searchStrateAnnalList')"><a target="mainFrame">收益统计</a></li>--%>
							<%--&ndash;%&gt;<li class="submenu" onclick="addPanel('参数配置','<c:out value='${contextPath}'/>/sccContro/searchConfigCollList')"><a target="mainFrame">参数配置</a></li>--%>
						<%--</ul>--%>
					<%--</li>--%>
				</ul>
			</li>
		</ul>
	</div>
</body>
</html>
