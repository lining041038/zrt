$(function(){
	/*Nav*/
	$(".topNav").hover(function(){
		//$(".topNav .menuCon").addClass("borSecond");
	},function(){
		$(".topNav .menuCon").removeClass("borSecond");
		$(".topNav .menuList ul li").removeClass("selected");
		$(".topNav .menuCon .subcon").hide();
		$(".nav-tabg").hide()
	})
	$("#IndexPage .topNav .menuList ul li").find("a").hover(function(){
		index = $("#IndexPage .topNav .menuList ul li").find("a").index(this);
        curNav=$(this).parent().parent().addClass("selected").siblings().removeClass("selected");
        
        $(".topNav .menuCon .subcon").eq(index).show().siblings().hide();   
        $(".topNav .menuCon").addClass("borSecond");        
        //
        $(".nav-tabg").show()
        var pos=125*index;
        $(".nav-tabg").stop().animate({"left":pos},500)     
        setTimeout("curNav",2000)
    })
	//数据
	$(".menus07 .reportList tr:even").attr({"bgcolor":"#e5e5e5","height":"32"});
	$(".menus07 .reportList tr:odd").attr({"bgcolor":"#ffffff","height":"36"});
	//右侧浮窗
	function rigScroll(){
        var winWidth=$(window).width();
        if(winWidth<1024){
            $(".gov_weixin_share").css({"right":10})
        }else{      
            var winRig=(winWidth-1005)/2-90;
            $(".gov_weixin_share").css({"right":winRig})
        }
		$(".closeBtn").click(function(){
			$(".gov_weixin_share").hide()
		})
		
    }
	rigScroll()
	$(window).scroll(function() {
		rigScroll()
	});
 
	$(window).resize(function() {
		rigScroll()
	});	
	//网友留言
	$(".lyBox li").each(function(i){
		 var txtLy=$(this).text().length
		 if(txtLy>95){
			 	var text=$(this).text().substring(0,95)+"......";  
				//重新赋值;  
				$(this).text(text);  
		} 
	})
	$(".wmly li").each(function(i){
         var txtLy=$(this).text().length
         if(txtLy>110){
                var text=$(this).text().substring(0,110)+"......";  
                //重新赋值;  
                $(this).text(text);  
        } 
    })
})