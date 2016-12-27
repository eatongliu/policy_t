package com.gpdata.wanyou.azkaban;

/**
 * 调用https，操作Azkaban的示例：
 * Created by guoxy on 2016/10/11.
 */

import com.gpdata.wanyou.utils.ConfigUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class AzkabanOperator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AzkabanOperator.class);
    private static String url = ConfigUtil.getConfig("azkaban.url");
    private static String azkabanUser = ConfigUtil.getConfig("azkaban.user");
    private static String azkabanPassword = ConfigUtil.getConfig("azkaban.password");
    private static String GDI_Project = ConfigUtil.getConfig("GDI_Project");
    private static String GDI_Workflow = ConfigUtil.getConfig("GDI_Workflow");


    /**
     * 登录
     *
     * @return
     * @throws Exception
     */
    public JSONObject login() throws Exception {
        JSONObject result = null;
        String queryStr = "action=login&username=" + azkabanUser + "&password="
                + azkabanPassword;
        result = AzkabanHttpsPost.post(url, queryStr);
        return result;
    }

    public JSONObject executeGDIFlow(String sessionID, String project,
                                     String flow, String cwParams, String smParams, String gdiParams)
            throws Exception {
        JSONObject result = null;
        String executeStr = "session.id=" + sessionID
                + "&ajax=executeFlow&project=" + project + "&flow=" + flow
                + "&flowOverride[cw_params]=" + cwParams
                + "&flowOverride[sm_params]=" + smParams
                + "&flowOverride[gdi_params]=" + gdiParams;
        String executeUrl = url + "/executor";
        result = AzkabanHttpsPost.post(executeUrl, executeStr);
        return result;
    }

    /**
     * 遍历任务执行id的输入流
     *
     * @param sessionID
     * @param execID
     * @return
     * @throws Exception
     */
    public JSONObject fetchFlow(String sessionID, String execID)
            throws Exception {
        JSONObject result = null;
        String executeStr = "session.id=" + sessionID
                + "&ajax=fetchexecflow&execid=" + execID;
        String executeUrl = url + "/executor";
        result = AzkabanHttpsPost.post(executeUrl, executeStr);
        LOGGER.debug("result", result);
        return result;
    }

    /**
     * 新建project
     *
     * @param sessionID,projectName,description
     * @return :{ "status":"success", "path":"manager?project=aaaa", "action":"redirect" }
     */
    public JSONObject creatProject(String sessionID, String projectName, String description)
            throws Exception {
        JSONObject result = null;
        String createStr = "session.id=" + sessionID + "&action=create&name=" + projectName + "&description=" + description;
        String createUrl = url + "manager";
        result = AzkabanHttpsPost.post(createUrl, createStr);
        LOGGER.debug("result", result);
        return result;
    }


}