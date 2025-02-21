package com.wuweibi.module4j.exception;


import java.io.IOException;

/**
 * 模块移动异常
 * @author marker
 */
public class ModuleMoveException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1700620098387621128L;


    /**
     * 构造
     * @param e 异常
     */
    public ModuleMoveException(IOException e) {
        super(e);
    }
}
