package com.wuweibi.module4j;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 模块文件封装
 *
 * @author marker
 *         2013-01-19
 */
public final class ModuleFile {

    /**
     * ZipFile对象
     */
    private ZipFile zipFile;


    /**
     * 插件File构造
     *
     * @param pluginPath 文件路径
     * @throws IOException 异常
     */
    public ModuleFile(String pluginPath) throws IOException {
        this(new File(pluginPath));
    }


    /**
     * 插件File构造
     *
     * @param file File对象
     * @throws IOException 异常
     */
    public ModuleFile(File file) throws IOException {
        this.zipFile = new ZipFile(file);
    }


    /**
     * 导出插件文件
     *
     * @param pluginsPath 插件安装文件夹
     * @throws IOException 异常
     */
    public void export(String pluginsPath) throws IOException {
        Enumeration<?> entries = zipFile.entries();
        ZipEntry entry = null;
        while (entries.hasMoreElements()) {
            entry = (ZipEntry) entries.nextElement();
            String entryName = entry.getName();
            if (entryName.endsWith("/")) {//
                continue;
            } else {// 解压
                String pluginPath = pluginsPath + entry.getName();
                this.writeFileByZipEntry(entry, pluginPath);
            }
        }
    }

    /**
     * 解压文件操作
     *
     * @param entry zip
     * @param path  解压路径
     * @throws IOException 异常
     */
    private void writeFileByZipEntry(ZipEntry entry, String path)
            throws IOException {
        InputStream is = zipFile.getInputStream(entry);
        File file = new File(path);
        if (!file.getParentFile().exists())// 如果该文件夹不存在就创建
            file.getParentFile().mkdirs();
        OutputStream ow = new FileOutputStream(file, true);
        byte[] b = new byte[256];
        int len = is.read(b);
        while (len > 0) {
            ow.write(b, 0, len);
            len = is.read(b);
        }
        ow.flush();
        ow.close();
        is.close();
    }


    /**
     * 获取模块名称
     *
     * @return String
     */
    public String getModuleName() {
        Enumeration<?> entries = zipFile.entries();
        ZipEntry entry = null;
        if (entries.hasMoreElements()) {
            entry = (ZipEntry) entries.nextElement();
            String entryName = entry.getName();
            if (entryName.endsWith("/")) {
                return entryName.substring(0, entryName.length() - 1);

            }
        }
        return null;
    }


}
