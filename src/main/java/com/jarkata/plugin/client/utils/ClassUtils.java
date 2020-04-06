package com.jarkata.plugin.client.utils;

import com.intellij.util.lang.UrlClassLoader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * CLass工具类
 */
public class ClassUtils {

    /**
     * 根据className获取class对象
     *
     * @param clzzName
     * @return
     * @throws Exception
     */
    public static Class<?> getClass(String clzzName) throws Exception {
        URLClassLoader classLoader = getClassLoader();
        return classLoader.loadClass(clzzName);
    }

    /**
     * 根据Object对象获取方法
     *
     * @param object
     * @param method
     * @return
     */
    public static Method getMethod(Object object, String method) {
        Class<?> objectClass = object.getClass();
        for (Method declaredMethod : objectClass.getDeclaredMethods()) {
            if (method.equals(declaredMethod.getName())) {
                return declaredMethod;
            }
        }
        return null;
    }

    /**
     * 获取选择的方法
     *
     * @param selectClazz
     * @param method
     * @return
     */
    public static Method getMethod(Class<?> selectClazz, String method) {
        for (Method declaredMethod : selectClazz.getDeclaredMethods()) {
            if (method.equals(declaredMethod.getName())) {
                return declaredMethod;
            }
        }
        return null;
    }


    /**
     * 把指定目录下的文件目录加入ClassLoader
     *
     * @return
     * @throws Exception
     */
    public static URLClassLoader getClassLoader() throws Exception {
        String extPath = FileUtils.getExtPath(FileUtils.FILE_DUBBO);
        File directorys = new File(extPath);
        File[] jarFileList = directorys.listFiles((dir, name) -> name.endsWith(".jar"));
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URLClassLoader urlClassLoader = null;
        System.out.println(contextClassLoader);
        if (contextClassLoader instanceof UrlClassLoader) {
            urlClassLoader = (URLClassLoader) contextClassLoader;
        }
        if (urlClassLoader == null) {
            urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            System.out.println("systemClassLoader:" + urlClassLoader);
        }
        System.out.println(urlClassLoader);
        // URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

        Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        boolean accessible = addURLMethod.isAccessible();
        try {
            addURLMethod.setAccessible(true);
            assert jarFileList != null;
            for (File file : jarFileList) {
                URL url = file.toURI().toURL();
                addURLMethod.invoke(urlClassLoader, url);
            }
        } finally {
            addURLMethod.setAccessible(accessible);
        }
        return urlClassLoader;
    }

}
