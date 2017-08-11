function checkAll(funname,val,rvalue){
	if(funname.indexOf("checkNull")>=0){
		return checkNull(val);
	}
	//if(funname.indexOf("checkLength")>=0){
	//	return checkLength(val,rvalue);
	//}
	//if(funname.indexOf("checkIn")>=0){
	//	return checkIn(val,rvalue);
	//}
//	if(funname.indexOf("checkDaYu")>=0){
//		return checkDaYu(val,rvalue);
//	}
//	if(funname.indexOf("checkDengYu")>=0){
//		return checkDengYu(val,rvalue);
//	}
//	if(funname.indexOf("checkBuDeng")>=0){
//		return checkBuDeng(val,rvalue);
//	}
//	if(funname.indexOf("checkXiaoYu")>=0){
//		return checkXiaoYu(val,rvalue);
//	}
	//if(funname.indexOf("checkXiaoDeng")>=0){
	//	return checkXiaoDeng(val,rvalue);
	//}
//	if(funname.indexOf("checkDaDeng")>=0){
//		return checkDaDeng(val,rvalue);
//	}
	return true;
}
//非空
function checkNull(val){
	if(""==val.replace(/[ ]/g,"")||null==val){
		alert("不能为空！");
		return false;
	}
	return true;
}
//编码格式
function checkLength(val,leng){
	if(val.length==leng){
		return true;
	}
	alert("长度必须为："+leng);
	return false;
}
//in
//function checkIn(val,rvalue){
//	if(rvalue.indexOf(","+val+",")>=0){
//		return true;
//	}
//	return false;
//}
//大于
function checkDaYu(val,rvalue){
	if(val>rvalue){
		return true;
	}
	return false;
}
//等于
function checkDengYu(val,rvalue){
	if(val==rvalue){
		return true;
	}
	return false;
}
//不等
function checkBuDeng(val,rvalue){
	if(val!=rvalue){
		return true;
	}
	return false;
}
//小于
function checkXiaoYu(val,rvalue){
	if(val<rvalue){
		return true;
	}
	return false;
}
//小等
function checkXiaoDeng(val,rvalue){
	if(val.length<=rvalue){
		return true;
	}
	alert("长度不能大于"+rvalue);
	return false;
}
//大等
function checkDaDeng(val,rvalue){
	if(val>=rvalue){
		return true;
	}
	return false;
}

function getrvalue(o,pcheck,val,rcontent,reportdate,tarname){
	var params={"rcontent": rcontent,"reportDate":reportdate,"val":val,"method":"getRvalue"};
	$.ajax({ 
		type: "POST", 
		async: false, //ajaxͬ��
		url: "dataFill.do", 
		data: jQuery.param(params), 
		success: function(result){
			//var bflag=checkAll(pcheck,val,result);
			if(result<1){
				$("#"+strs+"_datatype").val(1);
				alert("【"+tarname+"】的值不存在！");
			}else{
				var strs=$(o).attr("id");
				$("#"+strs+"_datatype").val(0);
				$(o).css("background", "#fff"); 
			}
		}
	});
}

