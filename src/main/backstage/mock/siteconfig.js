/**
 * Created by ligang on 2016/12/16.
 */
'use strict';

module.exports = {
/**
 * 站点配置
 */
  /**
   * 查询站点配置
   * 成功：["status":"success","data":{}]
   * 失败：[“error”:”错误原因”
   */
  'GET/admin/sc': function (req, res) {
    setTimeout(function () {
      res.json({
        status: success,
        data: {
          scID:"", //站点配置编号
          companyName:"", //公司名称
          telephone:"", //联系电话
          address:"", //联系地址
          icp:"", //网站备案号
          seoKeyword:"", // SEO关键字
          seoDescription:"", //SEO描述
          qrCode:"" //二维码图片地址
        },
      });
    }, 500);
  },

  /**
   * 新增站点配置
   * 参数1：companyName //公司名称
   * 参数2：telephone //联系电话
   * 参数3：address //联系地址
   * 参数4：icp //网站备案号
   * 参数5：seoKeyword // SEO关键字
   * 参数6：seoDescription //SEO描述
   * 参数7： qrCode //二维码图片地址
   * 成功：["status":"success"]
   * 失败：[“error”:”错误原因”]
   *
   */
  'POST /admin/sc/{scId}': function (req, res) {
    setTimeout(function () {
      req.json({
        companyName:"", //公司名称
        telephone:"", //联系电话
        address:"", //联系地址
        icp:"", //网站备案号
        seoKeyword:"", // SEO关键字
        seoDescription:"", //SEO描述
        qrCode:"" //二维码图片地址
      });
      res.json({
        status: success
      });
    }, 500);
  },
  /**
   * 修改站点配置
   * 参数1：scID //站点配置编号
   * 参数2：companyName //公司名称
   * 参数3：telephone //联系电话
   * 参数4：address //联系地址
   * 参数5：icp //网站备案号
   * 参数6：seoKeyword // SEO关键字
   * 参数7：seoDescription //SEO描述
   * 参数8： qrCode //二维码图片地址
   * 成功：["status":"success"]
   * 失败：[“error”:”错误原因”]
   */
  'PUT /admin/sc/{scId}': function (req, res) {
    setTimeout(function () {
      req.json({
        scID:"", //站点配置编号
        companyName:"", //公司名称
        telephone:"", //联系电话
        address:"", //联系地址
        icp:"", //网站备案号
        seoKeyword:"", // SEO关键字
        seoDescription:"", //SEO描述
        qrCode:"" //二维码图片地址
      });
      res.json({
        status: success
      });
    }, 500);
  },
  /**
   * 二维码图片上传
   * 参数1：qrCodeFile  二维码图片
   * 成功：["status":"success"，"data":filePath]
   * 失败：[“error”:”错误原因”]
   */
  'POST /admin/sc/upload': function (req, res) {
    setTimeout(function () {
      req.json({
        qrCodeFile:"", //二维码图片
      });
      res.json({
        status: success
      });
    }, 500);
  },

  /**
   * 遇到问题
   */
  /**
   * 获取问题列表
   * 参数1：input 查询关键字
   * 参数2：limit 每页条数
   * 参数3：offset 偏移量
   * 成功：系统消息["status":"success","data":{"rows":问题列表,"total":总数}]
   * 失败：[“error”:”错误原因”]
   */
  'GET /admin/pr': function (req, res) {
    setTimeout(function () {
      req.json({
        input:"",
        limit:"",
        offset:""
      }),
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": {
          "total": 1,
          "rows": [
            {
              "prId": 1, //问题编号
              "mainTitle": "",//主标题
              "subtitle": "",//小标题
              "remark": "", //摘要
              "content": "",//内容
              "createDate":"", //创建日期
              "dispStatus": "",//显示状态 0 否 1 是
            }
          ]
        }
      });
    }, 500);
  },
  /**
   * 获取指定 id 的问题记录
   *  参数1：prId: "",//问题ID
   */
  'GET /admin/pr/{prId}': function (req, res) {
    setTimeout(function () {
        res.json({
          "status": "SUCCESS",
          "cause": null,
          "data": {
                "prId": 1, //问题编号
                "mainTitle": "",//主标题
                "subtitle": "",//小标题
                "remark": "", //摘要
                "content": "",//内容
                "createDate":"", //创建日期
                "dispStatus": ""//显示状态 0 否 1 是
          }
        });
    }, 500);
  },
  /**
   * 新增问题
   * 参数1：mainTitle: "",//主标题
   * 参数2：subtitle: "",//小标题
   * 参数3：remark: "", //摘要
   * 参数4：content: "",//内容
   */
  'POST /admin/pr': function (req, res) {
    setTimeout(function () {
      req.json({
        "mainTitle": "",//主标题
        "subtitle": "",//小标题
        "remark": "", //摘要
        "content": "",//内容
      }),
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": null
      });
    }, 500);
  },
  /**
   * 问题修改
   * 参数1：prId: "",//问题ID
   */
  'PUT /admin/pr/{prId}': function (req, res) {
    setTimeout(function () {
      req.json({
        "prId": 1, //问题编号
        "mainTitle": "",//主标题
        "subtitle": "",//小标题
        "remark": "", //摘要
        "content": "",//内容
      }),
        res.json({
          "status": "SUCCESS",
          "cause": null,
          "data": null
        });
    }, 500);
  },
  /**
   * 问题删除
   * 参数1：prId: "",//问题ID
   */
  'DELETE /admin/pr/{prId}': function (req, res) {
    setTimeout(function () {
      req.json({
        "prId": 1, //问题编号
      }),
        res.json({
          "status": "SUCCESS",
          "cause": null,
          "data": null
        });
    }, 500);
  },

  /**
   * 关于我们
   */

  /**
   * 获取关于我们
   */
  'GET /admin/au': function (req, res) {
    setTimeout(function () {
        res.json({
          "status": "SUCCESS",
          "cause": null,
          "data": {
            "total": 1,
            "rows": [
              {
                "sortId": "", //排序
                "title": "",//标题
                "content": ""//内容
              }
            ]
          }
        });
    }, 500);
  },

  /**
   * 新增关于我们
   * 参数1：sortId: "",//排序
   * 参数2：title: "",//标题
   * 参数3：content: "", //内容
   */
  'POST /admin/au': function (req, res) {
    setTimeout(function () {
      req.json({
        "sortId": "", //排序
        "title": "",//标题
        "content": ""//内容
      }),
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": null
      });
    }, 500);
  },

  /**
   * 修改关于我们
   * 参数1：sortId: "",//排序
   * 参数2：title: "",//标题
   * 参数3：content: "", //内容
   */
  'PUT /admin/au': function (req, res) {
    setTimeout(function () {
      req.json({
        "sortId": "", //排序
        "title": "",//标题
        "content": ""//内容
      }),
        res.json({
          "status": "SUCCESS",
          "cause": null,
          "data": null
        });
    }, 500);
  },
  /**
   * 删除关于我们
   * 参数1：sortId: "",//排序
   * 参数2：title: "",//标题
   * 参数3：content: "", //内容
   */
  'DELETE /admin/au/{sortId}': function (req, res) {
    setTimeout(function () {
      req.json({
        "sortId": "" //排序
      }),
        res.json({
          "status": "SUCCESS",
          "cause": null,
          "data": null
        });
    }, 500);
  },
};
