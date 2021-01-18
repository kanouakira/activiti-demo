package com.cqkj.activiti6demo.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 写入本地的工具类
 */
public class WriteUtil {
    /**
     *
     * 把InputStream写入本地文件
     * @param dest 本地文件目录
     * @param inputStream 输入流
     * @throws IOException
     */
    public static void writeToLocal(String dest, InputStream inputStream) throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(dest);
        while ((index = inputStream.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        inputStream.close();
    }
}
