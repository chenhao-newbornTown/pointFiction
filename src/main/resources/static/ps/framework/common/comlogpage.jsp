<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/ps/framework/common/taglibs.jsp"%>
<%@ include file="/ps/framework/common/glbVariable.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>KRM</title>


		<link type="text/css" href="jqueryTab/login.css" rel="stylesheet" />	
		
		<script type="text/javascript" src="jqueryTab/jquery-1.3.2.min.js"></script>
		<script type="text/javascript" src="jqueryTab/easyTooltip.js"></script>
		<script type="text/javascript" src="jqueryTab/jquery-ui-1.7.2.custom.min.js"></script>
		
		
		<link rel="stylesheet" type="text/css" href="jqueryTab/lanrenzhijia.css" media="all" />
		<script type="text/javascript" src="jqueryTab/execute.js"></script>

<script language="javascript" type="text/javascript" src="<c:url value='${util}/My97DatePicker/WdatePicker.js'/>"></script>


</head>
<body style="overflow-y: hidden;">
<div id="container">
<div class="logo">
			<%--<a href="#"><img src="../images/east_logo_w.png" alt="" /></a>--%>
		</div>
<div id="box">
<form method="post" action="loginAction.do?method=login" name="form1">
                        	 <p class="main" style="width: 100%">
								<label for="user_name"><fmt:message key="usermanage.sysadmin.label.username"/></label>
								<input name="logonname" id="user_name" type="text"  style="width: 100;height: 25"/>
						
								<label for="user_password"><fmt:message key="usermanage.sysadmin.label.password"/></label>
								<input name="password" id="user_password" type="password" style="width: 100;height: 25"/>
							
								<label for="user_password"><fmt:message key="logpage.date"/></label>
								<input id="logindate" name="logindate" type="text" 
									style="width:100;height: 25" readonly="true" onClick="WdatePicker()"/>
							</p>
							
							<p class="space">
								<input type="button" value="<fmt:message key='denglu'/>" class="login" onclick="getlogin();"/>
							</p>
		
					</form>
</div>

				<div id="b22" style="visibility: hidden;">
					<ul id="nav-shadow">
					    <li class="button-color-2"><a href="#" onclick="login();" title="East">East</a></li>
					    <li class="button-color-3"><a href="#" title="My fancy link">Link Text</a></li>
					    <li class="button-color-4"><a href="#" title="My fancy link">Link Text</a></li>
					 </ul>
				</div>
</div>
</body>
</html>
<script type="text/javascript">
function getlogin(){
	if($("#user_name").val()==""){
		alert('<fmt:message key="logpage.xname"/>');
		return false;
	}
	var param={"logname":$("#user_name").val(),"logpasd":$("#user_password").val(),"logdate":$("#logindate").val()};
	$.post("<c:out value='${contextPath}'/>/loginAction.do?method=getlogin",param,function(data){
		var datasta = eval(data)[0].status;
		if(datasta==1){
			$("#box").slideUp();
			$("#b22").slideDown();
			$("#b22").attr({"style":"visibility:visible"});
		}else if(datasta==2){
			 alert("<fmt:message key="user.userpassword"/>");
			 document.forms[0].logonname.focus();
			 return false;
		}
	});
}

function login(){
	document.form1.action="<c:out value='${contextPath}'/>/loginAction.do?method=login";
	document.form1.submit();
}

</script>