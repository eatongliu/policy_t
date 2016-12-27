package com.gpdata.wanyou.tk.service;

import com.gpdata.wanyou.tk.entity.TaskKettle;

import java.util.List;
import java.util.Map;

/**
 * Created by guoxy on 2016/11/10.
 */
public interface TaskKettleService {
    /**
     * 查询kettle
     *
     * @param kid
     * @return
     */
    TaskKettle getKettle(Integer kid);

    /**
     * 新增kettle任务
     *
     * @param taskKettle
     * @return
     */
    Integer addKettle(TaskKettle taskKettle);

    /**
     * 修改kettle
     *
     * @param taskKettle
     */
    void updateKettle(TaskKettle taskKettle);

    /**
     * 删除kettle
     *
     * @param taskKettle
     */
    void deleteKettle(TaskKettle taskKettle);

    /**
     * kettle列表
     *
     * @param maps
     * @return
     */
    @SuppressWarnings("rawtypes")
    List<TaskKettle> searchTaskKettle(Map<String, String> maps);

    Integer searchTaskKettleTotal(Map<String, String> maps);
}
