package com.gpdata.wanyou.tk.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.tk.dao.TaskKettleDao;
import com.gpdata.wanyou.tk.entity.TaskKettle;
import com.gpdata.wanyou.tk.service.TaskKettleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by guoxy on 2016/11/10.
 */
@Service
public class TaskKettleServiceImpl extends BaseService implements TaskKettleService {
    @Autowired
    private TaskKettleDao taskKettleDao;

    /**
     * 查询kettle
     *
     * @param kid
     * @return
     */
    @Override
    public TaskKettle getKettle(Integer kid) {
        return taskKettleDao.getKettle(kid);
    }

    /**
     * 新增kettle任务
     *
     * @param taskKettle
     * @return
     */
    @Override
    public Integer addKettle(TaskKettle taskKettle) {
        return taskKettleDao.addKettle(taskKettle);
    }

    /**
     * 修改kettle
     *
     * @param taskKettle
     */
    @Override
    public void updateKettle(TaskKettle taskKettle) {
        taskKettleDao.updateKettle(taskKettle);
    }

    /**
     * 删除kettle
     *
     * @param taskKettle
     */
    @Override
    public void deleteKettle(TaskKettle taskKettle) {
        taskKettleDao.deleteKettle(taskKettle);
    }

    /**
     * kettle列表
     *
     * @param maps
     * @return
     */
    @Override
    public List<TaskKettle> searchTaskKettle(Map<String, String> maps) {
        return taskKettleDao.searchTaskKettle(maps);
    }

    @Override
    public Integer searchTaskKettleTotal(Map<String, String> maps) {
        return taskKettleDao.searchTaskKettleTotal(maps);
    }
}
