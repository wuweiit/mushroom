package org.marker.mushroom.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mushroom.db")
public class MrcmsDatabaseProperties {

    // 数据库驱动类名
    private String driver;
    // 数据库连接 URL
    private String host;
    private int port;
    private String demo;
    private String database;
    // 数据库用户名
    private String user;
    // 数据库密码
    private String pass;


    // 数据源的初始连接数
    private int initialSize;
    // 数据源的最大连接数
    private int maxTotal;

}
