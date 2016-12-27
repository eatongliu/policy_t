'use strict';

module.exports = {
  /*消息*/
//answer 消息内容
  //参数
//{
// "answer": null,
//}
  'POST /admin/msg/a': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": "SUCCESS"
      });
    }, 500);
  },
  //消息列表
  //{"uid":"","id":"","keyword":"","isShow":"","qType":""}
  //作为搜索用    key--> uid 用户id ,id   消息id,keyword  问题关键字,isShow  是否显示,qType   消息类型
  'POST /admin/msg/ls/offset/limit': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": {
          "total": 1,
          "rows": [
            {
              "id": 1,
              "userId": null,
              "userName": "王老吉",
              "userQuestion": "今天吃啥",
              "qTime": 1481342313000,
              "answerMan": null,
              "answer": null,
              "aTime": null,
              "isAnswer": null,
              "isShow": null,
              "isRead": null,
              "qType": null,
              "remark": null,
              "qStatus": null,
              "isPoint": null,
              "isResolve": null
            }
          ]
        }
      });
    }, 500);
  },

/**
 * 系统消息
 * */

  /**条件查询
   * 参数1：limit 每页条数
   * 参数2：offset 偏移量
   * 参数3：startDate 开始时间 ,
   * 参数4：endDate   结束时间,
   * 参数5：keyword  消息关键字,
   * 参数6：qType  消息类型,
   * 参数7：qStatus   消息状态
   * 成功：系统消息["status":"success","data":{"rows":消息列表,"total":总数}]
   * 失败：[“error”:”错误原因”]
   */
  //{"startDate":"","endDate":"","keyword":"","qType":"","qStatus":"","limit":"","offset":""}

  'GET /admin/msg': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": {
          "total": 1,
          "rows": [
            {
              "id": 1, //消息id
              "userId": "",//用户ID
              "userQuestion": "今天吃啥",//内容
              "qTime": 1481342313000, //时间
              "qType": "",//消息类型
              "remark": "",//备注
              "qStatus": "",//消息状态 0 待查阅； 1 已查看；2 待处理；3 已备注；4已处理
              "isPoint": "",//是否重点 0 否 1 是
              "isResolve": "",//是否解决 0 否 1 是
              "...": "..."
            }
          ]
        }
      });
    }, 500);
  },
  /**
   * 修改消息是否为重点
   * 参数1： id 消息ID,
   * 参数2：isPoint 是否重点 0 否 1 是
   * 成功：系统消息["status":"success"]
   * 失败：[“error”:”错误原因”]
   */
  //{"id":"","isPoint":""}
  'PUT /admin/msg/ispoint/{id}': function (req, res) {
    setTimeout(function () {
      req.json({
        "isPoint":"" //是否重点 0 否 1 是
      });
      res.json({
        "status": "SUCCESS"
      });
    }, 500);
  },
  /**
   * 修改消息是否为已解决
   * 参数1： id 消息ID,
   * 参数2：isResolve 是否已解决 0 否 1 是
   * 成功：系统消息["status":"success"]
   * 失败：[“error”:”错误原因”]
   */
  //{"id":"","isPoint":""}
  'PUT /admin/msg/isresolve/{id}': function (req, res) {
    setTimeout(function () {
      req.json({
        "isResolve":"" //是否已解决 0 否 1 是
      });
      res.json({
        "status": "SUCCESS"
      });
    }, 500);
  },
  /**
   * 修改消息状态备注
   * 参数1： id 消息ID,
   * 参数2：qStatus 消息状态 0 待查阅； 1 已查看；2 待处理；3 已备注；4已处理
   * 参数3：remark 备注
   * 成功：系统消息["status":"success"]
   * 失败：[“error”:”错误原因”]
   */
  //{"id":"","isPoint":""}
  'PUT /admin/msg/status/{id}': function (req, res) {
    setTimeout(function () {
      req.json({
        "qStatus":"", //消息状态 0 待查阅； 1 已查看；2 待处理；3 已备注；4已处理
        "remark":"" //备注
      });
      res.json({
        "status": "SUCCESS"
      });
    }, 500);
  },

};
