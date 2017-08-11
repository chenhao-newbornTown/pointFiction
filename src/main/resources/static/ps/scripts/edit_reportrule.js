$(function(){
//	$("li").each(function(){
//		$(this).unbind("click");
//		$(this).bind("click",function(){
//			$("li").children("a").attr("class","");
//			$(this).children("a").attr("class","current");
//		});
//	});
	if($("#range2").attr("checked")==true){
		$("#fanwei1").show();
		$("#fanwei0").hide();
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
	alert(url);
	document.all.objTree2.SetXMLPath(url);
   	document.all.objTree2.Refresh();	
}
function changeTree2(){
	changetarget();
}
function setConVal(val){
	var aa=$("#rulenullcontent").value;
	alert("aa"+aa);
	if('jia'==val){
		alert(489574839);
		aa=aa+"+";
	}else if('jian'==val){
		aa=aa+"-";
	}else if('cheng'==val){
		aa=aa+"*";
	}else if('chu'==val){
		aa=aa+"/";
	}else if('deng'==val){
		aa=aa+"==";
	}else if('dayu'==val){
		aa=aa+">";
	}else if('dayudeng'==val){
		aa=aa+">=";
	}else if('xiaoyu'==val){
		aa=aa+"<";
	}else if('xiaoyudeng'==val){
		aa=aa+"<=";
	}else if('budeng'==val){
		aa=aa+"!=";
	}else if('and'==val){
		aa=aa+"and";
	}else if('or'==val){
		aa=aa+"or";
	}else if('if'==val){
		aa=aa+"if";
	}else if('else'==val){
		aa=aa+"else";
	}else if('zuo'==val){
		aa=aa+"(";
	}else if('you'==val){
		aa=aa+")";
	}
	$("#rulenullcontent").val(aa);
}