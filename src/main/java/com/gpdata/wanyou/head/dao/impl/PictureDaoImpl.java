package com.gpdata.wanyou.head.dao.impl;

import com.gpdata.wanyou.base.dao.BaseDao;
import com.gpdata.wanyou.head.dao.PictureDao;
import com.gpdata.wanyou.head.entity.Picture1Map;
import com.gpdata.wanyou.head.entity.Picture2Contrast;
import com.gpdata.wanyou.head.entity.Picture3Pentagon;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaz
 * @create 2016-12-16 11:08
 */
@Repository
public class PictureDaoImpl extends BaseDao implements PictureDao {
    @Override
    public Map<String, List> getAll() {
        HashMap<String, List> res = new HashMap<>();
        List<Picture1Map> p1 = getCurrentSession().createQuery("from Picture1Map").list();
        List<Picture2Contrast> p2 = getCurrentSession().createQuery("from Picture2Contrast").list();
        List<Picture3Pentagon> p3 = getCurrentSession().createQuery("from Picture3Pentagon").list();
        res.put("p1", p1);
        res.put("p2", p2);
        res.put("p3", p3);
        return res;
    }
}
