package com.gpdata.wanyou.tk.service.impl;

import com.gpdata.wanyou.base.service.BaseService;
import com.gpdata.wanyou.tk.dao.TaskJobDao;
import com.gpdata.wanyou.tk.entity.TaskJob;
import com.gpdata.wanyou.tk.service.TaskJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 任务
 * Created by guoxy on 2016/10/25.
 */
@Service
public class TaskJobServiceImpl extends BaseService implements TaskJobService {
    @Autowired
    private TaskJobDao taskJobDao;

    /**
     * 4.2.1浏览
     * URI	/tk/jo/g
     * 功能	说明：显示具体某个任务的详细信息。
     * 参数：taskid任务ID（必填）
     * 成功：表task_job中指定taskid的记录
     * 失败：[“error”:”错误原因”]
     *
     * @param taskid
     */
    @Override
    public TaskJob getTaskJobById(int taskid) {
        return taskJobDao.getTaskJobById(taskid);
    }

    /**
     * 4.2.2新增
     * URI	/tk/jo/a
     * 功能1	说明：在任务基本信息表中新增记录。
     * 参数1：templateid模板ID（必填）
     * 参数2：taskname     标题 英文，作为文件名使用（必填）
     * 参数3：retries       自动尝试失败的次数 默认1次
     * 参数4：retrybackoff  每次重试尝试之间的毫秒时间，默认无
     * 参数5：type         命令类型如 command （必填）
     * 参数6：command    根据上边选择的类型编写具体的命令 （必填）
     * 参数7：dependencies  第一个job不可选依赖关系，第二个job可以依赖第一个
     * 成功：[“id”:新增任务的id]
     * 失败：[“error”:”错误原因”]
     *
     * @param taskJob
     */
    @Override
    public int addTaskJob(TaskJob taskJob) {
        return taskJobDao.addTaskJob(taskJob);
    }

    /**
     * 4.2.3修改
     * URI	/tk/jo/u
     * 功能	说明：修改任务信息
     * 参数1：taskid     任务ID（必填）
     * 参数2：taskname   标题 英文，作为文件名使用（必填）
     * 参数3：retries      自动尝试失败的次数 默认1次
     * 参数4：retrybackoff 每次重试尝试之间的毫秒时间，默认无
     * 参数5：type       命令类型如 command （必填）
     * 参数6：command   根据上边选择的类型编写具体的命令 （必填）
     * 参数7：dependencies  第一个job不可选依赖关系，第二个job可以依赖第一个
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     *
     * @param taskJob
     */
    @Override
    public void updateTaskJob(TaskJob taskJob) {
        taskJobDao.updateTaskJob(taskJob);
    }

    /**
     * 4.2.4删除
     * URI	/tk/jo/d
     * 功能	说明：删除某个任务
     * 参数：taskid任务id（必填）
     * 成功：删除任务基本信息表中指定记录
     * 失败：[“error”:”错误原因”]
     *
     * @param taskJob
     */
    @Override
    public void deleteTaskJob(TaskJob taskJob) {
        taskJobDao.deleteTaskJob(taskJob);
    }

    /**
     * 4.2.5检索
     * URI	/tk/jo/q
     * 功能	说明：检索模板信息
     * 参数1：templateid模板ID
     * 成功：检索任务信息列表，
     * 失败：[“error”:”错误原因”]
     *
     * @param maps
     */
    @Override
    public List<TaskJob> searchTaskJob(Map<String, String> maps) {
        return taskJobDao.searchTaskJob(maps);
    }

    @Override
    public Integer searchTaskJobTotal(Map<String, String> maps) {
        return taskJobDao.searchTaskJobTotal(maps);
    }
}
