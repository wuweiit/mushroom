package org.marker.mushroom.system.domain.vo;

import lombok.Data;

@Data
public class DatabaseInfoVO {

    //数据库连接配置信息
	private String dbHost;
	private String dbPort;
	private String dbDatabase;
	private String dbCharset;
	private String dbPrefix;
	private String dbDriver;
	private String dbUser;
	private String dbPass;

    //数据库连接池配置信息
    private Integer initialPoolSize;
    private Integer minPoolSize;
    private Integer maxPoolSize;
    private Integer acquireIncrement;
    private Integer maxIdleTime;
    private Integer maxStatements;
}
