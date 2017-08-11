$(document).ready(function() {
	/* $(":range").rangeinput({progress: true}); */
	/* Slide Toogle */

	$("div.jmenuheader").each(function() {
		var arrow = $(this).find("span.arrow");
		arrow.removeClass("up");
		arrow.addClass("down");
		$(this).parent().find("ul.jmenumenu").slideUp();
	});

	$("ul.expmenu li > div.jmenuheader").click(function() {
		
		var arrow = $(this).find("span.arrow");
		
		if (arrow.hasClass("up")) {
			/*点击其他菜单，收起其他已经打开的菜单--start*/
			$("div.jmenuheader").each(function() {
				var arroww = $(this).find("span.arrow");
				arroww.removeClass("down");
				arroww.addClass("up");
				$(this).parent().find("ul.jmenumenu").slideUp();
			});
			/*点击其他菜单，收起其他已经打开的菜单--end*/
			arrow.removeClass("up");
			arrow.addClass("down");
			
		} else if (arrow.hasClass("down")) {
			arrow.removeClass("down");
			arrow.addClass("up");
		}

		
		$(this).parent().find("ul.jmenumenu").slideToggle();
		

	});


	$("ul.expmenu li > ul.jmenumenu > li.submenu").click(function() {
		$("li.submenu").each(function() {
			$(this).removeAttr("style");
		});
		$(this).css("background", "#FFF url(ps/uitl/jmenu/images/finger_16.png) no-repeat 10px");
		$(this).css("color", "#000000");
	});
	

});