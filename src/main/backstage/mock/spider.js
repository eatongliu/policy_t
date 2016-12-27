'use strict';

module.exports = {
  /*消息*/
  /**
   * 第一步 生成爬虫标识
   * URI  /sp/ch
   * POST  说明：将任务名和按照相应规则生成的spiderid存入cache中，并把返回的spiderid显示在“任务标识”栏。
   * 参数1：taskname={任务名}（必填）
   * 成功：{"status": "SUCCESS","data": {"spider": "爬虫标识"} }
   * 失败：{"status": "ERROR","cause": "错误原因" }
   * 如果spiderid为空则新增，否则覆盖原对象
   *
   * SpiderBaseInfo spider
   */
  'POST /sp/ch': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS", "data": {"spider": "爬虫标识"}
      });
    }, 500);
  },
  /**
   * 获取爬虫标识
   * URI  /sp/ch
   * POST  说明：将任务名和按照相应规则生成的spiderid存入cache中，并把返回的spiderid显示在“任务标识”栏。
   * 参数1：taskname={任务名}（必填）
   * 成功：{"status": "SUCCESS","data": {"spider": "爬虫标识"} }
   * 失败：{"status": "ERROR","cause": "错误原因" }
   */
  'GET /sp/ch/spiderid': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "data": {"spider": "爬虫标识"}
      });
    }, 500);
  },
  /**
   * 获取爬虫备注和深度
   * URI  /sp/ch
   * POST  说明：将任务名和按照相应规则生成的spiderid存入cache中，并把返回的spiderid显示在“任务标识”栏。
   * 参数1：taskname={任务名}（必填）
   * 成功：{"status": "SUCCESS","data": {"spider": "爬虫标识"} }
   * 失败：{"status": "ERROR","cause": "错误原因" }
   * spiderid  SpiderBaseInfo spider
   */
  'PUT /sp/ch/spiderid': function (req, res) {
    setTimeout(function () {
      res.json({
        success: true,
        data: "SUCCESS",
      });
    }, 500);
  },
  /**
   * 4.3.2 爬取范围信息
   * 文件linux根路径：/nutch/config/{爬虫标识}/crawlscope.json
   * URI  /sp/cs
   * POST  说明：将“采集来源”中的所有内容拼接成的json内容保存到/nutch/config/{爬虫标识}/crawlscope.json文件中，并将该文件按照路径全部上传至HDFS.
   * 参数1：spiderid={爬虫标识}（必填）
   * 参数2：由“采集来源”中的所有内容拼接成的json串（必填）【具体格式见备注】
   * 成功： { "status": "SUCCESS", "data": { "spider": "爬虫标识" } }
   * 失败： { "status": "ERROR",  "cause": "错误原因"  }
   */
  'POST /sp/cs/spiderid': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "data": {"spider": "爬虫标识"}
      });
    }, 500);
  },
  /**
   * 4.3.4 解析页面
   * 文件：/nutch/config/{爬虫标识}/parse.json
   * URI  /sp/ps
   * POST  说明：将“采集维度”中的所有内容拼接成的json内容保存到/nutch/config/{爬虫标识}/parse.json文件中
   * 参数1：spiderid={爬虫标识}（必填）
   * 参数2：由“采集维度”中的所有内容拼接成的json串（必填）【具体格式见备注】
   * 成功：{"status": "SUCCESS","data": {"spider": "爬虫标识"}}
   * 失败：{"status": "ERROR","cause": "错误原因"}
   * <p>
   * 后来:
   * <p>
   * 不需要存本地, 直接存 Redis (2016-11-23)
   * <p>
   * 将爬虫配置文件 parse.json (json 格式) 写入到 Redis 中
   * <p>
   * redis 中的 KEY : spiderid_[spiderId]
   * 例如: spiderd_xinlangboke_1479880143107
   * <p>
   * redis 中的 VALUE : JSON 串
   */
  'POST /sp/ps/spiderid': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS", "data": {"spider": "爬虫标识"}
      });
    }, 500);
  },
  /**
   * 第一步  将文件写入hadoop
   * 第二部  将任务生成zip并上传
   * 第三步  前两步成功后把cache中的数据存入数据库
   *
   * param spiderid
   * param parsejson
   * param request
   * return
   */
  'POST /sp/zip/spiderid': function (req, res) {
    setTimeout(function () {
      res.json({
        "status": "SUCCESS",
        "data": "爬虫配置成功"
      });
    }, 500);
  },
};
