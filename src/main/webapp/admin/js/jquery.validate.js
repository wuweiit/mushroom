$.fn.extend({
    checkRequired:function(inputArg){
        if(inputArg.required){
            if($(this).is("input") || $(this).is("textarea")){
                //绑定获得焦点事件
                $(this).bind("focus",function(){
                    if(inputArg.onFocus!=undefined){
                    	 $("#" + inputArg.tipId).html(inputArg.onFocus);
                    }
                });
                
                //绑定失去焦点事件
                $(this).bind("blur",function(){
                    if($(this).val()!=undefined && $(this).val()!=""){
                        $("#" + inputArg.tipId).html(inputArg.onSucces);
                    }else{
                        $("#" + inputArg.tipId).html(inputArg.onBlur);
                    }
                });
            }
        }
    }
});

