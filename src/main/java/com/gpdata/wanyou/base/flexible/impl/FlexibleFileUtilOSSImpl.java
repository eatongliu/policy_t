package com.gpdata.wanyou.base.flexible.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.gpdata.wanyou.base.flexible.FlexibleFileType;
import com.gpdata.wanyou.base.flexible.FlexibleFileUtil;
import com.gpdata.wanyou.utils.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.function.Supplier;

/**
 * <p>
 * 保存文件之-保存到阿里 OSS
 * <p>
 * <p>
 * saveUploadFileUtilOSSImpl
 *
 * @author chengchaos
 */
public class FlexibleFileUtilOSSImpl implements FlexibleFileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlexibleFileUtilOSSImpl.class);

    private static String ALI_OSS_PUB_BUCKET = ConfigUtil.getConfig("ali.oss.pub.bucket");
    private static String ALI_OSS_RES_BUCKET = ConfigUtil.getConfig("ali.oss.res.bucket");


    /**
     * Note that there are two ways of uploading an object to your bucket, the one
     * by specifying an input stream as content source, the other by specifying a file.
     */
    private static String endpoint = ConfigUtil.getConfig("ali.oss.endpoint");
    private static String accessKeyId = ConfigUtil.getConfig("ali.oss.AccessKeyId");
    private static String accessKeySecret = ConfigUtil.getConfig("ali.oss.AccessKeySecret");


    private void putTemplateMethod(Supplier<PutObjectRequest> putObjectRequestSupplier) throws IOException {
        
        /*
         * Constructs a client instance with your account for accessing OSS
         */
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        try {
            /*
             * Upload an object to your bucket from a file
             */
            LOGGER.debug("Uploading a new object to OSS from a file\n");
            //client.putObject(new PutObjectRequest(bucketName, rightPartOfPath, source));
            client.putObject(putObjectRequestSupplier.get());

        } catch (OSSException oe) {
            LOGGER.debug("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            LOGGER.debug("Error Message: {}", oe.getErrorCode());
            LOGGER.debug("Error Code:    {}", oe.getErrorCode());
            LOGGER.debug("Request ID:    {}", oe.getRequestId());
            LOGGER.debug("Host ID:       {}", oe.getHostId());
        } catch (ClientException ce) {
            LOGGER.debug("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            LOGGER.debug("Error Message: {}", ce.getMessage());
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            client.shutdown();
        }
    }


    @Override
    public void saveUploadFile(File source, String rightPartOfPath, FlexibleFileType type) throws IOException {

        String left = null;
        if (FlexibleFileType.PUB.equals(type)) {
            left = ALI_OSS_PUB_BUCKET;
        }
        if (FlexibleFileType.RES.equals(type)) {
            left = ALI_OSS_RES_BUCKET;
        }
        String bucketName = left;
        String key = FlexibleFileUtil.replaceToSlant(rightPartOfPath);
        this.putTemplateMethod(() -> new PutObjectRequest(bucketName, key, source));

    }

    @Override
    public void saveUploadFile(InputStream source, String rightPartOfPath, FlexibleFileType type) throws IOException {
        String left = null;
        if (FlexibleFileType.PUB.equals(type)) {
            left = ALI_OSS_PUB_BUCKET;
        }
        if (FlexibleFileType.RES.equals(type)) {
            left = ALI_OSS_RES_BUCKET;
        }
        String bucketName = left;
        String key = FlexibleFileUtil.replaceToSlant(rightPartOfPath);
        this.putTemplateMethod(() -> new PutObjectRequest(bucketName, key, source));

    }


    @Override
    public void downloadResFile(String rightPartOfPath, String localTargetPath) throws IOException {

        String key = FlexibleFileUtil.replaceToSlant(rightPartOfPath);
        String bucketName = ALI_OSS_RES_BUCKET;
        
        /*
         * Constructs a client instance with your account for accessing OSS
         */
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        OSSObject object = null;
        InputStream is = null;
        FileOutputStream os = null;
        try {
            /*
             * Download an object from your bucket
             */
            LOGGER.debug("Downloading an object");
            object = client.getObject(new GetObjectRequest(bucketName, key));
            LOGGER.debug("Content-Type: {}", object.getObjectMetadata().getContentType());
            is = object.getObjectContent();
            File localTargetFile = new File(localTargetPath);
            localTargetFile.getParentFile().mkdirs();

            os = new FileOutputStream(localTargetFile);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
            }
        } catch (FileNotFoundException fe) {
            LOGGER.debug("{}", fe);

        } catch (OSSException oe) {
            LOGGER.debug("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            LOGGER.debug("Error Message: {}", oe.getErrorCode());
            LOGGER.debug("Error Code:    {}", oe.getErrorCode());
            LOGGER.debug("Request ID:    {}", oe.getRequestId());
            LOGGER.debug("Host ID:       {}", oe.getHostId());
        } catch (ClientException ce) {
            LOGGER.debug("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            LOGGER.debug("Error Message: {}", ce.getMessage());
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            client.shutdown();
            try {
                os.close();
            } catch (IOException ignore) {
                /* ignore */
            }
            try {
                is.close();
            } catch (IOException ignore) {
                /* ignore */
            }
        }

    }


}
