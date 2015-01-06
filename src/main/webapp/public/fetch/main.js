


window.onbeforeunload  =  function(){ 
    // 获取当前网站的更目录，这比较重要，全局通用的保证
    var path = getRootPath();
    // 后台处理的文件地址，注意，必须把这个文件的前台页面大部分清空，只留下第一行
    var url = path + "/fetch";
	
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest;
    }
    else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHttp");
    }
    if (req) { 
        req.open("GET", url + "?leave=1", true);
        req.onreadystatechange = null; // 制定回调函数
        req.send(null);  
    }
     
};
        



// 检查是否安装flash
function checkFlash(){
	var isIE = !-[1,];
	if(isIE){
	    try{
	        new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
	        return 1;
	    } catch(e){
	        return 0;
	    }
	} else {
	    try{
	        var swf2 = navigator.plugins['Shockwave Flash'];
	        if(swf2 == undefined){
	            return 0;
	        }  else {
	        	return 1;
	        }
	    }catch(e){
	    	 return 0;
	    }
	}
}


var req;
var visitID;
creatReq();

function creatReq() {

    // 获取当前网站的更目录，这比较重要，全局通用的保证
    var path = getRootPath();
    // 后台处理的文件地址，注意，必须把这个文件的前台页面大部分清空，只留下第一行
    var url = path + "/fetch";

    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest;
    }
    else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHttp");
    }
    if (req) {
        // 获取当前的网址
        var link = window.location.href.replace(path, "");
        // 获取上页地址
        var oldlink = document.referrer.replace(path, "");
        // 屏幕分辨率
        var screen = window.screen.width + "*" + window.screen.height;    
        // 异步请求发送
        
        req.open("GET", url + "?v0=" + escape(link) + "&v1=" + escape(oldlink) + "&v2=" + getSysInfo() + "&v3=" + screen + "&v4=" + GetBrowserType() + "&v5=" + GetBrowserVersion()+"&v6="+getLang() +"&v7="+checkFlash()+ "&k=" + GetKeyword(oldlink), true);
        req.onreadystatechange = callback; // 制定回调函数
        req.send(null); 
    }
}

// 获取当前网站的更目录，这比较重要，全局通用的保证
function getRootPath() {
    return window.location.protocol + "//" + window.location.host;
}


// 获取来自搜索引擎的关键词
function GetKeyword(url) {
    if (url.toString().indexOf("baidu") > 0) {
        return request(url, "wd");
    }
    else if (url.toString().indexOf("google") > 0) {
        return request(url, "q");
    }
    else if (url.toString().indexOf("sogou") > 0) {
        return request(url, "query");
    }
    else if (url.toString().indexOf("soso") > 0) {
        return request(url, "w");
    }
    else {
        return "";
    }
}

// 获取链接地址中某个参数的值
function request(url, paras) {
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
    var paraObj = {};
    for (var i = 0; j = paraString[i]; i++) {
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
    }
    var returnValue = paraObj[paras.toLowerCase()];
    if (typeof (returnValue) == "undefined") {
        return "";
    } else {
        return returnValue;
    }
}


// 回调函数，可以获取添加后的访问ID，以便其他操作。
function callback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            visitID = req.responseText.toString();
        }
    } 
}


// 获取系统信息
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

// 获取浏览器类型
function GetBrowserType() {

	// chrome:mozilla/5.0 (windows nt 6.1; wow64) applewebkit/537.36 (khtml, like gecko) chrome/32.0.1700.76 safari/537.36
	// IE11:mozilla/5.0 (windows nt 6.1; wow64; trident/7.0; slcc2; .net clr 2.0.50727; .net clr 3.5.30729; .net clr 3.0.30729; media center pc 6.0; .net4.0c; .net4.0e; rv:11.0) like gecko
    // opera:mozilla/5.0 (windows nt 6.1; wow64) applewebkit/537.36 (khtml, like gecko) chrome/36.0.1985.125 safari/537.36 opr/23.0.1522.60 
	// firefox:mozilla/5.0 (windows nt 6.1; wow64; rv:30.0) gecko/20100101 firefox/30.0
	var ua = navigator.userAgent.toLowerCase();
 
    if (ua == null) return "ie";
    
    else if (ua.indexOf('opr') != -1) return "opera";

    else if (ua.indexOf('chrome') != -1) return "chrome"; 

    else if (ua.indexOf('.net') != -1) return "ie";

    else if (ua.indexOf('safari') != -1) return "safari";

    else if (ua.indexOf('firefox') != -1) return "firefox";

    else if (ua.indexOf('ucbrowser') != -1) return "uc";
  
    else return "ie";

}

// 获取浏览器语言(小写)
function getLang(){ 
    return navigator.language.toLowerCase();  
}




// 获取浏览器版本
function GetBrowserVersion() {

    var ua = navigator.userAgent.toLowerCase();

    if (ua == null) return "null";
    
    else if (ua.indexOf('opr') != -1) return ua.substring(ua.indexOf('opr') + 4, ua.length).split('.')[0];

    else if (ua.indexOf('chrome') != -1) return ua.substring(ua.indexOf('chrome') + 7, ua.length).split('.')[0];

    else if (ua.indexOf('.net') != -1) return ua.substring(ua.indexOf('rv:') + 3, ua.length).split('.')[0];

    else if (ua.indexOf('safari') != -1) return ua.substring(ua.indexOf('safari') + 7, ua.length).split('.')[0];

    else if (ua.indexOf('firefox') != -1) return ua.substring(ua.indexOf('firefox') + 8, ua.length).split('.')[0];
   
    else if (ua.indexOf('ucbrowser') != -1)  return ua.substring(ua.indexOf('ucbrowser') + 10, ua.length).split('.')[0];
    	
    else return "null";

}