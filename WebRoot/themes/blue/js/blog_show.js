// JavaScript Document
//轮播状态
var isNext = true;
function show(num){
	if(isNext){
		$(".button").css("border","none");//复位 
		var ps = $("#blog_show_inner");
		var v = -(num * $(".blog_card").height());
		ps.animate({top:v+"px"});
	}
}
var i = 0;
var showNow = setInterval(function(){
  	show(i++);
	if(i == 3) i=0;
},5000);

//注册事件控制停止和启动
$(".blog_card").mouseleave(function(){isNext = true;});
$(".blog_card").mouseover(function(){isNext = false;});