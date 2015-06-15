$(function(){
	$("#delKey").bind("click",function(){
		$("#searchKey").val("");
	});
	
	$(".gov-right .sort-list").find("li").has("a").bind("click",function(){
		$(this).addClass("cur");
		$(".gov-right .sort-list").find("li").not(this).removeClass("cur");
		
		//点击事件传递参数
		var t = $(this).find("a").attr("type");
		$("input[name=t]").val(t);
		
		if("timecs"== t){
			$("#specialDate").show();
		}else{
			$("#specialDate").hide();
			$("input[name=mintime]").val("");
			$("input[name=maxtime]").val("");
			$("#qform").submit();
		}
	});
	
	$(".gov-right .sort-list").find("li").removeClass("cur");
	$(".gov-right .sort-list").find("li").find("a[type='"+timeKey+"']").parent().addClass("cur");
	
	if(timeKey == "timecs"){
		$("#specialDate").show();
		$("#fromDate").val($("input[name=mintime]").val());
		$("#toDate").val($("input[name=maxtime]").val());
	}
	
	
	
	var num = 0;
	var keyShow = function(searchKey){
		return function(){
			if(!!searchKey&&$("li.res-list:contains("+searchKey+")").size()>0){
				var reg = new RegExp(searchKey,"g");
				$("li.res-list:contains("+searchKey+")").each(function(){
					$(this).html($(this).html().replace(reg,"<em>"+searchKey+"</em>"));
				});
				//$("li.res-list:contains("+searchKey+")").html($("li.res-list:contains("+searchKey+")").html().replace(reg,"<em>"+searchKey+"</em>"));
			}
			num++;
			if(num>15){
				return;
			}
			setTimeout(keyShow(searchKey),100);
		}
	}
	
	setTimeout(keyShow(searchKey),100);
	
	//时间控件监控
	
	$("#fromDate").bind("change",function(){
		$("input[name=mintime]").val($("#fromDate").val());
	});
	$("#toDate").bind("change",function(){
		$("input[name=maxtime]").val($("#toDate").val());
	});
	
	$("#rjs").bind("click",function(){
		var fromDate = $("#fromDate").val();
		var toDate = $("#toDate").val();
		if(!fromDate||!toDate){
			alert("时间格式错误或者未填写!");
			return;
		}
		fromDate = fromDate.split("-");
		toDate = toDate.split("-");
		var d1 = new Date(fromDate[0],fromDate[1],fromDate[2]);
		var d2 = new Date(toDate[0],toDate[1],toDate[2]);
		if(d1 > d2){
			alert("起始时间不能大于结束时间!");
			return;
		}
		$("#qform").submit();
	});
	
});