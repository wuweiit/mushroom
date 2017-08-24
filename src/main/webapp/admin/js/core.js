/*
 * MRCMS后台交互核心JS文件
 * 
 * author： marker
 * email：weiweiit@gmail.com
 * blog:  www.yl-blog.com
 * */

// 当前栏目标签
var CURRENT_CHANNEL_HTML = "";
// 配置项
var options ={
	modelTimeout:1000  // 模态加载超时时间
};
window.view_callback = null;// 内容界面，关闭毁掉函数



// 导航栏选择状态
$(function(){ 
	$('#admin_nav ul li').click(function(){
		$(".admin_content")[0].scrollTop = 0;// 滚动条归零
		
		var indexUrl = $(this).attr('index');
		if(indexUrl != null && indexUrl != ""){ 
			$('.admin_content').load(indexUrl);
		}
		
		// showBreadCrumbMenu("", $(this).text()+"管理" );
		
		
		var showtimeout = setTimeout(function(){
			zoom.showModelDialog();// 200 毫秒若未加载完毕，则显示加载提示
		}, options.modelTimeout);
		$('.admin_menu').load(uniqueUrl($(this).attr('url')),function(){
			if(showtimeout){clearTimeout(showtimeout);}
			zoom.closeModelDialog();
		});
		
	});
});


// 显示面包屑导航
function showBreadCrumbMenu(one, two){
	var lis = $(".bread_crumb_menu li"); 
	$(lis[2]).html(two);
	$(lis[4]).html(one);
	$(lis[3]).removeClass('hide');
}


//动态加载内容区域
function addContent(obj){
	$(".admin_content")[0].scrollTop = 0;// 滚动条归零
	
	// 视图切换回调，资源回收操作
	if(window.view_callback) window.view_callback(); 
	
	
	var nav_two = $(obj).parent().parent().parent().children().first().html();
	var nav_one = $(obj).html();
	
	showBreadCrumbMenu(nav_one, nav_two );


    var url = $(obj).attr('url');
    if(url.indexOf("http") == 0){
        window.open(url);
        return ;

    }


	var showtimeout = setTimeout(function(){
		zoom.showModelDialog();// 200 毫秒若未加载完毕，则显示加载提示
	}, options.modelTimeout);


    $('.admin_content').load(uniqueUrl(url),function(){
        if(showtimeout){clearTimeout(showtimeout);}
        zoom.closeModelDialog();
    });




}


//提交数据
function submitActionForm(obj){
	//采用ajaxForm作为表单提交插件满足文件上传功能
	zoom.showConfirmDialog("确定提交数据吗？","消息提示", function(){
		var options = {
			type : "POST",
			dataType: 'json',

			success: function(data) {
				zoom.closeConfirmDialog();//关闭确定提示
				if(data.status){
					zoom.showMessageDialog(data.message, "消息提示",1000);
					refreshContentByURL($(obj).attr("return"), $(obj).attr("name"));
				}else{
					if('101' == data.code){// 表示访问正常页面，但会话失效
						zoom.showMessageDialog(data.message + "<br/>","消息提示",1500,function(){
							window.location.href = "login.do";// 回调中刷新界面
						}); 
					}else{
						zoom.showMessageDialog(data.message + "<br/>","消息提示",1500); 
					}
				}
			},
			error: function(){
				zoom.showMessageDialog( "网络错误，请重新尝试<br/>","消息提示",1500); 
			}
		};
		$('#myForm').ajaxSubmit(options);
	});
}

//表单重置
function resetActionForm(){
	$("#myForm")[0].reset();//表单重置
}


/* **********************
* 提交一个get请求
* 例子:  <button onClick="deleteCheck(this);" id="staff_delete.action" id="stafffind.action">删除</button>
* id  -deleteAction地址
* callback  -删除成功回调页面地址
* ********************* */
function submitActionGet(obj){
	var action = $(obj).attr("action");
	var params = $(obj).attr("param");
	var name   = $(obj).attr("name");
	zoom.showConfirmDialog("确定安装 " + name + " 插件吗？","消息提示", function(){
		$.getJSON(action, params, function(data){
			if(data.status){
				zoom.showMessageDialog(data.message,"消息提示",1500);
				zoom.closeConfirmDialog();
			}else{
				zoom.showMessageDialog(data.message,"消息提示",1500);
				zoom.closeConfirmDialog();
			}
		});
	});
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
	zoom.showConfirmDialog("确定删除吗？","消息提示", function(){
		$.getJSON(action,null,function(data){
			if(data.status){
				refreshContentByURL(reurl);
				zoom.closeConfirmDialog();
			}else{
				zoom.showMessageDialog(data.message,"消息提示",1500);
				zoom.closeConfirmDialog();
			}
		});
	});
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
		zoom.showMessageDialog("您还没有选择要删除的信息哦!","消息提示", 1500);
	}else{
		zoom.showConfirmDialog("确定删除吗？","消息提示", function(){
			$.getJSON(action, list, function(data){
				if(data.status){
					refreshContentByURL(reurl);
					zoom.closeConfirmDialog();
				}else{
					zoom.showMessageDialog("批量删除失败!<br/>"+data.message,"消息提示", 1500);
					zoom.closeConfirmDialog();
				}
			});
		});
	}
}













