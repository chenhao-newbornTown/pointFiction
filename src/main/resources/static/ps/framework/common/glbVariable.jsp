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
<c:set var = "activeXTreeHeight">
	300
</c:set>
<script type="text/javascript">

	/**
	 * 行情
	 */
	function getHQ(temp){
		var url="<%=request.getContextPath()%>/sccContro/getStrategyCcbyTemp.do";
		var param = {"temp":"HQ_STRATEGY"};
		$.post(url,param,function(data){
	      	var asdList = eval(data);
					var objs = document.all("strategyCodeMarket");
					for ( var i = objs.options.length - 1; i >= 0; i--) {
						objs.options.remove(i); //ä»åå¾åå é¤
					}
					var oOption0 = document.createElement("OPTION");
					objs.options.add(oOption0);
					oOption0.innerText =""; 
					oOption0.value = "";
					for ( var i = 0; i < asdList.length; i++) {
						var oOption = document.createElement("OPTION"); //åå»ºä¸ä¸ªOPTIONèç¹
						objs.options.add(oOption); //
						oOption.innerText =asdList[i].tempInfoName; //è®¾ç½®éæ©å±ç¤ºçä¿¡æ¯
						oOption.value =asdList[i].tempInfo; //è®¾ç½®éé¡¹çå¼
						if(asdList[i].tempInfo==temp){
							oOption.selected=true;
						}
					}
		})
	}
	/**
	 * 周期
	 */
	function getCycle(temp){
		var url="<%=request.getContextPath()%>/sccContro/getStrategyCcbyTemp.do";
		var param = {"temp":"CYCLE_STRATEGY"};
		$.post(url,param,function(data){
	      	var asdList = eval(data);
					var objs = document.all("strategyCycle");
					for ( var i = objs.options.length - 1; i >= 0; i--) {
						objs.options.remove(i); //ä»åå¾åå é¤
					}
					var oOption0 = document.createElement("OPTION");
					objs.options.add(oOption0);
					oOption0.innerText =""; 
					oOption0.value = "";
					for ( var i = 0; i < asdList.length; i++) {
						var oOption = document.createElement("OPTION"); //åå»ºä¸ä¸ªOPTIONèç¹
						objs.options.add(oOption); //
						oOption.innerText =asdList[i].tempInfoName; //è®¾ç½®éæ©å±ç¤ºçä¿¡æ¯
						oOption.value =asdList[i].tempInfo; //è®¾ç½®éé¡¹çå¼
						if(asdList[i].tempInfo==temp){
							oOption.selected=true;
						}
					}
		})
	}
	
	/*
	 * 获取ajax one
	 */
	function getAjaxOne(strategyCodeMarket){
		var onlineOfflineStatus = document.getElementsByName("onlineOfflineStatus")[0].value;
	    $.ajax({
			    type: "POST",
				url: "<%=request.getContextPath()%>/strategyInfo/strategyInfoList.do" ,
				data:{"strategyCodeMarket":strategyCodeMarket,"onlineOfflineStatus":onlineOfflineStatus},
				success: function(data){
	                var asdList = eval(data);
				var objs = document.all("strategyName");
				for ( var i = objs.options.length - 1; i >= 0; i--) {
					objs.options.remove(i); //从后往前删除
				}
				var oOption0 = document.createElement("OPTION"); //创建一个OPTION节点
				objs.options.add(oOption0);
				oOption0.innerText =""; 
				oOption0.value = "";
				
				for ( var i = 0; i < asdList.length; i++) {
					var oOption = document.createElement("OPTION"); //创建一个OPTION节点
					objs.options.add(oOption); //
					oOption.innerText =asdList[i].strategyName+"_("+asdList[i].strategyId+")"; //设置选择展示的信息
					oOption.value =asdList[i].strategyName; //设置选项的值
				}
	
			}
		}); 
	}
	
	/*
	 *获取ajax two
	 */
	function getAjaxSelectedTwo(strategyCodeMarket,strategyName){
		var onlineOfflineStatus = document.getElementsByName("onlineOfflineStatus")[0].value;
		$.ajax({
		    type: "POST",
			url: "<%=request.getContextPath()%>/strategyInfo/strategyInfoList.do" ,
			data:{"strategyCodeMarket":strategyCodeMarket,"onlineOfflineStatus":onlineOfflineStatus},
			success: function(data){
               	var asdList = eval(data);
				var objs = document.all("strategyName");
				for ( var i = objs.options.length - 1; i >= 0; i--) {
					objs.options.remove(i); //从后往前删除
				}
				var oOption0 = document.createElement("OPTION"); //创建一个OPTION节点
				objs.options.add(oOption0);
				oOption0.innerText =""; 
				oOption0.value = "";
				for ( var i = 0; i < asdList.length; i++) {
					var oOption = document.createElement("OPTION"); //创建一个OPTION节点
					objs.options.add(oOption); //
					oOption.innerText =asdList[i].strategyName+"_("+asdList[i].strategyId+")"; //设置选择展示的信息
					oOption.value =asdList[i].strategyName; //设置选项的值
					if(asdList[i].strategyName==strategyName){
						oOption.selected=true;
					}	
				}
			}
		});
	}
	

	
	 
</script>
<%--<script type="text/javascript" src="<c:url value='/ps/scripts/log4javascript.js'/>"></script>--%>