'use strict';

module.exports = {
  /*用户管理*/
  //用户列表
  //搜索条件map     key-->userId 用户Id,userName 用户名 ，phone 手机号
  'POST /admin/user/ls/offset/limit': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": {
          "total": 1,
          "rows": [
            {
              "userId": 1,
              "userName": "18511555555",
              "password": "123",
              "nickName": "王老吉",
              "optiVersion": null,
              "createDate": null,
              "lastTime": null,
              "lastIp": null,
              "lastSite": null,
              "realName": null,
              "sex": null,
              "birthday": null,
              "categoryParentId": null,
              "categoryId": null,
              "headPic": "/upload/userLogo/1/4cd23864-0ee4-437d-b454-dddb42b0f287.png",
              "phone": null,
              "tel": null,
              "email": null,
              "qq": null,
              "weixin": null,
              "province": null,
              "city": null,
              "payPwd": null,
              "certifyStatus": null,
              "certifyType": null,
              "expertUser": null,
              "money": null,
              "userStatus": null
            }
          ]
        }
      });
    }, 500);
  },
  //用户详情
  'GET /admin/user/g/userId': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": {
          "userId": 1,
          "userName": "18511555555",
          "password": "123",
          "nickName": "王老吉",
          "optiVersion": null,
          "createDate": null,
          "lastTime": null,
          "lastIp": null,
          "lastSite": null,
          "realName": null,
          "sex": null,
          "birthday": null,
          "categoryParentId": null,
          "categoryId": null,
          "headPic": "/upload/userLogo/1/4cd23864-0ee4-437d-b454-dddb42b0f287.png",
          "phone": null,
          "tel": null,
          "email": null,
          "qq": null,
          "weixin": null,
          "province": null,
          "city": null,
          "payPwd": null,
          "certifyStatus": null,
          "certifyType": null,
          "expertUser": null,
          "money": null,
          "userStatus": null
        }
      });
    }, 500);
  },
  //用户详情
  //参数User user ，userId
  'PUT /admin/user/u/userId': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": "SUCCESS"
      });
    }, 500);
  },


  //管理员管理
  //管理员添加  order adminname  offset limit
  'POST /admin/g/admins': function (req, res) {
    setTimeout(function () {
      res.json({
        success: true,
        data: "SUCCESS",
      });
    }, 500);
  },
  //管理员添加  AdminUser admin
  'POST /admin/a/createadmin': function (req, res) {
    setTimeout(function () {
      res.json({
        success: true,
        data: "SUCCESS",
      });
    }, 500);
  },
  //管理员删除  AdminUser admin
  'DELETE /admin/a/createadmin': function (req, res) {
    setTimeout(function () {
      res.json({
        success: true,
        data: "SUCCESS",
      });
    }, 500);
  },
};
