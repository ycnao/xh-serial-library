package com.xhuman.serial.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public String path = Environment.getExternalStorageDirectory() + "/";

    public String getSdPath() {
        return path;
    }

    public File creatSDFile(String paramString) {
        File file = null;
        try {
            file = new File(path + paramString);
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    public File createSDDir(String paramString) {
        File file = new File(path + paramString);
        file.mkdir();
        return file;
    }

    public boolean isFileExist(String paramString) {
        File file = new File(path + paramString);
        return file.exists();
    }

    /**
     * 将输入流写入SD卡指定路径的文件
     *
     * @param inputStream 待写入的输入流（参数3）
     * @param dirPath     目录路径（参数1）
     * @param fileName    文件名（参数2）
     * @return 写入成功的文件对象，失败返回null
     * @throws Exception 可能抛出的IO异常
     */
    public File write2SDFromInput(String dirPath, String fileName, InputStream inputStream) {
        File file = null;
        FileOutputStream fos = null;
        int bytesRead = -1; // 记录每次读取的字节数

        try {
            // 1. 创建SD卡目录（忽略返回值）
            createSDDir(dirPath);

            // 2. 拼接文件完整路径（目录+文件名）并创建文件
            String filePath = new StringBuilder(dirPath).append(fileName).toString();
            file = creatSDFile(filePath);

            // 3. 打开文件输出流
            fos = new FileOutputStream(file);

            // 4. 定义缓冲区（153600字节 = 150KB）
            byte[] buffer = new byte[153600];

            // 5. 循环读取输入流并写入输出流
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead); // 写入缓冲区中实际读取的字节
            }

            // 6. 刷新并关闭输出流
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace(); // 打印异常堆栈
            if (fos != null) {
                try {
                    fos.close(); // 异常时关闭输出流
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // 确保资源关闭（字节码中finally逻辑的简化）
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // 返回创建的文件对象
        return file;
    }
}
