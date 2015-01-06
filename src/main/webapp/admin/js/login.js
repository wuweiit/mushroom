/*
 * 登录控制代码
 * 
 * @author marker
 * 
 */



// 设置用户名得到焦点
$("#username").focus();


/* 按下回车继续登录下一步 */
function onkeyupforcontinueLogin(event){
 	var keycode = event.keyCode;  
 	if(keycode == 13)//回车
 		continueLogin();
}


/* 显示验证码 */
function continueLogin(){
	var username = $("#username").val();
	var password = $("#password").val();
	//验证输入并提示错误信息
	if(username == ""){
		zoom.showMessageDialog("用户名不能为空哦!","消息提示",1500);
		$("#username").focus();
		return;
	}
	if(password == ""){
		zoom.showMessageDialog("密码不能为空哦!","消息提示",1500,function(){
			$("#password").focus();
		});
		$("#password").blur();
		return;
	}
	zoom.showInputDialog("<img id=\"code\" onClick=\"$('#code').attr('src','../SecurityCode?r='+Math.random())\" src=\"../SecurityCode?r="+Math.random()+"\" style=\"height:40px;\" />","验证码", function(){
		var randcode = $("#showInputDialogInput").val();
		submitLogin(randcode);
	});
}

/* 立即登录 */
function submitLogin(randcode){
	var username = $("#username").val();
	var password = $("#password").val();
	var device   = getSysInfo();
	var action  = "loginSystem.do";
	var params  = "username=" + username + "&password=" + password + "&randcode=" + randcode +
		"&device=" + device + "&r=" + Math.random();
 	
	$.ajax({
		url: action,
		type:"post",
		dataType:"json",
		data: params,
		success: function(json){
			if(json.status == true){
				location.href = "index.do";
			}else{
				zoom.closeInputDialog();
				zoom.showMessageDialog(json.message,"消息提示",2500,function(){
					$("#password").focus();
				});
			}
		},
		error:function(){
			zoom.showMessageDialog("网络错误，请重新尝试!","消息提示",2500,function(){
				$("#password").focus();
			});
		}
	});
}


//获取系统信息
function getSysInfo() {

    var ua = navigator.userAgent.toLowerCase();
    isWin7 = ua.indexOf("nt 6.1") > -1;
    isVista = ua.indexOf("nt 6.0") > -1;
    isWin2003 = ua.indexOf("nt 5.2") > -1;
    isWinXp = ua.indexOf("nt 5.1") > -1;
    isWin2000 = ua.indexOf("nt 5.0") > -1;
    isWindows = (ua.indexOf("windows") != -1 || ua.indexOf("win32") != -1);
    isMac = (ua.indexOf("macintosh") != -1 || ua.indexOf("mac os x") != -1);
    isAir = (ua.indexOf("adobeair") != -1);
    isLinux = (ua.indexOf("linux") != -1);
 
    if (isWin7) {
        sys = "Windows 7";
    } else if (isVista) {
        sys = "Vista";
    } else if (isWinXp) {
        sys = "Windows xp";
    } else if (isWin2003) {
        sys = "Windows 2003";
    } else if (isWin2000) {
        sys = "Windows 2000";
    } else if (isWindows) {
        sys = "Windows";
    } else if (isMac) {
        sys = "Macintosh";
    } else if (isAir) {
        sys = "Adobeair";
    } else if (isLinux) {
        sys = "Linux";
    } else {
        sys = "Unknow";
    }
    return sys;
}