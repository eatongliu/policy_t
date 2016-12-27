package com.gpdata.wanyou.utils;

import java.io.*;
import java.util.Enumeration;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将一个或多个文件压缩成zip
 * Created by guoxy on 2016/10/27.
 */
public class ZipUtil {
    protected static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);

    /**
     * 把接受的全部文件打成压缩包
     *
     * @param List<File>;
     * @param org.apache.tools.zip.ZipOutputStream
     */
    public static void zipFile(List files, ZipOutputStream outputStream) {
        int size = files.size();
        for (int i = 0; i < size; i++) {
            File file = (File) files.get(i);
            zipFile(file, outputStream);
        }
    }

    /**
     * 根据输入的文件与输出流对文件进行打包
     *
     * @param File
     * @param org.apache.tools.zip.ZipOutputStream
     */
    public static void zipFile(File inputFile,
                               ZipOutputStream ouputStream) {
        try {
            if (inputFile.exists()) {
                /**如果是目录的话这里是不采取操作的，
                 * 至于目录的打包正在研究中*/
                if (inputFile.isFile()) {
                    try (FileInputStream IN = new FileInputStream(inputFile);
                         BufferedInputStream bins = new BufferedInputStream(IN, 512);) {
                        //org.apache.tools.zip.ZipEntry
                        ZipEntry entry = new ZipEntry(inputFile.getName());
                        ouputStream.putNextEntry(entry);
                        // 向压缩文件中输出数据
                        int nNumber;
                        byte[] buffer = new byte[512];
                        while ((nNumber = bins.read(buffer)) != -1) {
                            ouputStream.write(buffer, 0, nNumber);
                        }
                        // 关闭创建的流对象,try()中声明可自动关闭close
                        //   bins.close();
                        //   IN.close();
                    } catch (Exception e) {
                        logger.error("Exception", e);
                    }


                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], ouputStream);
                        }
                    } catch (Exception e) {
                        logger.error("Exception", e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }

    /**
     * 解压
     */
    /**
     * 解压到指定目录
     *
     * @param zipPath
     * @param descDir
     * @author isea533
     */
    public static void unZipFiles(String zipPath, String descDir) {
        unZipFiles(new File(zipPath), descDir);
    }

    /**
     * 解压文件到指定目录
     *
     * @param zipFile
     * @param descDir
     * @author isea533
     */
    @SuppressWarnings("rawtypes")
    public static void unZipFiles(File zipFile, String descDir) {
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        try {
            ZipFile zip = new ZipFile(zipFile);
            for (Enumeration entries = zip.getEntries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
                try (InputStream in = zip.getInputStream(entry); OutputStream out = new FileOutputStream(outPath)) {
                    //判断路径是否存在,不存在则创建文件路径
                    File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                    if (new File(outPath).isDirectory()) {
                        continue;
                    }
                    //输出文件路径信息
                    logger.debug(outPath);
                    byte[] buf1 = new byte[1024];
                    int len;
                    while ((len = in.read(buf1)) > 0) {
                        out.write(buf1, 0, len);
                    }
                }

            }
            logger.debug("******************解压完毕********************");
        } catch (Exception e) {
            logger.debug("异常:{}", e);
        }

    }
}