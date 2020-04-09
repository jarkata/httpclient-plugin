package com.jarkata.plugin.client.utils;

import com.jarkata.plugin.client.PluginConstants;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件工具类
 */
public class FileUtils {

    public static final String FILE_DUBBO = "ext-lib";
    public static final String FILE_TYPE_CONFIG = "config";

    /**
     * 获取所有的插件文件的基本路径
     *
     * @return
     */
    public static String getBasePath() {
        String basePath = System.getProperty("user.home") + "/.ideaplugin/httpclient/";
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return basePath;
    }

    /**
     * 获取扩展文件路径
     *
     * @param filetype
     * @return
     */
    public static String getExtPath(String filetype) {
        String basePath = getBasePath();
        basePath = basePath + "/" + filetype;
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return basePath;
    }

    /**
     * 根据作用获取文件
     *
     * @param filetype
     * @return
     */
    public static List<File> getFileList(String filetype) {
        String extPath = getExtPath(filetype);
        File file = new File(extPath);
        File[] files = file.listFiles();
        if (files == null) {
            return new ArrayList<>(0);
        }
        return Arrays.asList(files);
    }

    /**
     * 保存文件内容
     *
     * @param filetype
     * @param filename
     * @param contentFile
     * @throws IOException
     */
    public static void saveFileContent(String filetype, String filename, File contentFile) throws IOException {
        String basePath = getExtPath(filetype);
        basePath = basePath + "/" + filename;
        File file = new File(basePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bis = new BufferedInputStream(new FileInputStream(contentFile));
            int length = bis.available();
            byte[] bytes = new byte[length];
            int read = bis.read(bytes);
            if (read > 0) {
                bos.write(bytes);
            }
        } finally {
            if (bos != null) {
                bos.close();
            }
            if (bis != null) {
                bis.close();
            }
        }
    }

    /**
     * 保存文件
     *
     * @param filename
     * @param content
     * @throws IOException
     */
    public static void saveFileContent(String filetype, String filename, String content) throws IOException {
        String basePath = getExtPath(filetype);
        basePath = basePath + "/" + filename;
        File file = new File(basePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(content.getBytes(StandardCharsets.UTF_8));
        } finally {
            if (bos != null) {
                bos.close();
            }
        }
    }


    public static void saveConfig(String filename, String content) throws IOException {
        saveFileContent(PluginConstants.FILE_TYPE_CONFIG, filename, content);
    }

    /**
     * 读取文件内容
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static String getContent(String filetype, String filename) {
        String basePath = getExtPath(filetype);
        basePath = basePath + "/" + filename;
        try {
            return IOUtils.toString(new FileInputStream(basePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getConfig(String filename) {
        return getContent(PluginConstants.FILE_TYPE_CONFIG, filename);
    }

}