/* *******************
* 刷新页面(内部JS调用方法)
* url -刷新地址
* title - 地址标题（面包屑导航）
* ******************* */
function refreshContentByURL(url){
//	if(title != null) $('.breadcrumbs li:eq(2)').text(title);//设置标题
	$('.admin_content').load(uniqueUrl(url));//只是刷新内容区域，不用改变面包屑导航
}

//页面里调用的刷新方法（针对翻页）
function refreshContentByElement(obj){
	var param = $(obj).attr("param");
	param = encodeURI(param);
	$('.admin_content').load(uniqueUrl($(obj).attr('action')), param);
}

 

/* *****************
* 唯一URL地址算法(内部JS调用方法)
* @param u_url -需要转换唯一地址的URL
* @return String 唯一URL地址
* ************* */
function uniqueUrl(u_url){
	if(u_url.indexOf("?")==-1){u_url+="?r=";}else{u_url+="&r=";}
	u_url += Math.random();
	return encodeURI(u_url);
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





















/*  以下是view组件 */

window.zoom = new Object();//zoom
var timeTemp;//临时变量

/* 显示消息Dialog */
zoom.closeMessageDialog = function(){
	var currentDialog = document.getElementById("showMessageDialog");
	if(currentDialog != null) window.document.body.removeChild(currentDialog);
	clearTimeout(timeTemp);//清除定时任务
};
zoom.showMessageDialog = function(content,title,time, callback){
	if(title ==null) title = "消息提示";
	var message_dialog_div = document.createElement("div");
	$(message_dialog_div).attr("id","showMessageDialog");
	$(message_dialog_div).addClass("dialog");
	var htmlText = "<div class=\"dialogFullScreen\"></div>"+
    	"<div class=\"dialogContent dialog_style_content\">"+
        	"<div class=\"dialogPanel\">"+
            	"<h1>"+title+"</h1>"+
               " <div class=\"showMessageDialogPanel\">"+
                	"<div class=\"dialogPanelContent\">"+
                    	content+
                   " </div>"+
               " </div>"+
               " <div class=\"dialogButton\">"+
                	"<button id=\"dialogButtonCancel\">取 消</button>"+
              "  </div>"+
          "  </div>"+
       " </div>";
	$(message_dialog_div).html(htmlText);
	window.document.body.appendChild(message_dialog_div);
	$("#dialogButtonCancel").bind("click",function(){
		zoom.closeMessageDialog();
	});
	timeTemp = window.setTimeout(function(){
		var currentDialog = document.getElementById("showMessageDialog");
		if(currentDialog != null) window.document.body.removeChild(currentDialog);
		if(callback != null) callback();// 判断回调函数是否存在，存在则调用。
	},time);
}; 


/* 显示确定框Dialog */
zoom.closeConfirmDialog = function(){
	var currentDialog = document.getElementById("showConfirmDialog");
	if(currentDialog != null) window.document.body.removeChild(currentDialog);
};
zoom.showConfirmDialog = function(content,title,fun){
	if(title ==null) title = "消息提示";
	var dialog_div = document.createElement("div");
	$(dialog_div).attr("id","showConfirmDialog");
	$(dialog_div).addClass("dialog");
	var htmlText = "<div class=\"dialogFullScreen\"></div>"+
    	"<div class=\"dialogContent dialog_style_content\">"+
        	"<div class=\"dialogPanel\">"+
            	"<h1>"+title+"</h1>"+
               " <div class=\"showConfirmDialogPanel\">"+
                	"<div class=\"dialogPanelContent\">"+
                    	content+
                   " </div>"+
               " </div>"+
               " <div class=\"dialogButton\">"+
               		"<button id=\"dialogButtonConfirm\">确 定</button> &nbsp; &nbsp; "+
                	"<button id=\"dialogButtonCancel\">取 消</button>"+
              "  </div>"+
          "  </div>"+
       " </div>";
	$(dialog_div).html(htmlText);
	window.document.body.appendChild(dialog_div);
	$("#dialogButtonConfirm").focus();//设置焦点
	$("#dialogButtonConfirm").bind("click",fun);
	$("#dialogButtonCancel").bind("click",function(){
		zoom.closeConfirmDialog();
	});
	
}; 


/* 输入框框 */
zoom.closeInputDialog = function(){
	var showInputDialog = document.getElementById("showInputDialog");
	if(showInputDialog != null) window.document.body.removeChild(showInputDialog);
};
zoom.showInputDialog = function(content,title,fun){
	if(title == null) title = "消息提示";
	var dialog_div = document.createElement("div");
	$(dialog_div).attr("id","showInputDialog");
	$(dialog_div).addClass("dialog");
	var htmlText = "<div id=\"showInputDialog\" class=\"dialog\">"+
		"<div class=\"dialogFullScreen\"></div>"+
		"<div class=\"dialogContent dialog_style_content\">"+
			"<div class=\"dialogPanel\">"+
 			   "<h1>" + title + "</h1>" +
               "<div class=\"showInputDialogPanel\">"+
              		"<div class=\"dialogPanelContent\">"+ content + "</div>"+
               " </div>"+
               " <div class=\"showInputDialogInput\">"+
                "	<input id=\"showInputDialogInput\" class=\"input_for_login\" />"+
               " </div>"+
                "<div class=\"dialogButton\">"+
                	"<button id=\"dialogButtonConfirm\">确 定</button> &nbsp; &nbsp; "+
                	"<button id=\"dialogButtonCancel\">取 消</button>"+
               " </div>"+
          	"</div>"+
 		"</div>"+
    " </div>";
	$(dialog_div).html(htmlText);
	window.document.body.appendChild(dialog_div);
	$("#showInputDialogInput").focus();// 设置焦点
	$("#showInputDialogInput").bind("keyup",function(event){
		if(event.keyCode == 13) fun();
	});
	$("#dialogButtonConfirm").bind("click",fun);
	$("#dialogButtonCancel").bind("click",function(){
		zoom.closeInputDialog();
	});
};

/* 
 * 模态窗口
 * */
zoom.closeModelDialog = function(){
	var showInputDialog = document.getElementById("showModelDialog");
	if(showInputDialog != null) window.document.body.removeChild(showInputDialog);
	
};

zoom.showModelDialog = function(){
	var dialog_div = document.createElement("div");
	$(dialog_div).attr("id","showModelDialog");

	$(dialog_div).addClass("loading_model");
	
	var htmlText = "<div class=\"clear_zone\"></div>" +
		 "<div class=\"loading_info\">" +
	     "<i class=\"fa fa-spinner loading_animation\"></i> 正在载入页面 ......" +
	 "</div>";
	$(dialog_div).html(htmlText);
	window.document.body.appendChild(dialog_div); 
};


/* 
 * iframe窗口
 * */

zoom.closeFrameDialog = function(){
	var showDialog = document.getElementById("showFrameDialog");
	if(showDialog != null) window.document.body.removeChild(showDialog);
};
zoom.showFrameDialog = function(title,width,height,content){
	var dialog_div = document.createElement("div");
	$(dialog_div).attr("id","showFrameDialog"); 
	$(dialog_div).addClass("frame_model");
	
	var htmlText = "<div class=\"clear_zone\"></div>" +
		 "<div class=\"frame_window\" style=\"width:"+width+"px;margin-left:-"+(width/2)+"px; height: "+height+"px; margin-top: -"+(height/2)+"px;"+   "\">" +
	     	"<div class=\"frame_bar\"><span class=\"title\">"+title+"</span><div class=\"frame_exit\"></div></div>"+
	     	"<div class=\"frame_content\" style=\"height:"+ (height-36)+"px;\"><div class=\"frame_margin markdown\">"
	     		+ content +
	     	"</div></div>" +
	 "</div>";
	$(dialog_div).html(htmlText);
	window.document.body.appendChild(dialog_div); 
	
	$(dialog_div).on("click",".frame_exit",function(){
		zoom.closeFrameDialog();
	});
};











