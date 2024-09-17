package org.marker.mushroom.core.config.impl;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.marker.mushroom.core.config.ConfigDBEngine;
import org.marker.mushroom.holder.SpringContextHolder;


/**
 * 存储配置
 *
 * @author marker
 */
@Setter
@Getter
@NoArgsConstructor
public final class StorageConfig extends ConfigDBEngine<StorageConfig> {


    /**
     * 存储类型 ALIYUN_OSS、LOCAL_OSS
     */
    private String storageType;

    /**
     * 阿里云oss 配置
     */
    private AliyunOssProperties aliyunOss;

	/**
	 * 本地存储
	 */
    private LocalStorageProperties localOss;

    /**
     * 获取实例
     *
     * @return SystemConfig
     */
    public static StorageConfig getInstance() {
        return SpringContextHolder.getApplicationContext().getBean(StorageConfig.class);
    }
    // 自定义 getStorageType 方法
    public String getStorageType() {
        if (storageType == null) {
            return this.properties.getProperty("storageType");
        }
        return this.storageType;
    }


    /**
     * 阿里云OSS配置
     *
     * @author marker
     */
    @Data
    public static class AliyunOssProperties {
        /**
         * accessKeyId
         */
        private String accessKeyId;
        /**
         * accessKeySecret
         */
        private String accessKeySecret;
        /**
         * bucket
         */
        private String bucket;
    }

    /**
     * 本地存储配置
     *
     * @author marker
     */
    @Data
    public static class LocalStorageProperties {

        /**
         * 文件存储基础路径
         */
        private String baseFilePath;
    }


}
