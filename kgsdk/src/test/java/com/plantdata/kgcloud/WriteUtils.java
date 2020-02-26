package com.plantdata.kgcloud;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class WriteUtils {
    BufferedWriter out = null;

    /*
     * 生成文件
     * @param file 文件路径+文件名称
     * @param content 要生成的文件内容
     */
    public void appendWriteFile(String fileName, String content, boolean space) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("开始以追加的形式写文件到：[" + file + "]");

        try {
            if (out == null) {
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            }
            //换行

            out.write(content);

            log.info("写文件:[" + file + "]完成");
        } catch (Exception e) {
            close();
            log.error("写文件:[" + file + "]异常，异常信息为:[" + e.getMessage() + "]");
        }
    }

    public void close() {
        log.info("关闭流");
        if (out != null) {
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
