$(function(){
	YLCMS = new Object();
	
	//初始化提示窗口
	$("#ylcms_dialog").easydrag();//给指定的标签绑定拖动效果，也可以是Class
	$("#ylcms_dialog").setHandler('dialog_bar');//指定触发拖动的元素，handler是该元素的id，后面我们需要修改它
	$(".dialog_exit").click(function(){ $("#ylcms_dialog").hide(); });
	
	//自定义提示框
	YLCMS.dialog = function(obj,title, width, height){
		$("#ylcms_dialog").show(); 
		$("#ylcms_dialog").css("width",width);
		$("#ylcms_dialog").css("height",height);
		$(".dialog_title_text").text(title);
		$(".dialog_content").load(uniqueUrl($(obj).attr('action')));
	};
	
	
	//文件列表视图
	YLCMS.FileViewItem = function(actionurl,data){
		this.name = data.name;
		this.icon = data.icon;
		this.type = data.type;
		this.size = data.size;
		this.path = data.path;
		this.parser = function(){
			var temp ="<div class=\"yl_file_item\" type=\""+this.type+"\" action=\""+actionurl+this.path+"\" ondblclick=\"gotoPath(this);\">";
 			temp += 	"<div class=\"yl_file_item_border\">";
 			temp += 		"<div class=\"yl_file_item_icon\"><img style=\"width:23px; height:23px;\" src=\"data:image/bmp;base64,"+this.icon+"\"/></div>";
 			temp += 		"<div class=\"yl_file_item_name\">"+this.name+"</div>";
 			temp += 		"<div class=\"yl_file_item_time\">2012/10/9 19:51</div>";
 			temp += 		"<div class=\"yl_file_item_type\">"+this.type+"</div>";
 			temp += 		"<div class=\"yl_file_item_size\">"+this.size+"</div>";
 			temp += 		"<div class=\"cb\"></div>";
 			temp += 	"</div>";
 			temp += "</div>";
			return temp;
		};
	}; 
	
});


//导航栏选择状态
$(function(){
	$('#nav ul li').click(function(){
		$(this).addClass('selected').siblings().removeClass('selected');
	});
});

//加载二级菜单事件注册
$(function(){ 
	$('#nav a').click(function(){
		$('#nav2').load(uniqueUrl($(this).attr('id')));
		$('#content_inner').html('');//清空显示区
	 
		$('.breadcrumbs').show();
		$('.breadcrumbs li:eq(0)').text($(this).text());
		$('.breadcrumbs li:eq(1)').text('');
		$('.breadcrumbs li:eq(2)').text('');
	});
});

//展开二级菜单
function addMenuSub(obj){
	$($(obj).children().get(0)).toggleClass('icon');  
	$(obj).nextAll().toggle();
	$('.breadcrumbs li:eq(1)').text($(obj).text());
	$('.breadcrumbs li:eq(2)').text('');
}

//动态加载内容区域
function addContent(obj){
	$('#content_inner').load(uniqueUrl($(obj).attr('id')));
	$('.east').css({background:'#fff',top:'125px'});
	$('.breadcrumbs').show();
	$('.breadcrumbs li:eq(2)').text($(obj).text());
	$('.breadcrumbs li:eq(1)').text($(obj).parent().parent().children("dt").text());
}


//提交数据
function submitActionForm(obj){
	//采用ajaxForm作为表单提交插件满足文件上传功能
	if(confirm("确定提交数据吗？")){
		var options = { 
			dataType: 'json',
			success: function(data) {
				if(data['status'] == 'true'){
					alert("提交成功!");
					refreshContentByURL($(obj).attr("return"),$(obj).attr("name"));
					$("#ylcms_dialog").hide();//隐藏对话框
				}else{
					alert("提交失败!\nerror:"+data['errcode']);
				}
			}
		};
		$('#myForm').ajaxSubmit(options);
	}
}

//表单重置
function resetActionForm(){
	$("#myForm")[0].reset();//表单重置
}



/* **********************
 * 删除一条或者多条数据(通用方法)
 * 例子:  <button onClick="deleteCheck(this);" id="staff_delete.action" id="stafffind.action">删除</button>
 * id  -deleteAction地址
 * callback  -删除成功回调页面地址
 * ********************* */
function deleteById(obj){
	var action = $(obj).attr("action");
	var reurl  = $(obj).attr("return");
	if(confirm("确定删除吗?")){
		$.getJSON(action,null,function(json){
			if(json['status']){
				refreshContentByURL(reurl);
			}else{
				alert("删除失败");
			}
		});
	}
}

function editById(obj){
	var action = $(obj).attr("action");
	var reurl  = $(obj).attr("return");
	if(confirm("确定删除吗?")){
		$.getJSON(action,null,function(json){
			if(json['status']){
				refreshContentByURL(reurl);
			}else{
				alert("删除失败");
			}
		});
	}
}


/* **********************
 * 删除一条或者多条数据(通用方法)
 * 例子:  <button onClick="deleteCheck(this);" id="staff_delete.action" id="stafffind.action">删除</button>
 * id  -deleteAction地址
 * callback  -删除成功回调页面地址
 * ********************* */
function deleteCheck(obj){
	var action = $(obj).attr("action");
	var reurl  = $(obj).attr("return");
	var list = "rid=";
	var temp;//存放临时id
	$(":checkbox").each(function(){
		if($(this).attr("checked")){//被选择
			temp = $(this).attr("value");
			list += temp + ","; 
		}
	});
	list = list.substr(0,list.length-1);//去掉最后的逗号
	if(list == "rid"){//判断有没有选择
		alert("您还没有选择要删除的信息哦!");
	}else{
		if(confirm("确定删除吗?")){
			$.getJSON(action, list, function(json){
				if(json['status']){
					refreshContentByURL(reurl);
				}else{
					alert("批量删除失败");
				}
			});
		}
	}
}













/* *******************
 * 刷新页面(内部JS调用方法)
 * url -刷新地址
 * title - 地址标题（面包屑导航）
 * ******************* */
function refreshContentByURL(url,title){
//	if(title != null) $('.breadcrumbs li:eq(2)').text(title);//设置标题
	$('#content_inner').load(uniqueUrl(url));//只是刷新内容区域，不用改变面包屑导航
}

//页面里调用的刷新方法（针对翻页）
function refreshContentByElement(obj){
	$('#content_inner').load(uniqueUrl($(obj).attr('action')));
}


/* *****************
 * 唯一URL地址算法(内部JS调用方法)
 * @param u_url -需要转换唯一地址的URL
 * @return String 唯一URL地址
 * ************* */
function uniqueUrl(u_url){
	if(u_url.indexOf("?")==-1){u_url+="?r=";}else{u_url+="&r=";}
	u_url += Math.random();
	return u_url;
}

/*
刷新表格
参数  obj为tr对象
参数  over为boolean值
*/
function refreshTableCSS(obj,over){
	var current = $(obj);
	if(over == true){//不在当前这里
		tempTableTrCSS = current.css("background-color");
		current.css("background-color","#BFDFFF");
	}else{//在这里
		current.css("background-color",tempTableTrCSS);
	}
}

/* *********************
 * JSON转换JS对象(内部JS调用方法)
 * @param json字符串
 * @return Object JS对象
 * ******************** */
function transformObject(json){
	return eval("(" + json + ")");
}


//全选
function allCheck(){
	$(":checkbox").attr("checked",'true');//全选  
}
//反选
function allRecheck(){
	$(":checkbox").each(function(){if($(this).attr("checked")){$(this).removeAttr("checked");}else{$(this).attr("checked","true");}});
}