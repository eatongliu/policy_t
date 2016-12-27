package com.gpdata.wanyou.ds.util.resource.factory;

import com.gpdata.wanyou.ds.constant.DataSourceConstant;
import com.gpdata.wanyou.ds.util.resource.DataResourceHelper;
import com.gpdata.wanyou.ds.util.resource.impl.MySqlResourceHelper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 数据源工具类工厂方法
 *
 * @author yaz
 * @create 2016-11-08 15:50
 */

public class DataResourceFactory {
    @Autowired
    private MySqlResourceHelper mySqlHelper;


    private static DataResourceFactory dataResourceFactory = new DataResourceFactory();

    public static DataResourceFactory getFactory() {
        return dataResourceFactory;
    }

    private DataResourceFactory() {
    }

    public DataResourceHelper getHelper(int flag) {
        switch (flag) {
            case DataSourceConstant.MYSQL:
                return mySqlHelper;
            default:
                throw new RuntimeException("无相关工厂方法！");
        }
    }
}
