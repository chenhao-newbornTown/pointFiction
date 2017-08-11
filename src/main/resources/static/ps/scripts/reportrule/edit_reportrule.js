$(function(){
//	$("li").each(function(){
//		$(this).unbind("click");
//		$(this).bind("click",function(){
//			$("li").children("a").attr("class","");
//			$(this).children("a").attr("class","current");
//		});
//	});
	
	if($("#range2").attr("checked")==true){
		$("#fanwei0").hide();
		$("#fanwei1").show();
	}else{
		$("#fanwei1").hide();
	}
	if($("#null5").attr("checked")==true){
		$("#qita").show();
	}else{
		$("#qita").hide();
	}
	$("#quanxuan").click(function(){
		$("#formwrapper").find("[name='rulecode']").attr("checked","checked");
		$("#fieldset1").show();
		$("#fieldset2").show();
		$("#fieldset3").show();
		$("#fieldset4").show();
	});
	$("#quxiao").click(function(){
		$("#formwrapper").find("[name='rulecode']").removeAttr("checked");
		$("#fieldset1").hide();
		$("#fieldset2").hide();
		$("#fieldset3").hide();
		$("#fieldset4").hide();
	});
	$("#rulecode0").click(function(){
		if($("#rulecode0").attr("checked")==true){
			$("#fieldset1").show();
		}else{
			$("#fieldset1").hide();
		}
	});
	$("#rulecode1").click(function(){
		if($("#rulecode1").attr("checked")==true){
			$("#fieldset2").show();
		}else{
			$("#fieldset2").hide();
		}
	});
	$("#rulecode2").click(function(){
		if($("#rulecode2").attr("checked")==true){
			$("#fieldset3").show();
		}else{
			$("#fieldset3").hide();
		}
	});
	$("#rulecode3").click(function(){
		if($("#rulecode3").attr("checked")==true){
			$("#fieldset4").show();
		}else{
			$("#fieldset4").hide();
		}
	});
});

function changetarget(){
	var repid=$("#reportId").value;
	var repname=$("#reportName").value;
	var url = g_url.itemTreeURL+"&reportId="+repid+"&reportName="+repname;
	document.all.objTree2.SetXMLPath(url);
   	document.all.objTree2.Refresh();	
}
function changeTree2(){
	changetarget();
}
function setContentVal(val){
	var aa=$("#content").val();
		aa=aa+"  "+$("#"+val+"").val()+" ";
	$("#content").val(aa);
}
