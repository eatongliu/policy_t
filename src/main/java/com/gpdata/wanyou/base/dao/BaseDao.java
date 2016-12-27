package com.gpdata.wanyou.base.dao;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by chengchao on 2016/10/25.
 */
public class BaseDao {

    /**
     * 可继承使用的 Logger 对象
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *
     */
    private SessionFactory sessionFactory;

    public static void main(String[] args) {


        String hql = " from User where 1=:1 " +
                "And (x like:X) AND name = :name " +
                "and age0 > :age0 and age2 < :age2 " +
                "and age3 >= :age3 and age4 <= :age4 " +
                "and age3 in (:some) " +
                "and age3 between :begin and :end " +
                "and (age3 = :x or age3 = :y or age3 = :z) " +
                "and age5 != :age3 and age6 <> :age4 ";

        String p = "(?i)(?:and|where)(\\s+\\w+\\s*(?:=|(?i)like|>|>=|>|<|<=|!=|<>)\\s*(:\\w+))";

        Pattern pattern = Pattern.compile(p);

        Matcher matcher = pattern.matcher(hql);

        StringBuffer buff = new StringBuffer();

        Map<String, String> condition = new HashMap<>();


        while (matcher.find()) {
            for (int i = 0; i <= matcher.groupCount(); i++) {
                System.out.printf("%d  %s | ", i, matcher.group(i));
            }
            System.out.println();

            matcher.appendReplacement(buff, "");
        }


        System.out.println(buff);
    }

    /**
     * @return
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Resource
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @return
     */
    public final Session getCurrentSession() {
        return this.getSessionFactory().getCurrentSession();
    }

    private String contractHql(String hql, Map<String, Object> params) {

        if (StringUtils.isBlank(hql)) {
            return hql;
        }

        int position = hql.indexOf(':');
        if (position == -1) {
            return hql;
        }

        Pattern pattern = Pattern.compile("\\w+\\s*(:?=|like)\\s*:\\w+");

        Matcher matcher = pattern.matcher(hql);

        if (matcher.find()) {
            System.out.println(matcher.group(0));
        }

        return "";
    }

    public List<?> queryHqlToList(String hql, List<Pair<String, Object>> paramList) {

        Session session = this.getCurrentSession();

        Query query = session.createQuery(hql);

        if (paramList != null) {

            List<String> keyList = paramList.stream()
                    .filter(pair -> pair.getRight() != null)
                    .map(pair -> pair.getLeft())
                    .collect(Collectors.toList());
        }

        return null;
    }

    public <T> List<T> querySqlToList(Class<T> returnType, String sql, List<Pair<String, Object>> paramList) {

        return null;
    }

    public Object queryHqlToObject(String hql, List<Pair<String, Object>> paramList) {

        List<?> list = this.queryHqlToList(hql, paramList);

        return null;
    }

    public Object querySqlToObject(String hql, List<Pair<String, Object>> paramList) {

        return null;
    }
}
