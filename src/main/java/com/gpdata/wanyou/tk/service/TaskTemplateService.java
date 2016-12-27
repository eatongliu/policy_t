package com.gpdata.wanyou.tk.service;

import com.gpdata.wanyou.tk.entity.TaskTemplate;

import java.util.List;
import java.util.Map;

/**
 * 任务模板
 * Created by guoxy on 2016/10/25.
 */
public interface TaskTemplateService {
    /**
     * 4.1.1浏览
     * URI	/tk/tp/g
     * 功能说明：显示具体某个模板的详细信息。
     * 参数1：templateid模板ID（必填）
     * 参数2：temptype 模板类型（0添加    1上传）
     * 成功：表task_ template中指定templateid的记录
     * 失败：[“error”:”错误原因”]
     */
    TaskTemplate getTaskTemplateById(int tempId);

    /**
     * 4.1.2新增
     * URI	/tk/tp/a
     * 功能说明：在模板基本信息表中新增记录。
     * 参数1：caption标题 英文，作为文件名使用（必填）
     * 参数2：remark说明
     * 成功：[“templateid”:新增模板的templateid]
     * 失败：[“error”:”错误原因”]
     */
    int addTaskTemplate(TaskTemplate taskTemplate);

    /**
     * 4.1.3修改
     * URI	/tk/tp/u
     * 功能说明：修改模板信息
     * 参数1：templateid模板id（必填）
     * 参数2：caption 标题 英文，作为文件名使用
     * 参数3：remark说明
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     */
    void updateTaskTemplate(TaskTemplate taskTemplate);

    /**
     * 4.1.4删除
     * URI	/tk/tp/d
     * 功能	说明：删除某个模板
     * 参数：templateid模板id（必填）
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     */
    void deleteTaskTemplate(TaskTemplate taskTemplate);

    /**
     * 4.1.5检索
     * URI	/tk/tp/q
     * 功能	说明：检索模板信息
     * 参数1：caption标题
     * 参数2：temptype 模板类型（0添加    1上传）
     * 成功：检索模板信息列表，caption模糊匹配
     * 失败：[“error”:”错误原因”]
     */
    @SuppressWarnings("rawtypes")
    List<TaskTemplate> searchTaskTemplate(Map<String, String> maps);

    Integer searchTaskTemplateTotal(Map<String, String> maps);
    /**
     * 4.1.6上传
     URI	/tk/tp/up
     功能	说明：上传模板文件
     参数：zip文件
     成功：检索模板信息列表，
     失败：[“error”:”错误原因”]

     */
}
