package org.marker.ext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marker.ext.groovy.GroovyScriptUtil;


/**
 * 模型工具
 * 
 * @author marker
 * @version 1.0
 */
public class ModuleUtils {
	
	protected final Log log = LogFactory.getLog(ModuleUtils.class);
	
	protected GroovyScriptUtil util;

	
	
 
	public void setUtil(GroovyScriptUtil util) {
		this.util = util;
	} 
	
}
