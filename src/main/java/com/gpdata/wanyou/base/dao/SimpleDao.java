package com.gpdata.wanyou.base.dao;

import com.gpdata.wanyou.system.entity.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chengchao on 2016/10/26.
 */
public interface SimpleDao {

    /**
     * 保存新对象, 返回生成的主键
     *
     * @param bean
     * @param <T>
     * @return
     */
    <T> Serializable save(T bean);

    <T> Serializable save(T bean, boolean flushImmediate);

    /**
     * 修改一个对象
     *
     * @param bean
     * @param <T>
     */
    <T> void update(T bean);


    /**
     * @param id
     * @param partValues
     */
    void update(Serializable id, Object partValues);

    /**
     * 删除一个对象
     *
     * @param bean
     * @param <T>
     */
    <T> void delete(T bean);

    /**
     * 使用主键删除一个对象
     *
     * @param type
     * @param id
     * @param <T>
     */
    <T> void delete(Class<T> type, Serializable id);

    /**
     * 合并方法
     *
     * @param bean
     * @param <T>
     * @return
     */
    <T> T merge(T bean);

    /**
     * 持久化方法
     *
     * @param bean
     * @param <T>
     */
    <T> void persist(T bean);

    /**
     * 使用主键获取一个对象
     *
     * @param type
     * @param id
     * @param <T>
     * @return
     */
    <T> T getById(Class<T> type, Serializable id);


    /**
     * 获取全部数据
     *
     * @param type
     * @param orderByLiteral
     * @param <T>
     * @return
     */
    <T> List<T> getAll(Class<T> type, String orderByLiteral);


}
