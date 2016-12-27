'use strict';

module.exports = {
  /*订阅*/
  //订阅列表
  'GET /admin/su/ls/offset/limit': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": {
          "total": 1,
          "rows": [
            {
              "sid": 2,
              "createDate": null,
              "reviseDate": null,
              "keyWords": "皇马",
              "uRemark": null,
              "uContext": null,
              "status": "1",
              "uid": 1,
              "mobile": "0",
              "mRemark": null
            }
          ]
        }
      });
    }, 500);
  },
  //查询详情订阅
  'GET /admin/su/s/sid': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": {
        "data": {
          "sid": 2,
            "createDate": null,
            "reviseDate": null,
            "keyWords": "皇马",
            "uRemark": null,
            "uContext": null,
            "status": "1",
            "uid": 1,
            "mobile": "0",
            "mRemark": null
        }
      }

      });
    }, 500);
  },
  //修改详情订阅
  'PUT /admin/su/u/sid': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "data": "爬虫配置成功"
      });
    }, 500);
  },
  //订阅手动推送
  'PUT /admin/su/send/sid': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "data": "爬虫配置成功"
      });
    }, 500);
  },
  //获取文章详情
  'PUT /list/q/pdid': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": {
          "total": 7,
          "rows": [
            {
              "placed": "上海市人民政府",
              "city": "上海",
              "createYear": 2016,
              "county": "",
              "isEffect": 1,
              "link": "上海市行政规范性文件制定和备案规定",
              "isPilot": 0,
              "linkAddress": "http://www.shanghai.gov.cn/nw2/nw2314/nw3124/u6aw2289.html",
              "content": " 　　第一章总则\n\n　　第一条（目的和依据）\n\n　　同时废止。",
              "isPub": 0,
              "isHide": 1,
              "issuedNum": "2016年10月9日上海市人民政府令第46号公布",
              "province": "上海",
              "topicClassify": "",
              "pubOrg": "上海市人民政府",
              "pdId": 1,
              "pdName": "上海市行政规范性文件制定和备案规定",
              "createDate": "2016-12-16T16:56:47.858"
            }
          ]
        }
      });
    }, 500);
  },
};
