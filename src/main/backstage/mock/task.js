'use strict';

module.exports = {
  /*任务*/
//拼音转换  参数 word
  //result
  'GET /pinyin': function (req, res) {
    setTimeout(function () {
      res.json({
        success: true,
        data: "pinyin",
      });
    }, 500);
  },
  //kettle任务
  //TaskKettle taskKettle
  'POST /tk/ktr': function (req, res) {
    setTimeout(function () {
      res.json({
        status: success,
        data: {kettleId:"",upload:""},
      });
    }, 500);
  },
  //kettle任务
  /**
   * 文件上传并解压到指定目录
   * data.put("kettleName", kettleName);
   *data.put("filePath", rootPath + realpath);
   */
  'POST /tk/ktr/upload': function (req, res) {
    setTimeout(function () {
      res.json({
        status: "SUCCESS",
        data: {kettleName:"",filePath:""},
      });
    }, 500);
  },
  /*任务模板*/
  /**
   * 第一步，显示模板
   * URI	/tk/tp/{tid}
   * Method	PUT
   * 功能说明：读取模板信息
   * 参数1：templateid模板id（必填）
   * 成功："rows":TaskTemplate
   * 失败：[“error”:”错误原因”]
   */
  'GET /tk/tp/tid': function (req, res) {
    setTimeout(function () {
      res.json({
        success: true,
        data: {"rows":TaskTemplate},
      });
    }, 500);
  },
  /**
   * 第一步，新增模板
   * URI	/tk/tp
   * Method	POST
   * 功能说明：在模板基本信息表中新增记录。
   * 参数1：caption标题 英文，作为文件名使用（必填）
   * 参数2：remark说明
   * 成功：[“templateid”:新增模板的templateid]
   * 失败：[“error”:”错误原因”]
   * 参数 TaskTemplate taskTemplate
   * 返回("templateid", tid);
   */
  'POST /tk/tp': function (req, res) {
    setTimeout(function () {
      res.json({
        success: true,
        data: {"templateid":"新增模板的templateid"},
      });
    }, 500);
  },
  /**
   * 修改模板
   * @PathVariable Integer templateId,
   * @RequestBody TaskTemplate taskTemplate,
   */
  'PUT /tk/tp/templateId': function (req, res) {
    setTimeout(function () {
      res.json({
        success: true,
        data: {"templateid":"templateId"},
      });
    }, 500);
  },
  /**
   * 第二步，新增任务
   * 4.2.2新增
   * URI	/tk/jo
   * Method	POST application/json
   * 功能说明：在任务基本信息表中新增记录。
   * 参数1：templateid模板ID（必填）
   * 参数2：taskname     标题 英文，作为文件名使用（必填）
   * 参数3：retries       自动尝试失败的次数 默认1次
   * 参数4：retrybackoff  每次重试尝试之间的毫秒时间，默认无
   * 参数5：type         命令类型如 command （必填）
   * 参数6：command    根据上边选择的类型编写具体的命令 （必填）
   * 参数7：dependencies  第一个job不可选依赖关系，第二个job可以依赖第一个
   * 成功：[“id”:新增任务的id]
   * 失败：[“error”:”错误原因”]
   */
  'POST /tk/jo': function (req, res) {
    setTimeout(function () {
      res.json({
        status: "SUCCESS",
        data: "id",
      });
    }, 500);
  },
  /**
   * 修改任务
   *application/json
   * @param TaskJob taskJob
   * @return
   */
  'PUT /tk/jo': function (req, res) {
    setTimeout(function () {
      res.json({
        success: true,
        data: {"createZip":{data:success},"jobId":jobid},
      });
    }, 500);
  },
  /**
   * 第二步 任务列表
   */
  'GET /tk/jo/templateid': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": {
          "total": 1,
          "rows": [
            {
              "taskId": 1,
              "taskName": null,
              "retries": null,
              "retryBackoff": null,
              "type": null,
              "command": "sss",
              "dependencies": null,
              "templateId": 1,
              "remark": null
            }
          ]
        }
      });
    }, 500);
  },
  /**
   * 第二步，显示任务
   * URI	/tk/jo/{templateid}/{tid}
   * Method	GET
   * 功能说明：显示任务信息
   * 参数1：templateid    任务模板ID（必填）
   * 参数2：tid  任务ID（必填）
   * 成功：”success”
   * 失败：[“error”:”错误原因”]
   */
  'GET /tk/jo/templateid/tid': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": {
          "total": 1,
          "rows": [
            {
              "taskId": 1,
              "taskName": null,
              "retries": null,
              "retryBackoff": null,
              "type": null,
              "command": "sss",
              "dependencies": null,
              "templateId": 1,
              "remark": null
            }
          ],
          "job": {
            "taskId": 1,
            "taskName": null,
            "retries": null,
            "retryBackoff": null,
            "type": null,
            "command": "sss",
            "dependencies": null,
            "templateId": 1,
            "remark": null
          }
        }
      });
    }, 500);
  },
  /**
   * 显示列表
   * URI	/tk/tp?limit=10&offset=0&caption=&temptype=1
   * Method	GET
   * 功能	说明：检索模板信息
   * 参数1：limit 每页条数
   * 参数2：offset 偏移量
   * 参数3：caption 标题
   * 参数4：temptype 模板类型（0添加    1上传）
   * 成功：检索模板信息列表，caption模糊匹配
   * 失败：[“error”:”错误原因”]
   */
  'GET /tk/tp': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "cause": null,
        "data": {
          "total": 1,
          "rows": [
            {
              "templateId": 1,
              "caption": "1",
              "creator": null,
              "createDatetime": null,
              "reviseDatetime": null,
              "tempType": 1,
              "tempPath": null,
              "remark": null
            }
          ]
        }
      });
    }, 500);
  },
  /**
   * 第三步，完成并生成zip并保存文件路径
   * URI	/tk/zip/{templateid}
   * Method	GET
   * 功能说明：显示任务信息
   * 参数1：templateid    任务模板ID（必填）
   * 成功：”success”
   * 失败：[“error”:”错误原因”]
   */
  'GET /tk/zip/templateid': function (req, res) {
    setTimeout(function () {
      res.json({
        status: success,
        data: success,
      });
    }, 500);
  },
  /**
   * 任务模板zip上传
   * 文件上传
   */
  'GET /tk/zip/upload': function (req, res) {
    setTimeout(function () {
      res.json({
        status: "SUCCESS",
        data: {templateId:"templateId"},
      });
    }, 500);
  },
};
