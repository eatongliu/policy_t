package com.gpdata.wanyou.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chengchao on 16-10-10.
 */
public class SqlCreatorUtil {

    public static final String DATABASE_TYPE_MYSQL = "MYSQL";
    public static final String DATABASE_TYPE_ORACLE = "ORACLE";
    private static final Pattern REPLACE_PARAM_PATTERN = Pattern.compile("\\{\\{\\s*(\\w+)\\s*\\}\\}");

    /**
     * "SELECT * FROM TB_1 WHERE ID = '{{ID}}' "
     * 替换成
     * "SELECT * FROM TB_1 WHERE ID = '123' "
     *
     * @param sqlTemplate
     * @param params
     * @return
     */
    public static final String replaceParam(String sqlTemplate, Map<String, Object> params) {

        if (StringUtils.isBlank(sqlTemplate)) {
            throw new IllegalArgumentException("SQL 模板不存在!");
        }

        StringBuffer result = new StringBuffer(sqlTemplate.length() * 2);

        Matcher matcher = REPLACE_PARAM_PATTERN.matcher(sqlTemplate);
        int i = 0;
        while (matcher.find()) {
            String paramName = matcher.group(1);
            Object value = params.get(paramName);
            if (value == null) {
                throw new IllegalArgumentException("缺少参数 : (" + paramName + ")");
            }
            matcher.appendReplacement(result, value.toString());
        }

        matcher.appendTail(result);

        return result.toString();
    }


    /**
     * @param dataType
     * @return
     */
    public static boolean checkAvailableDataType(String dataType) {

        String upperCaseDataType = dataType.trim().toUpperCase();
        return (upperCaseDataType.equals(DATABASE_TYPE_MYSQL)
                || upperCaseDataType.equals(DATABASE_TYPE_ORACLE));

    }

    /**
     * @param dataType (MySQL、Oracle、File) 等
     * @param sql
     * @param offset
     * @param limit
     * @return
     */
    public static String wrapPagination(String dataType, String sql, Integer offset, Integer limit) {
        String upperCaseDataType = dataType.trim().toUpperCase();

        String result = "";
        switch (upperCaseDataType) {
            case DATABASE_TYPE_MYSQL:
                result = wrapPaginationForMysql(sql, offset, limit);
                break;
            case DATABASE_TYPE_ORACLE:
                result = wrapPaginationForOracle(sql, offset, limit);
                break;
            default:
                result = sql;
        }

        return result;
    }

    private static String wrapPaginationForMysql(String sql, Integer offset, Integer limit) {


        return new StringBuilder().append(sql)
                .append(" limit ")
                .append(offset)
                .append(", ")
                .append(limit)
                .toString();
    }


    private static String wrapPaginationForOracle(String sql, Integer offset, Integer limit) {

        StringBuilder buff = new StringBuilder();
        buff.append("SELECT * FROM (SELECT t_row.*, rownum row_num FROM ( ");

        buff.append(sql);

        buff.append(" ) t_row WHERE rownum <= ");
        buff.append(offset + limit);
        buff.append(" ) WHERE row_num > ");
        buff.append(offset);

        return buff.toString();
    }

}
