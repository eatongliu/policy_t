package com.gpdata.wanyou.utils;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * client实例化类
 */
public class EsClientUtil {
	private static final Logger LOG = LoggerFactory.getLogger(EsClientUtil.class);
	private static String esIP = ConfigUtil.getConfig("ES.url");
	private static String esClusterName = ConfigUtil.getConfig("ES.cluster");
	
	/**
	 * 静态client
	 */
	private static Client client;

	private static EsClientUtil instance = new EsClientUtil();

	/**
	 * 静态获取
	 * @return
	 */
	public static EsClientUtil getInstance(){
		return instance;
	}

	public static Client init() {
		return client;
	}
	
	/**
	 * 初始化client值
	 * @return
	 */
	static {
		if (client == null) {
			//参数配置
			Settings settings = Settings.settingsBuilder()
					.put("cluster.name", esClusterName).build();

			Client currentClient = null;
			try {
				//端口号IP号用配置文件
				currentClient = TransportClient.builder().settings(settings).build()
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esIP), 9300));
			} catch (Exception e) {
				LOG.error("ES的client创建失败！");
			}
			//赋值给静态client
			client = currentClient;
		}
	}

    /**
     * 插入数据到ES
     * @param indexName   库名
     * @param typeName  表名
     * @param json
     * @return 是否创建成功
     */
    public static boolean save2Es(String indexName, String typeName, String json) {
        boolean isCreated = false;
        //创建client
        Client client = EsClientUtil.init();
        //将json数据存入到对应的索引中。
        IndexResponse response = client.prepareIndex (indexName, typeName).setSource(json).execute().actionGet();

        //是否创建成功
        isCreated = response.isCreated();

        return isCreated;
    }
}
