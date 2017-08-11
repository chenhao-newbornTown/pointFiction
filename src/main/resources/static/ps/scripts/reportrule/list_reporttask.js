var flag=0;
function f_s(id,id2,val){
	   var maskWidth = $(window).width();//窗口的宽度
	   var thistop=$("#"+id2+"").offset().top;//获取当前位置的top
	   var dialogLeft;
	   var dialogTop
	if(val==''){
		  var arr = document.getElementsByName('yy');
		   for(i=0;i<arr.length;i++){
		     if( arr[i].checked == true){
		    	 val+=arr[i].value+",";
		     }
		   }
		   dialogLeft= (maskWidth/3) - $("#"+id+"").width();
		   dialogTop=  3*thistop/4 - ($("#"+id+"").height()/2);
		   $('#pl').val(1);  //批量删除的标志
	}else{
		dialogLeft= (3*maskWidth/4) - $("#"+id+"").width();
		dialogTop=  thistop - ($("#"+id+"").height()/2);
	}
		$('#rep_pkid').val(val);
       var obj=document.getElementById(id);
       obj.style.display="block";
       obj.style.height="1px"; 
	         
       var changeW=function(){ 	 		
              var obj_h=parseInt(obj.style.height);
              if(obj_h<=210){ 
                     obj.style.height=(obj_h+Math.ceil((210-obj_h)/10))+"px";
              }
              else{ 
              clearInterval(bw1);
              }
       }       
       bw1= setInterval(changeW,10);
	   if(flag>0){
	   	 clearInterval(bw2);
	   }
	   
       //var maskHeight = $(document).height();//文档的总高度
       var thiswidth=$("#"+id2+"").offset().width;//当前位置的宽度
       $("#"+id+"").css({top:dialogTop,left:dialogLeft}).show();
}
function closeW(id){
		flag++;
       var obj=document.getElementById(id);
       var closeDiv=function(){
	   		 
	   		  clearInterval(bw1);
              var obj_h=parseInt(obj.style.height);
              if(obj_h>1){ 
                     obj.style.height=(obj_h-Math.ceil(obj_h)/10)+"px";
					
              }
              else{
              clearInterval(bw2);
              obj.style.display="none";
              }
       }         
      bw2= setInterval(closeDiv,1);
//alert(flag)
}
function showDiv(){ 
	var ele = document.getElementById("div1");
	clearInterval(bw1);
	clearInterval(bw2);
	ele.style.display = "block";
	ele.style.height = 335 + "px";
	
	
}