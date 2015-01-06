
function ins_form(){
	test_data();
	if($('#test_conn').val()==0){
		return false();
	}else{
		$("form").submit();
	}
}

function test_data(){
	 val=$('#create').attr("checked");
     if(val){
		 create=1;
     }else{
     	create=0;
     } 
	 $.post( 'data.jsp?action=test_data',
	 {
		 DB_HOST:$('#DB_HOST').val(),
		 DB_PORT:$('#DB_PORT').val(),
		 DB_NAME:$('#DB_NAME').val(),
		 DB_USER:$('#DB_USER').val(),
		 DB_PWD:$('#DB_PWD').val(),
		 create:create
	 }, function(html){
		 if(html==1){
			 $('.msg').html('<span style="color:green">数据库连接成功</span>');
			 $('#test_conn').val('1');
		 }else{
			 $('.msg').html('<span style="color:red">'+html+'</span>');
			 $('#test_conn').val('0');
		 }
	 },'html');
}
