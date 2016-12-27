package com.gpdata.wanyou.dingyue;

import com.alibaba.fastjson.JSON;
import com.gpdata.wanyou.policy.entity.PolicyDocument;
import com.gpdata.wanyou.policy.util.FtRetrievalUtil;
import com.gpdata.wanyou.utils.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by guoxy on 2016/12/15.
 */
public class LambdaTest {
    /**
     * lambda 处理数组测试
     */
    @Test
    public void testStringToOther() {
        String keyWords = "打,阿道夫,阿斯顿,发,地方,额外,人,噶,十多个,啊第三个,啊俄方,啊,地方,阿达,发,地方";
        StringBuilder sb = new StringBuilder();
        Arrays.asList(keyWords.split(",")).forEach(str ->
                sb.append(str).append(" "));
        System.out.print(sb);
    }

    @Test
    public void testES() {
        String keyWords = "王宝强";
        String sdate = DateUtils.timeToString(new Date(), "yyyy-MM-dd");
        //System.out.println(sdate);
        PolicyDocument document = new PolicyDocument();
        document.setKeyWords(keyWords);
        //document.setIndex("zcfg/"+sdate);
        document.setIndex("zcfg/");
        String result = FtRetrievalUtil.search(document, 0, 20);
        Pair<Integer, List<Map<String, Object>>> pair = FtRetrievalUtil.parseHits(result);
        //System.out.println(pair.getRight());
        pair.getRight().forEach(policy->{
            PolicyDocument document2 = JSON.parseObject(JSON.toJSONString(policy),PolicyDocument.class);
            System.out.println(document2.getPdId()+""+document2.getPdName()+""+document2.getTopicClassify());
        });




    }
}
