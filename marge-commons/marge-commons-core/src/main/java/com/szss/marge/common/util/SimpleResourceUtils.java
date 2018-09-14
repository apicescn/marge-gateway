package com.szss.marge.common.util;

import java.io.*;

/**
 * @author XXX
 * @date 2018/6/1
 */
public class SimpleResourceUtils {
    /**
     * 获取资源文件内容
     * 
     * @param resourceLocation 资料位置
     * @return
     * @throws FileNotFoundException 没有发现文件
     */
    public static final String fileContent(String resourceLocation) throws FileNotFoundException {
        File file = org.springframework.util.ResourceUtils.getFile(resourceLocation);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuilder sb = new StringBuilder();
        br.lines().forEach(x -> sb.append(x));
        return sb.toString();
    }

    /**
     * 获取资源文件内容,指定编码
     *
     * @param resourceLocation 资料位置
     * @return
     * @throws FileNotFoundException 没有发现文件
     */
    public static final String fileContent(String resourceLocation, String character) throws FileNotFoundException, UnsupportedEncodingException {
        File file = org.springframework.util.ResourceUtils.getFile(resourceLocation);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),character));
        StringBuilder sb = new StringBuilder();
        br.lines().forEach(x -> sb.append(x));
        return sb.toString();
    }

}
