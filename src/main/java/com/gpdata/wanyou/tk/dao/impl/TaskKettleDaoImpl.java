package com.gpdata.wanyou.tk.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.tk.dao.TaskKettleDao;
import com.gpdata.wanyou.tk.entity.TaskKettle;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * kettle
 * Created by guoxy on 2016/11/10.
 */
@Repository
public class TaskKettleDaoImpl extends BaseDao implements TaskKettleDao {
    /**
     * 查询kettle
     *
     * @param kid
     * @return
     */
    @Override
    public TaskKettle getKettle(Integer kid) {
        return (TaskKettle) this.getCurrentSession().get(TaskKettle.class, kid);
    }

    /**
     * 新增kettle任务
     *
     * @param taskKettle
     * @return
     */
    @Override
    public Integer addKettle(TaskKettle taskKettle) {
        this.getCurrentSession().save(taskKettle);
        return taskKettle.getKettleId();
    }

    /**
     * 修改kettle
     *
     * @param taskKettle
     */
    @Override
    public void updateKettle(TaskKettle taskKettle) {
        this.getCurrentSession().merge(taskKettle);
    }

    /**
     * 删除kettle
     *
     * @param taskKettle
     */
    @Override
    public void deleteKettle(TaskKettle taskKettle) {
        this.getCurrentSession().delete(taskKettle);
    }

    /**
     * kettle列表
     *
     * @param maps
     * @return
     */
    @Override
    public List<TaskKettle> searchTaskKettle(Map<String, String> maps) {
        Integer kettleId = Integer.valueOf(maps.get("kettleId"), 10);
        String offset = maps.get("offset");
        String limit = maps.get("limit");
        String order = maps.get("order");
        String kettleName = maps.get("kettleName");
        String hql = "from TaskKettle where 1=1";
        if (kettleId != null) {
            hql += " and kettleId =:kettleId";
        }
        if (kettleName != null) {
            hql += " and kettleName like :kettleName";
        }
        Query query = getCurrentSession().createQuery(hql);
        if (kettleId != null) {
            query.setInteger("kettleId", kettleId);
        }
        if (kettleName != null) {
            query.setString("kettleName", "%" + kettleName + "%");
        }
        if (order != null && order.length() != 0 && "desc".equals(order)) {
            hql += " order by  createdate desc";
        }
        //返回list
        @SuppressWarnings("unchecked")
        List<TaskKettle> taskKettleList = null;
        if (offset != null && limit != null) {
            Integer start = Integer.valueOf(offset, 10);
            Integer size = Integer.valueOf(limit, 10);
            taskKettleList = query
                    .setFirstResult(start)
                    .setMaxResults(size).list();
            return taskKettleList;
        } else {
            taskKettleList = query.list();
        }


        return taskKettleList;
    }

    @Override
    public Integer searchTaskKettleTotal(Map<String, String> maps) {
        Integer kettleId = Integer.valueOf(maps.get("kettleId"), 10);
        String kettleName = maps.get("kettleName");
        String hql = "SELECT count(*) from TaskKettle where 1=1";
        if (kettleId != null) {
            hql += " and kettleId =:kettleId";
        }
        if (kettleName != null) {
            hql += " and kettleName like :kettleName";
        }
        Query totalQuery = getCurrentSession().createQuery(hql);
        if (kettleId != null) {
            totalQuery.setInteger("kettleId", kettleId);
        }
        if (kettleName != null) {
            totalQuery.setString("kettleName", "%" + kettleName + "%");
        }
        String total = totalQuery.uniqueResult().toString();
        return Integer.valueOf(total, 10);
    }
}
