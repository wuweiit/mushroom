$(function(){
	var $sideBar = $('#myAffix');
	$sideBar.affix({
		offset: {
			top: 220,
			bottom:230
		}
	}); 
	// 滚动监听
	$('body').scrollspy({ target: '.navbar-build' });
});