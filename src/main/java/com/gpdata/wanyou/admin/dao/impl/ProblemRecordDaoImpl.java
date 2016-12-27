package com.gpdata.wanyou.admin.dao.impl;

import com.gpdata.wanyou.admin.dao.ProblemRecordDao;
import com.gpdata.wanyou.admin.entity.ProblemRecord;
import com.gpdata.wanyou.base.dao.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ligang on 2016/12/10.
 */
@Repository
public class ProblemRecordDaoImpl extends BaseDao implements ProblemRecordDao {

    @Override
    public Map<String, Object> getProblemRecordList(String input, Integer limit, Integer offset) {

        String sql = "SELECT t.* from problem_record t WHERE t.dispStatus = '1' ";
        String totalSql = "select count(t.prId)  FROM problem_record t WHERE t.dispStatus = '1' ";

        if (StringUtils.isNotBlank(input)) {
            sql += " AND (t.mainTitle LIKE :input or t.subtitle LIKE :input or t.remark LIKE :input or t.content LIKE :input)";
            totalSql += " AND (t.mainTitle LIKE :input or t.subtitle LIKE :input or t.remark LIKE :input or t.content LIKE :input)";
        }
        Query query = this.getCurrentSession().createSQLQuery(sql).setFirstResult(offset).setMaxResults(limit);
        SQLQuery totalQuery = getCurrentSession().createSQLQuery(totalSql);
        if (StringUtils.isNotBlank(input)) {
            query.setString("input", "%" + input + "%");
            totalQuery.setString("input", "%" + input + "%");
        }
        List<ProblemRecord> problemRecordList = query
                .setResultTransformer(Transformers.aliasToBean(ProblemRecord.class))
                .list();
        int total = Integer.parseInt(totalQuery.uniqueResult().toString());
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", problemRecordList);
        return map;
    }
}
