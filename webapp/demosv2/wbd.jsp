<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:include page="/include/header2.jsp" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>canvas画图</title>
<script>
	$(function() {
		window.location.assign("wbd.html?roomId=room1&userId=admin");
		/* var slider = $("#slider");
		slider.slider();
		slider.slider({
			max : 20
		});
		slider.slider({
			value : 0
		});
		//slider.slider({ step: 1000 });
		slider.slider({
			slide : function(event, ui) {
				console.log(ui.value);
				globalBar.interrupt();
				globalBar = new bar();
				globalBar.create(ui.value)();
			}
		});
		
		var globalBar = null;
		
		var bar = function(){
			var interrupt = false;
			return {
				"create":function(val){
					return function(){
						if(interrupt){
							return;
						}
						moveSlider(val);
						if(val > slider.slider( "option", "max" )){
							return;
						}
						console.log(val);
						var s2 = setTimeout(globalBar.create(++val),1000);
						console.log(s2,"s2");
					}
				},
				"interrupt":function(){
					interrupt = true;
				},
				"resume":function(){
					interrupt = false;
				}
			};
		} 
		
		var moveSlider = function(val){
			slider.slider({value:val});
		}
		
		globalBar = new bar();
		
		globalBar.create(0)();
		//var s1 = setTimeout(globalBar.create(0),0);
		//console.log(s1,"s1"); */
	});
	
</script>
<style>
#slider {
	width: 500px;
	position: absolute;
	top: 200px;
	left: 500px;
}
</style>
</head>
<body>
	<div id="slider"></div>
</body>
</html>