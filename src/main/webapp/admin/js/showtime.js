// 动态显示时间用的
function setIndexTime(){	
	var time_d      = new Date();
	var time_year   = time_d.getFullYear();
	var time_month  = time_d.getMonth() + 1;
	var time_day    = time_d.getDate();
	var time_hour   = time_d.getHours(); 
	var time_minute = time_d.getMinutes(); 
	var time_second = time_d.getSeconds(); 
	$("#index_time").html("当前时间: "+
		time_year+"年"+
		(time_month<10?"0"+time_month:time_month)+"月"+
		(time_day<10?"0"+time_day:time_day)+"日 "+
		(time_hour<10?"0"+time_hour:time_hour)+":"+
		(time_minute<10?"0"+time_minute :time_minute)+":"+
		(time_second<10?"0" +time_second : time_second)
	);
	setTimeout("setIndexTime()",1000);
}
$(function(){setIndexTime();});