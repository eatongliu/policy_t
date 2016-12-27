package com.gpdata.wanyou.base.flexible.impl;

import com.gpdata.wanyou.base.flexible.FlexibleFileType;
import com.gpdata.wanyou.base.flexible.FlexibleFileUtil;
import com.gpdata.wanyou.utils.ConfigUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * 保存文件之-保存到文件系统
 * <p>
 * <p>
 * saveUploadFileUtilFSImpl
 *
 * @author chengchaos
 */
public class FlexibleFileUtilFSImpl implements FlexibleFileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlexibleFileUtilFSImpl.class);


    private static String RES_LOCAL_PREFIX = ConfigUtil.getConfig("fs.res.loc.prefix");
    private static String PUB_LOCAL_PREFIX = ConfigUtil.getConfig("fs.pub.loc.prefix");

    static {
        if (!PUB_LOCAL_PREFIX.endsWith("/")) {
            PUB_LOCAL_PREFIX += "/";
        }
        if (!RES_LOCAL_PREFIX.endsWith("/")) {
            RES_LOCAL_PREFIX += "/";
        }
    }


    /**
     *
     */
    @Override
    public void saveUploadFile(File srcFile, String rightPartOfPath, FlexibleFileType type) throws IOException {

        String left = null;

        if (FlexibleFileType.PUB.equals(type)) {
            left = PUB_LOCAL_PREFIX;
        }
        if (FlexibleFileType.RES.equals(type)) {
            left = RES_LOCAL_PREFIX;
        }

        String destFilePath = FlexibleFileUtil.replaceToSlantAndConcat(left, rightPartOfPath);
        LOGGER.debug("destFilePath : {}", destFilePath);

        File destFile = new File(destFilePath);
        FileUtils.copyFile(srcFile, destFile);

    }

    /**
     *
     */
    @Override
    public void saveUploadFile(InputStream source, String rightPartOfPath, FlexibleFileType type) throws IOException {


        String left = null;

        if (FlexibleFileType.PUB.equals(type)) {
            left = PUB_LOCAL_PREFIX;
        }
        if (FlexibleFileType.RES.equals(type)) {
            left = RES_LOCAL_PREFIX;
        }


        String destFilePath = FlexibleFileUtil.replaceToSlantAndConcat(left, rightPartOfPath);
        LOGGER.debug("destFilePath : {}", destFilePath);

        File destination = new File(destFilePath);
        FileUtils.copyInputStreamToFile(source, destination);

    }

    @Override
    public void downloadResFile(String rightPartOfPath, String localTargetPath) {
        // TODO Auto-generated method stub
    }


}
