//引入css
require('bootstrap/dist/css/bootstrap.css')
require("../../css/common/global.css");
require("../../css/common/grid.css");
require("../../css/page/index.css");
import {Button,Carousel} from 'react-bootstrap';
require('echarts/map/js/china.js');
import ReactEcharts from 'echarts-for-react';
var API_BATHPATH=require('../config').default;
var React = require('react');
var ReactDOM = require('react-dom');
import Header from '../components/header/index.js'
import Footer from '../components/footer/footer.js'
ReactDOM.render(
    <Header/>, document.getElementsByClassName('g-hd')[0]);

ReactDOM.render(
    <Footer/>, document.getElementsByClassName('g-ft')[0]);
var BodyMain = React.createClass({
  getInitialState(){
    return ({
      zcfg:true,
      zcwj:true,
      zcjd:true,
      jyta:true
    })
  },
  handleChecked(e){
    console.log(e.target.checked)
    var state={};
    state[e.target.value]=e.target.checked,
    this.setState(state)
  },
    render() {
        return (
            <div>
                <div className='search'>
                    <div className='container search_bg'>
                        <div className='search_logo'>全面的政策信息查询平台</div>
                        <div className="row mg_top">
                          <form method = "get" action = "search.html">
                            <div className="col-md-8 col-md-offset-2 search_btn">
                                <label style={{border:this.state.zcfg?'1px solid rgba(255,255,255,.6)':'none'}}><input onChange={this.handleChecked} checked={this.state.zcfg} type='checkbox' name='index' value='zcfg'/>政策法规</label>
                                <label style={{border:this.state.zcwj?'1px solid rgba(255,255,255,.6)':'none'}}><input onChange={this.handleChecked} checked={this.state.zcwj} type='checkbox' name='index' value='zcwj'/>政策文件</label>
                                <label style={{border:this.state.zcjd?'1px solid rgba(255,255,255,.6)':'none'}}><input onChange={this.handleChecked} checked={this.state.zcjd} type='checkbox' name='index' value='zcjd'/>政策解读</label>
                                <label style={{border:this.state.jyta?'1px solid rgba(255,255,255,.6)':'none'}}><input onChange={this.handleChecked} checked={this.state.jyta} type='checkbox' name='index' value='jyta'/>建议提案</label>
                            </div>
                            <div className="col-md-8 col-md-offset-2">
                                    <div className="form-group">
                                        <input required type="text" name='str' className="form-control search_ip" placeholder="请输入搜索内容..."/>
                                        <Button type='submit' className='search_btns'>搜 索</Button>
                                    </div>
                            </div>
                            <div className="col-md-8 col-md-offset-2 hot_word">
                                <div className="pull-left left">搜索热词：</div>
                                <div className="pull-left right">
                                    <a href='#'>2016年了房子还不降价</a>
                                    <a href='#'>2016年了我还没吃饭</a>
                                    <a href='#'>你在哪呢嘿嘿嘿嘿嘿</a>
                                    <a href='#'>呜呼阿黑颜</a>
                                    <a href='#'>2016年了房子还不降价</a>
                                </div>
                            </div>
                          </form>
                        </div>
                    </div>
                </div>
                <div className='blank'>
                    <div className='search_link'>
                        <div className="col-lg-offset-2 col-lg-2 col-md-6 col-xs-12">
                            <dl>
                                <dt><img src={require("../../img/icon_zcfg.png")}/></dt>
                                <dd>
                                    <h4>政策法规</h4>
                                    <p>搜集全网<br/>政策法规最新信息</p>
                                </dd>
                            </dl>
                        </div>
                        <div className="col-lg-2 col-md-6 col-xs-12">
                            <dl>
                                <dt><img src={require("../../img/icon_zcwj.png")}/></dt>
                                <dd>
                                    <h4>政策文件</h4>
                                    <p>全网政策文件<br/>最便捷的搜索方式</p>
                                </dd>
                            </dl>
                        </div>
                        <div className="col-lg-2 col-md-6 col-xs-12">
                            <dl>
                                <dt><img src={require("../../img/icon_zcjd.png")}/></dt>
                                <dd>
                                    <h4>政策解读</h4>
                                    <p>数览政策<br/>政策文件的解读信息</p>
                                </dd>
                            </dl>
                        </div>
                        <div className="col-lg-2 col-md-6 col-xs-12">
                            <dl>
                                <dt><img src={require("../../img/icon_jyta.png")}/></dt>
                                <dd>
                                    <h4>建议提案</h4>
                                    <p>采集各界声音<br/>搜集最新热点建议提案</p>
                                </dd>
                            </dl>
                        </div>
                    </div>
                </div>
                <div className='martop'>
                        <Carousel controls={false} interval={0}>
                            <Carousel.Item>
                                <Echart1/>
                            </Carousel.Item>
                            <Carousel.Item>
                            <div className='bgk'>
                                <div className='col-lg-5'>
                                    <div className='col-lg-offset-2 col-lg-10 t-r'>
                                        <h3>更全政策 更广分析</h3>
                                        <p>更全面的政策信息，满足更广泛的政策搜索需求<br/>更直观的分析方式，大数据可视化自由选择你需要的图表和组件</p>
                                        <p></p>
                                    </div>
                                </div>
                                <div className='col-lg-7'><Echart2/></div>
                            </div>
                            </Carousel.Item>
                            <Carousel.Item>
                                <div className='row'>
                                    <div className='col-lg-7'><Echart3/></div>
                                    <div className='col-lg-5'>
                                        <div className='col-lg-12 t-l'>
                                            <h3>更全政策 更广分析</h3>
                                            <p>更全面的政策信息，满足更广泛的政策搜索需求<br/>更直观的分析方式，大数据可视化自由选择你需要的图表和组件</p>
                                        </div>
                                    </div>
                                </div>
                            </Carousel.Item>
                        </Carousel>
                </div>
                <div className='container'>
                  <div className='row'>
                    <div style={{paddingTop:'100px'}} className='col-lg-offset-3 col-lg-6'>
                        <p className='fontSize'>政策通V1.0的这一切仅仅是一个全新的开始在探索和完善的道路上我们从未停止脚步</p>
                    </div>
                    <div className='col-lg-12 h3'>
                        <div className='col-lg-6'>
                            <h3>猜你喜欢</h3>
                        </div>
                        <div className='col-lg-6'>
                            <h3>相关政策</h3>
                        </div>
                    </div>
                    <div className='col-lg-12'>
                        <div className='col-lg-6'>
                            <div className='col-lg-3'>
                            [政策解读]
                            </div>
                            <div className='col-lg-6'>
                            北京市税收征收保障办法
                            </div>
                            <div className='col-lg-3'>
                            2016-08-11
                            </div>
                        </div>
                        <div className='col-lg-6'>
                            <div className='col-lg-3'>
                            [政策解读]
                            </div>
                            <div className='col-lg-6'>
                            北京市税收征收保障办法
                            </div>
                            <div className='col-lg-3'>
                            2016-08-11
                            </div>
                        </div>
                    </div>
                  </div>
                </div>
            </div>
        )
    }
})
var Echart1 = React.createClass({
    getInitialState:function(){
        return{
            data: {}
        }
    },
    componentDidMount: function() {
       $.ajax({
           url: API_BATHPATH+'h/p1',
           dataType: 'json',
           success: function(data){
            var data=data.data.map(function(one,index){
                return {
                    name:one.province,
                    value:one.count
                }
            })
             this.setState({data: data});
           }.bind(this)
         });
      },
    getOption() {
        var data = this.state.data
        var geoCoordMap = {
            '海门':[121.15,31.89],
            '鄂尔多斯':[109.781327,39.608266],
            '招远':[120.38,37.35],
            '舟山':[122.207216,29.985295],
            '齐齐哈尔':[123.97,47.33],
            '盐城':[120.13,33.38],
            '赤峰':[118.87,42.28],
            '青岛':[120.33,36.07],
            '乳山':[121.52,36.89],
            '金昌':[102.188043,38.520089],
            '泉州':[118.58,24.93],
            '莱西':[120.53,36.86],
            '日照':[119.46,35.42],
            '胶南':[119.97,35.88],
            '南通':[121.05,32.08],
            '拉萨':[91.11,29.97],
            '云浮':[112.02,22.93],
            '梅州':[116.1,24.55],
            '文登':[122.05,37.2],
            '上海':[121.48,31.22],
            '攀枝花':[101.718637,26.582347],
            '威海':[122.1,37.5],
            '承德':[117.93,40.97],
            '厦门':[118.1,24.46],
            '汕尾':[115.375279,22.786211],
            '潮州':[116.63,23.68],
            '丹东':[124.37,40.13],
            '太仓':[121.1,31.45],
            '曲靖':[103.79,25.51],
            '烟台':[121.39,37.52],
            '福州':[119.3,26.08],
            '瓦房店':[121.979603,39.627114],
            '即墨':[120.45,36.38],
            '抚顺':[123.97,41.97],
            '玉溪':[102.52,24.35],
            '张家口':[114.87,40.82],
            '阳泉':[113.57,37.85],
            '莱州':[119.942327,37.177017],
            '湖州':[120.1,30.86],
            '汕头':[116.69,23.39],
            '昆山':[120.95,31.39],
            '宁波':[121.56,29.86],
            '湛江':[110.359377,21.270708],
            '揭阳':[116.35,23.55],
            '荣成':[122.41,37.16],
            '连云港':[119.16,34.59],
            '葫芦岛':[120.836932,40.711052],
            '常熟':[120.74,31.64],
            '东莞':[113.75,23.04],
            '河源':[114.68,23.73],
            '淮安':[119.15,33.5],
            '泰州':[119.9,32.49],
            '南宁':[108.33,22.84],
            '营口':[122.18,40.65],
            '惠州':[114.4,23.09],
            '江阴':[120.26,31.91],
            '蓬莱':[120.75,37.8],
            '韶关':[113.62,24.84],
            '嘉峪关':[98.289152,39.77313],
            '广州':[113.23,23.16],
            '延安':[109.47,36.6],
            '太原':[112.53,37.87],
            '清远':[113.01,23.7],
            '中山':[113.38,22.52],
            '昆明':[102.73,25.04],
            '寿光':[118.73,36.86],
            '盘锦':[122.070714,41.119997],
            '长治':[113.08,36.18],
            '深圳':[114.07,22.62],
            '珠海':[113.52,22.3],
            '宿迁':[118.3,33.96],
            '咸阳':[108.72,34.36],
            '铜川':[109.11,35.09],
            '平度':[119.97,36.77],
            '佛山':[113.11,23.05],
            '海口':[110.35,20.02],
            '江门':[113.06,22.61],
            '章丘':[117.53,36.72],
            '肇庆':[112.44,23.05],
            '大连':[121.62,38.92],
            '临汾':[111.5,36.08],
            '吴江':[120.63,31.16],
            '石嘴山':[106.39,39.04],
            '沈阳':[123.38,41.8],
            '苏州':[120.62,31.32],
            '茂名':[110.88,21.68],
            '嘉兴':[120.76,30.77],
            '长春':[125.35,43.88],
            '胶州':[120.03336,36.264622],
            '银川':[106.27,38.47],
            '张家港':[120.555821,31.875428],
            '三门峡':[111.19,34.76],
            '锦州':[121.15,41.13],
            '江西':[115.89,28.68],
            '柳州':[109.4,24.33],
            '三亚':[109.511909,18.252847],
            '自贡':[104.778442,29.33903],
            '吉林':[126.57,43.87],
            '阳江':[111.95,21.85],
            '泸州':[105.39,28.91],
            '西宁':[101.74,36.56],
            '宜宾':[104.56,29.77],
            '呼和浩特':[111.65,40.82],
            '成都':[104.06,30.67],
            '大同':[113.3,40.12],
            '镇江':[119.44,32.2],
            '桂林':[110.28,25.29],
            '张家界':[110.479191,29.117096],
            '宜兴':[119.82,31.36],
            '北海':[109.12,21.49],
            '西安':[108.95,34.27],
            '金坛':[119.56,31.74],
            '东营':[118.49,37.46],
            '牡丹江':[129.58,44.6],
            '遵义':[106.9,27.7],
            '绍兴':[120.58,30.01],
            '扬州':[119.42,32.39],
            '常州':[119.95,31.79],
            '潍坊':[119.1,36.62],
            '重庆':[106.54,29.59],
            '台州':[121.420757,28.656386],
            '南京':[118.78,32.04],
            '滨州':[118.03,37.36],
            '贵州':[106.71,26.57],
            '无锡':[120.29,31.59],
            '本溪':[123.73,41.3],
            '克拉玛依':[84.77,45.59],
            '渭南':[109.5,34.52],
            '马鞍山':[118.48,31.56],
            '宝鸡':[107.15,34.38],
            '焦作':[113.21,35.24],
            '句容':[119.16,31.95],
            '北京':[116.46,39.92],
            '徐州':[117.2,34.26],
            '衡水':[115.72,37.72],
            '包头':[110,40.58],
            '绵阳':[104.73,31.48],
            '乌鲁木齐':[87.68,43.77],
            '枣庄':[117.57,34.86],
            '杭州':[120.19,30.26],
            '淄博':[118.05,36.78],
            '鞍山':[122.85,41.12],
            '溧阳':[119.48,31.43],
            '库尔勒':[86.06,41.68],
            '安阳':[114.35,36.1],
            '开封':[114.35,34.79],
            '济南':[117,36.65],
            '德阳':[104.37,31.13],
            '温州':[120.65,28.01],
            '九江':[115.97,29.71],
            '邯郸':[114.47,36.6],
            '临安':[119.72,30.23],
            '甘肃':[103.73,36.03],
            '沧州':[116.83,38.33],
            '临沂':[118.35,35.05],
            '南充':[106.110698,30.837793],
            '天津':[117.2,39.13],
            '富阳':[119.95,30.07],
            '泰安':[117.13,36.18],
            '诸暨':[120.23,29.71],
            '郑州':[113.65,34.76],
            '哈尔滨':[126.63,45.75],
            '聊城':[115.97,36.45],
            '芜湖':[118.38,31.33],
            '唐山':[118.02,39.63],
            '平顶山':[113.29,33.75],
            '邢台':[114.48,37.05],
            '德州':[116.29,37.45],
            '济宁':[116.59,35.38],
            '荆州':[112.239741,30.335165],
            '宜昌':[111.3,30.7],
            '义乌':[120.06,29.32],
            '丽水':[119.92,28.45],
            '洛阳':[112.44,34.7],
            '秦皇岛':[119.57,39.95],
            '株洲':[113.16,27.83],
            '石家庄':[114.48,38.03],
            '莱芜':[117.67,36.19],
            '常德':[111.69,29.05],
            '保定':[115.48,38.85],
            '湘潭':[112.91,27.87],
            '金华':[119.64,29.12],
            '岳阳':[113.09,29.37],
            '长沙':[113,28.21],
            '衢州':[118.88,28.97],
            '廊坊':[116.7,39.53],
            '菏泽':[115.480656,35.23375],
            '安徽':[117.27,31.86],
            '武汉':[114.31,30.52],
            '大庆':[125.03,46.58]
        };
        var convertData = function(data) {
            var res = [];
            for (var i = 0; i < data.length; i++) {
                var geoCoord = geoCoordMap[data[i].name];
                if (geoCoord) {
                    res.push(geoCoord.concat(data[i].value));
                }
            }
            return res;
        };
        var option = {
            title: {
                text: '2016年政策数据公开密集趋势',
                subtext: 'data from PM25.in',
                link: './chart.html',
                left: 'center',
                target:'self',
                textStyle: {
                    color: '#fff'
                }
            },
            backgroundColor: '#404a59',
            visualMap: {
                min: 0,
                max: 500,
                splitNumber: 5,
                inRange: {
                    color: ['#d94e5d', '#eac736', '#50a3ba'].reverse()
                },
                textStyle: {
                    color: '#fff'
                }
            },
            geo: {
                map: 'china',
                label: {
                    emphasis: {
                        show: false
                    }
                },
                roam: true,
                itemStyle: {
                    normal: {
                        areaColor: '#323c48',
                        borderColor: '#111'
                    },
                    emphasis: {
                        areaColor: '#2a333d'
                    }
                }
            },
            series: [
                {
                    name: 'AQI',
                    type: 'heatmap',
                    coordinateSystem: 'geo',
                    data: convertData(data)
                }
            ]
        };
        return option
    },
    onChartReady: function(chart) {
        chart.hideLoading();
    },
    render() {
        return (
            <div className='' style={{
                height: '500px',
                width: $(window).width()
            }}>
                <ReactEcharts option={this.getOption()} onChartReady={this.onChartReady} showLoading={false} style={{
                    height: '500px',
                    width: $(window).width()
                }}/>
            </div>
        )
    }
})
var Echart2 = React.createClass({
    getInitialState:function(){
        return{
            data1:[],
            data2:[],
            data3:[],
            data4:[],
            data5:[]
        }
    },
    componentDidMount:function(){
        $.ajax({
            url:API_BATHPATH+'h/p2',
            dataType:'json',
            success:function(data){
                var data1=data.data.map(function(two,index){
                    return two.type
                });
                var data2=data.data.map(function(two,index){
                    return two.pcount
                });
                var data3=data.data.map(function(two,index){
                    return two.pname
                });
                var data4=data.data.map(function(two,index){
                    return two.ncount
                });
                var data5=data.data.map(function(two,index){
                    return two.nname
                });
              this.setState({data1: data1,data2:data2,data3:data3,data4:data4,data5:data5});
            }.bind(this)
        });
    },
    getOption() {
        var option = {
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis:  {
                    type: 'value'
                },
                color:['#c23531','#2f4554'],
                yAxis: {
                    type: 'category',
                    data: this.state.data1
                },
                series: [
                    {
                        name: this.state.data3,
                        type: 'bar',
                        barWidth : 30,
                        itemStyle:{
                            normal:{
                                color:'#6ec6ef'
                            }
                        },
                        stack: '总量',
                        label: {
                            normal: {
                                show: true,
                                position: 'insideRight'
                            }
                        },
                        data: this.state.data2
                    },
                    {
                        name: this.state.data5,
                        type: 'bar',
                        barWidth : 30,
                        itemStyle:{
                            normal:{
                                color:'#f9d72a'
                            }
                        },
                        stack: '总量',
                        label: {
                            normal: {
                                show: true,
                                position: 'insideRight'
                            }
                        },
                        data: this.state.data4
                    }
                ]
            };
        return option
    },
    onChartReady: function(chart) {
        chart.hideLoading();
    },
    render() {
        return (
                <ReactEcharts option={this.getOption()} onChartReady={this.onChartReady} showLoading={false} style={{
                    height: '500px',
                    width: $(window).width()/2
                }}/>
        )
    }
})
var Echart3 = React.createClass({
    getInitialState:function(){
        return{
            data1:[],
            data2:[{name:'w',max:2020},{name:'w',max:2020},{name:'w',max:2020},{name:'w',max:2020},{name:'w',max:2020}]
        }
    },
    componentDidMount:function(){
        $.ajax({
            url:API_BATHPATH+'h/p3',
            dataType:'json',
            success:function(data){
                var data1=data.data.map(function(three,index){
                    return three.count
                })
                var data2=data.data.map(function(three,index){
                    return { text: three.type, max: 2500}
                })
                this.setState({data1: data1,data2:data2});
            }.bind(this)
        });
    },
    getOption() {
        var option = {
                tooltip: {
                    trigger: 'item',
                    backgroundColor : 'rgba(0,0,250,0.2)'
                },
                visualMap: {
                    color: ['red', 'yellow']
                },
                radar: {
                   indicator : this.state.data2
                },
                series : [{
                            type: 'radar',
                            symbol: 'none',
                            itemStyle: {
                                normal: {
                                    lineStyle: {
                                      width:1
                                    }
                                },
                                emphasis : {
                                    areaStyle: {color:'rgba(0,250,0,1)'}
                                }
                            },
                            data:[
                              {
                                value:this.state.data1||[1,1,1,1,1]
                              }
                            ]
                        }]
            };
        return option
    },
    onChartReady: function(chart) {
        chart.hideLoading();
    },
    render() {
        return (
            <div className='' style={{
                height: '500px',
                width: $(window).width()
            }}>
                <ReactEcharts option={this.getOption()} onChartReady={this.onChartReady} showLoading={false} style={{
                    height: '500px',
                    width: $(window).width()/2
                }}/>
            </div>
        )
    }
})
ReactDOM.render(
    <BodyMain/>, document.getElementsByClassName('g-bd')[0]);
