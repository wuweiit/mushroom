package org.marker.app.domain;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.marker.app.common.ErrorCode;
import org.marker.mushroom.beans.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 消息对象
 * @author marker
 * @version 1.0
 * 
 * @update 2015-02-11 添加了code属性，描述错误码，绑定国际化资源
 */
public class MessageResult implements Serializable {
	
	/** 表单验证错误 */
	public static final int TYPE_FORM      = -4;
	/** 程序异常 */
	public static final int TYPE_EXCEPTION = -2;
	/** 警告信息  */
	public static final int TYPE_WRANING   = -3;
	
	public static  final  int OperationSuccess = 0;
	public static  final  int OperationError = -1;
	
	/** 日志记录器 */ 
	private Logger logger = LoggerFactory.getLogger(getClass()); 
	

	/** 消息内容缓存区 */
	private static final Map<Integer, String> messages = new HashMap<Integer, String>();
	
	
	/** 状态 */
	protected int status;
	
	/** 消息内容 */
	protected String msg;
	
	/** 消息 */
	protected Object data;
	
	
	/**
	 * 消息
	 * */
	public MessageResult(Object data){
		this.status = OperationSuccess;
		this.data = data;
	}
	
	
	
	/**
	 * 针对返回多个错误码的接口设计
	 * @param errorcode
	 */
	public MessageResult(String[] errorcode){
		this.status = OperationError;
		this.data = errorcode;
	}
	
	
	/**
	 * 针对返回多个错误码的接口设计
	 * @param status
	 */
	public MessageResult(int status){
		this.status = status;
	}


 


 


	/**
	 * 默认操作成功消息
	 */
	public MessageResult() {
		this.status = OperationSuccess;
	}



	/**
	 * 通用操作成功与操作失败处理方法
	 * （当status=true时， 状态码为：0）
	 * （当status=false时，状态码为：100500 ）
	 * 
	 * @param status 状态
	 */
	public MessageResult(boolean status, String data) {
		if(status){
			this.status =  OperationSuccess;
		}else{
			this.status =OperationError;
		}
		this.data = data;
		
	}

	public MessageResult(boolean status, String msg, Object data) {
		if(status){
			this.status =OperationSuccess;
		}else{
			this.status =OperationError;
		}
		this.msg = msg;
		this.data = data; 
	}

	public MessageResult(boolean status, Object data) {
		if(status){
			this.status =OperationSuccess;
		}else{
			this.status =OperationError;
		}
		this.data = data;
	}

	/**
	 * 构造
	 * @param status 状态码
	 * @param msg 消息内容
	 * @param data 数据
	 */
	public MessageResult(int status, String msg, Object data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	/**
	 * 构造消息
	 * 
	 * @param status 状态代码
	 * @param obj 对象
	 */
	public MessageResult(int status, Object obj) {
		this.status = status;
		this.data = obj;
	}



	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}


	/**
	 * 包装错误码
	 * @param errorCode 错误代码
	 * @return
	 */
	public static MessageResult wrapErrorCode(ErrorCode errorCode) {
		return new MessageResult(errorCode.getCode(),errorCode.getMsg(), null);
	}

	/**
	 * 操作成功
	 * @param user
	 * @return
	 */
	public static MessageResult success(Object user) {
		return new MessageResult(true, "操作成功", user);
	}
	/**
	 * 操作成功
	 * @return
	 */
	public static MessageResult success() {
		return new MessageResult(true, null);
	}


	/**
	 * 错误消息
	 * @param msg 消息内容
	 * @return
	 */
	public static Object error(String msg) {
		return new MessageResult(false, msg, null);
	}
}
