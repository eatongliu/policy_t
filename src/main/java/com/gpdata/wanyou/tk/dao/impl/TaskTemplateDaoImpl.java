package com.gpdata.wanyou.tk.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.tk.dao.TaskTemplateDao;
import com.gpdata.wanyou.tk.entity.TaskTemplate;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 任务模板
 * Created by guoxy on 2016/10/25.
 */
@Repository
public class TaskTemplateDaoImpl extends BaseDao implements TaskTemplateDao {
    /**
     * 4.1.1浏览
     * URI	/tk/tp/g
     * 功能说明：显示具体某个模板的详细信息。
     * 参数1：templateid模板ID（必填）
     * 参数2：temptype 模板类型（0添加    1上传）
     * 成功：表task_ template中指定templateid的记录
     * 失败：[“error”:”错误原因”]
     *
     * @param tempId
     */
    @Override
    public TaskTemplate getTaskTemplateById(int tempId) {
        return (TaskTemplate) this.getCurrentSession().get(TaskTemplate.class, tempId);
    }

    /**
     * 4.1.2新增
     * URI	/tk/tp/a
     * 功能说明：在模板基本信息表中新增记录。
     * 参数1：caption标题 英文，作为文件名使用（必填）
     * 参数2：remark说明
     * 成功：[“templateid”:新增模板的templateid]
     * 失败：[“error”:”错误原因”]
     *
     * @param taskTemplate
     */
    @Override
    public int addTaskTemplate(TaskTemplate taskTemplate) {
        this.getCurrentSession().save(taskTemplate);
        return taskTemplate.getTemplateId();
    }

    /**
     * 4.1.3修改
     * URI	/tk/tp/u
     * 功能说明：修改模板信息
     * 参数1：templateid模板id（必填）
     * 参数2：caption 标题 英文，作为文件名使用
     * 参数3：remark说明
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     *
     * @param taskTemplate
     */
    @Override
    public void updateTaskTemplate(TaskTemplate taskTemplate) {
        this.getCurrentSession().merge(taskTemplate);
    }

    /**
     * 4.1.4删除
     * URI	/tk/tp/d
     * 功能	说明：删除某个模板
     * 参数：templateid模板id（必填）
     * 成功：”success”
     * 失败：[“error”:”错误原因”]
     *
     * @param taskTemplate
     */
    @Override
    public void deleteTaskTemplate(TaskTemplate taskTemplate) {
        this.getCurrentSession().delete(taskTemplate);
    }

    /**
     * 4.1.5检索
     * URI	/tk/tp/q
     * 功能	说明：检索模板信息
     * 参数1：caption 标题
     * 参数2：temptype 模板类型（0添加    1上传）
     * 成功：检索模板信息列表，caption模糊匹配
     * 失败：[“error”:”错误原因”]
     *
     * @param maps
     */
    @Override
    public List<TaskTemplate> searchTaskTemplate(Map<String, String> maps) {

        String caption = maps.get("caption");
//        String creator = maps.get("creator");
        String temptype = maps.get("temptype");
        Integer tempType = 0;
        if (temptype != null && temptype.length() != 0) {
            tempType = Integer.valueOf(temptype, 10);
        }
        String offset = maps.get("offset");
        String limit = maps.get("limit");

        String order = maps.get("order");
        String hql = "from TaskTemplate where 1=1";


        if (caption != null && caption.length() != 0) {
            hql += " and caption like :caption ";
        }
        if (tempType != null) {
            hql += " and tempType = :tempType ";
        }
        if (order != null && order.length() != 0 && "desc".equals(order)) {
            hql += " order by caption desc ";

        }

        Query query = getCurrentSession().createQuery(hql);
        if (caption != null && caption.length() != 0) {
            query.setString("caption", "%" + caption + "%");
        }
        if (tempType != null) {
            query.setInteger("tempType", tempType);
        }
        List<TaskTemplate> taskTemplateList = null;
        if (offset != null && limit != null) {
            Integer start = Integer.valueOf(offset, 10);
            Integer size = Integer.valueOf(limit, 10);
            taskTemplateList = query
                    .setFirstResult(start)
                    .setMaxResults(size).list();
            return taskTemplateList;
        }
        taskTemplateList = query.list();

        return taskTemplateList;
    }

    @Override
    public Integer searchTaskTemplateTotal(Map<String, String> maps) {

        String caption = maps.get("caption");
//        String creator = maps.get("creator");
        String temptype = maps.get("temptype");
        Integer tempType = 0;
        if (temptype != null && temptype.length() != 0) {
            tempType = Integer.valueOf(temptype, 10);
        }
        String hql = "SELECT count(*) from TaskTemplate where 1=1";

        if (caption != null && caption.length() != 0) {
            hql += " and caption like :caption ";
        }
        if (tempType != null) {
            hql += " and tempType = :tempType ";
        }

        Query totalQuery = getCurrentSession().createQuery(hql);
        if (caption != null && caption.length() != 0) {
            totalQuery.setString("caption", "%" + caption + "%");
        }
        if (tempType != null) {
            totalQuery.setInteger("tempType", tempType);
        }

        String result = totalQuery.uniqueResult().toString();
        Integer total = Integer.valueOf(result, 10);
        return total;
    }
}
