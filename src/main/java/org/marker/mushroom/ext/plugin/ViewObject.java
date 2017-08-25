package org.marker.mushroom.ext.plugin;


import java.io.Serializable;

/**
 * 视图对象
 *
 * @author marker
 */
public class ViewObject implements Serializable {

	/** 返回对象 */
	private Object result;

	/** 视图类型 */
	private ViewType type = ViewType.NONE;
	
	
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public ViewType getType() {
		return type;
	}
	public void setType(ViewType type) {
		this.type = type;
	}
	
	
}
