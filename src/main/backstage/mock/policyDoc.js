/**
 * Created by Administrator on 2016/12/21.
 */
/**
 * 文件列表
 * */

/**条件查询
 * 参数1：startTime 查询起始年份
 * 参数2：endTime 查询结束年份
 * 参数3：keyWords 查询关键字
 * 参数4：index   文件类型 1政策法规 zcfg 2 政策文件zcwj  3政策解读zcjd  4 建议提案jyta
 * 参数5：isPub  是否发布  0 否 1 是
 * 参数6：limit
 * 参数7：offset
 * 成功：文件列表["status":"success","data":{"rows":消息列表,"total":总数}]
 * 失败：[“error”:”错误原因”]
 */
module.exports = {
  'GET /admin/policydoc': function (req, res) {
    setTimeout(function () {
      req.json({
        "startTime": "",
        "endTime": "",
        "keyWords": "",
        "index": "",
        "isPub": "",
        "limit": "",
        "offset": ""
      }),
        res.json({
          "status": "SUCCESS",
          "cause": null,
          "data": {
            "total": 1,
            "rows": [
              {
                "pdId": "1",    //文件编号
                "pdName": "这是个名字a",  //文件名称
                "topicClassify": "11",  //主题分类
                "pubOrg": "666",  //颁布机构
                "placed": "好的", //具体单位
                "createDate": "2016-12-20", //成文日期
                "createYear": "2016",  //成文年份
                "issuedNum": "号号号",   //发文字号
                "link": "走你",  //原文链接
                "linkAddress": "www.zouni.com", //原文链接地址
                "isEffect": "effect", //是否有效
                "isPilot": "33",  //是否试点
                "isPub": "1",  //是否发布
                "isHide": "0",  //是否隐藏
                "province": "雾省2",   //省
                "city": "霾市2",  //市
                "county": "污县2",  //县
                "content":"今天天气不错2",  //文件内容
                "esIndex":"megacorp",
                "esType": "employee",
                "esId": "AVkG2R0dSEc10Dr4Rt4y"
              }
            ]
          }
        });
    }, 500);
  },

  /**
   * 通过ID获取ES文件
   *参数1：pdId //文件编号
   *
   */
  'GET /admin/policydoc/{pdId}': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": {
          "pdId": "1",    //文件编号
          "pdName": "这是个名字a",  //文件名称
          "topicClassify": "11",  //主题分类
          "pubOrg": "666",  //颁布机构
          "placed": "好的", //具体单位
          "createDate": "2016-12-20", //成文日期
          "createYear": "2016",  //成文年份
          "issuedNum": "号号号",   //发文字号
          "link": "走你",  //原文链接
          "linkAddress": "www.zouni.com", //原文链接地址
          "isEffect": "effect", //是否有效
          "isPilot": "33",  //是否试点
          "isPub": "1",  //是否发布
          "isHide": "0",  //是否隐藏
          "province": "雾省2",   //省
          "city": "霾市2",  //市
          "county": "污县2",  //县
          "content":"今天天气不错2",  //文件内容
          "esIndex":"megacorp",
          "esType": "employee",
          "esId": "AVkG2R0dSEc10Dr4Rt4y"
        }
      });
    }, 500);
  },



  /**
   * 修改ES文件
   */
  'PUT /admin/policydoc': function (req, res) {
    setTimeout(function () {
      req.json({
        "pdId": "1",    //文件编号
        "pdName": "这是个名字a",  //文件名称
        "topicClassify": "11",  //主题分类
        "pubOrg": "666",  //颁布机构
        "placed": "好的", //具体单位
        "createDate": "2016-12-20", //成文日期
        "createYear": "2016",  //成文年份
        "issuedNum": "号号号",   //发文字号
        "link": "走你",  //原文链接
        "linkAddress": "www.zouni.com", //原文链接地址
        "isEffect": "effect", //是否有效
        "isPilot": "33",  //是否试点
        "isPub": "1",  //是否发布
        "isHide": "0",  //是否隐藏
        "province": "雾省2",   //省
        "city": "霾市2",  //市
        "county": "污县2",  //县
        "content":"今天天气不错2",  //文件内容
        "esIndex":"megacorp",
        "esType": "employee",
        "esId": "AVkG2R0dSEc10Dr4Rt4y"
      }),
        res.json({
          "status": "SUCCESS",
          "cause": null,
          "data": null
        });
    }, 500);
  },
}


