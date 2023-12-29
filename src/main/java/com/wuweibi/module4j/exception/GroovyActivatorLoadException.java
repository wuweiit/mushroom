package com.wuweibi.module4j.exception;

import java.io.IOException;


/**
 * ActivatorLoad 错误
 * @author marker
 *
 */
public class GroovyActivatorLoadException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8035708850031939641L;


    /**
     * 构造
     * @param e 异常
     */
    public GroovyActivatorLoadException(Exception e) {
        super(e);
    }
}
