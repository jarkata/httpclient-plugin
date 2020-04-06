package com.jarkata.plugin.client.utils;

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

    public static final String FILE_DUBBO = "dubbo-lib";

    /**
     * 获取所有的插件文件的基本路径
     *
     * @return
     */
    public static String getBasePath() {
        String basePath = System.getProperty("user.home") + "/.ideaplugin/httpclient";
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

    public static List<File> getFileList(String filetype) {
        String extPath = getExtPath(filetype);
        File file = new File(extPath);
        File[] files = file.listFiles();
        if (files == null) {
            return new ArrayList<>(0);
        }
        return Arrays.asList(files);
    }

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
    public static void saveFileContent(String filename, String content) throws IOException {
        String basePath = getBasePath();
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
    
    /**
     * 读取文件内容
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static String getContent(String filename) throws IOException {
        String basePath = getBasePath();
        basePath = basePath + "/" + filename;
        return IOUtils.toString(new FileInputStream(basePath), StandardCharsets.UTF_8);
    }

}
