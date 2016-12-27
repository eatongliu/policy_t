package com.gpdata.wanyou.utils;

import com.gpdata.wanyou.policy.entity.PolicyDocument;
import com.gpdata.wanyou.policy.util.FtRetrievalUtil;

public class ESTest {
	public static void main(String[] args) {
		PolicyDocument policyDocument = new PolicyDocument();
		//多个索引库
		policyDocument.setIndex("zcfg,zcwj,zcjd");
		
	   	// 精确匹配：
		//  pdId
		//policyDocument.setPdId(3L);
		//	“isEffect”:””,      //是否有效 0 否 1 是    “精确“
		//policyDocument.setIsEffect("1");
		//	“isPilot”:””,      //是否试点 0 否 1 是     “精确“
		//policyDocument.setIsPilot("0");
		//	“isPub”:””,        //是否发布  0 否 1 是   默认 0    “精确“
		//policyDocument.setIsPub("0");
		//	“isHide”:””,        //是否隐藏  0 否 1 是   默认 1    “精确“
		//policyDocument.setIsHide("1");
			
		//	模糊匹配：
		//	“pdName”:””,       //文件名称   “模糊”
		//policyDocument.setPdName("上海");
		//	“topicClassify”:””,  //主题分类   “模糊”
		//policyDocument.setTopicClassify("主题");
		//	“pubOrg”:””,      //颁布机构   “模糊”
		//policyDocument.setPubOrg("上海市人民政府");
		//	“placed”:””,       //具体单位   “模糊”
		//policyDocument.setPlaced("北京市人民政府");
		//	“createDate”:””,    //成文日期   区间查询  startTime   endTime  
		//policyDocument.setStartTime("2010");
		//policyDocument.setEndTime("2020");
		//	“issuedNum”:””,    //发文字号   “模糊”
		//policyDocument.setIssuedNum("第46号公布");
		//	“content”:””     //文件内容   “模糊”
		policyDocument.setKeyWords("");
		//	“province”:””     //省      “模糊”
		//policyDocument.setProvince("北京");
		//	“city”:””     //市            “模糊”
		//policyDocument.setCity("上海");
		//	“county”:””     //县       “模糊”
		//policyDocument.setCounty("");
			
		//	关键字：
		//	“keyWords”:””     //查询关键字    “模糊”		

    	//排序
    	// sort
		//policyDocument.setOrder("n");

		System.out.println(FtRetrievalUtil.search(policyDocument, 0, 100));
	}
}
