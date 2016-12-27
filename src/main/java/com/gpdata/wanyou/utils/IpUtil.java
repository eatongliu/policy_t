package com.gpdata.wanyou.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by chengchao on 2016/10/27.
 */
public class IpUtil {
    protected static final Logger logger = LoggerFactory.getLogger(IpUtil.class);
    /**
     * 获取登录用户远程主机ip地址
     *
     * @param request
     * @return
     */
    public static final String getIpAddr(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 通过IP获取地理位置信息
     * 使用taobao在线接口
     * @param strIP
     * @return
     */
    public static String getAddressByIP(String strIP) {
        try {
            URL url = new URL( ConfigUtil.getConfig("taobao.checkip") + strIP);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            logger.debug("result: {}" , result);

            JSONObject resultJson = (JSONObject) JSONObject.parse(result.toString());
            logger.debug("resultJson: {}" , resultJson);

            //解析json串，得到国家、地区和城市
            if("0".equals(resultJson.get("code").toString())){
                StringBuilder builder = new StringBuilder();
                JSONObject data = resultJson.getJSONObject("data");
                builder.append(data.get("country"));
                builder.append(data.get("area"));
                builder.append(data.get("city"));
                return builder.toString();
            }
            return null;
        }
        catch( IOException e) {
            logger.error("Exception: {}",e);
            return "读取失败";
        }
    }
}
