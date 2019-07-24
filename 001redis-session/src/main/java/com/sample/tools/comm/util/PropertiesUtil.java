package com.sample.tools.comm.util;


import java.io.*;
import java.util.Properties;

import cn.hutool.core.io.FileUtil;

public class PropertiesUtil {

    public static Properties getPropertiesByFileName(String fileName) {
        Properties p = null;
        InputStream is = null;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
        	/*
        	Hutool工具类
        	File file = FileUtil.file(fileName);
        	System.out.println("读取db配置:" + file.getAbsolutePath());
        	is=FileUtil.getInputStream(file);
            */
            /*
            Spring工具类
            File file = ResourceUtils.getFile("classpath:" + fileName);
            is = new FileInputStream(file);
			*/
            p = new Properties();
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(is == null) return null;
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }

    public static String readProperty(Properties p, String key) {
        return p.getProperty(key);
    }
    
    public static String readProperty(String fileName, String key) {
        String value = "";
        InputStream is = null;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
            Properties p = new Properties();
            p.load(is);
            value = p.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static void writeProperty(String properiesName, String key, String value) {
        InputStream is = null;
        OutputStream os = null;
        Properties p = new Properties();
        try {
            is = new FileInputStream(properiesName);
            p.load(is);
            os = new FileOutputStream(PropertiesUtil.class.getClassLoader().getResource(properiesName).getFile());

            p.setProperty(key, value);
            p.store(os, key);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
                if (null != os)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}