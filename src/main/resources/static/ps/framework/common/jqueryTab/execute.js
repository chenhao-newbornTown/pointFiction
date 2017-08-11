
	// Begin jQuery
	
	$(document).ready(function() {
	
		// Append shadow image to each LI
		
		//$("#nav-shadow li").append('<img class="shadow" src="ps/framework/common/jqueryTab/icons-shadow.jpg" width="81" height="27" alt="" />');
	
		// Animate buttons, shrink and fade shadow
		
		$("#nav-shadow li").hover(function() {
			var e = this;
		    $(e).find("a").stop().animate({ marginTop: "-14px" }, 250, function() {
		    	$(e).find("a").animate({ marginTop: "-10px" }, 250);
		    });
		    $(e).find("img.shadow").stop().animate({ width: "80%", height: "20px", marginLeft: "8px", opacity: 0.25 }, 250);
		},function(){
			var e = this;
		    $(e).find("a").stop().animate({ marginTop: "4px" }, 250, function() {
		    	$(e).find("a").animate({ marginTop: "0px" }, 250);
		    });
		    $(e).find("img.shadow").stop().animate({ width: "100%", height: "27px", marginLeft: "0", opacity: 1 }, 250);
		});
						
	
	});
	
	
