//css
require('bootstrap/dist/css/bootstrap.css')
require("../../css/common/global.css");
require("../../css/common/grid.css");
require("../../css/page/search.css");
 import { Button } from 'react-bootstrap';
import 'antd/dist/antd.css';
var API_BATHPATH=require('../config').default;
var React = require('react');
window.$ = require('jquery/dist/jquery.js')
var $=window.$;
var ReactDOM = require('react-dom');
var parseQueryString=require('../components/Utils.js').parseQueryString
//模块
import SelectClass from '../components/search/SelectClass.js'
import Checkbox1 from '../components/search/Checkbox1.js'
import Checkbox2 from '../components/search/Checkbox2.js'
import SelectDate from '../components/search/SelectDate.js'
import SelectPublocation from '../components/search/SelectPublocation.js'
import Item from '../components/search/Item.js'
import Pagination from '../components/search/Pagination.js'

//main
var BodyChoose=React.createClass({
	getInitialState(){
    var str=decodeURIComponent(parseQueryString(location.href)['str'])||''
    var index=parseQueryString(location.href)['index']
    return({
      limit:12,
      offset:0,
      str:str||'',

      index:index||'zcfg,zcwj,zcjd,jyta,',
      topicClassify:'',
      pubOrg:'',
      placed:'',
      province:'',
      city:'',
      county:'',
      startTime:'',
      endTime:'',
      isEffect:'',
      isPilot:'',

      isPub:'',
      isHide:'',

      total:0,
      keyWords:[],
      rows:[]
		})
	},
	componentDidMount:function(){
    this.loadItems();
  },
  loadItems(){
    var state=this.state
    var self=this
    var params={
      index:state.index,
      topicClassify:state.topicClassify,
      pubOrg:state.pubOrg,
      placed:state.placed,
      province:state.province,
      city:state.city,
      county:state.county,

      startTime:state.startTime,
      endTime:state.endTime,
      isEffect:state.isEffect,
      isPilot:state.isPilot
      // isPub:state.isPub,
      // isHide:state.isHide
    }
      $.ajax({
        type:'post',
        url:API_BATHPATH+'ts/q?str='+state.str+'&limit='+state.limit+'&offset='+state.offset,
        data:JSON.stringify(params),
        contentType: "application/json",
        dataType: "json",
        success:function(data){
          if (data.status==='ERROR') {
            // alert(data.cause)
            return false ;
          }
          var data=data.data
          var keyWords=data.keyWords&&data.keyWords.split(' ')||[];
          keyWords=keyWords.filter(function(one){
            return one!==''
          })
          self.setState({
            keyWords:keyWords,
            total:data.total,
            rows:data.rows
          })

        }
      })
  },
  dispatch(obj){
    var self=this
    this.setState(obj,function(){
      self.loadItems()
    })
  },
	render(){
		return(
				<div>
					<div className='container choose'>
						<div className='row head_top'>
							<div className='col-lg-12 add'>
								<div className='col-lg-6' style={{paddingLeft:'0'}}>
                    <label className="col-sm-1 c-label">搜&nbsp;&nbsp;&nbsp;索:</label>
                    <div className="col-sm-10">
                      <form method = "get" action = "search.html">
                      <input required name='str' defaultValue={this.state.str||''} type="text" className="search_ip form-control" placeholder="请输入新条件"/>
                      <Button type='submit' bsStyle="danger" className='search_btns'></Button>
                    </form>
                    </div>
							  	</div>
							</div>
							<div className='col-lg-12 top'>
								<div className='line-h'>关键字:</div>
                  {this.state.keyWords&&this.state.keyWords.map(function(one,index){
                  return <div key={index} className='xunhuan'>
                  <span>{one}</span>
                  <em></em></div>
                })}
							</div>
							<div className='col-lg-12 middle'>
								<div style={{padding:'0'}} className='col-lg-2 col-md-12 col-xs-12'>
                  <SelectClass defaultValue='' dispatch={this.dispatch}/>
								</div>
								<div className='col-lg-6 col-md-12 col-xs-12 marr'>
                  <SelectPublocation dispatch={this.dispatch}/>
								</div>
								<div className='col-md-offset-1 col-lg-3 col-md-12 col-xs-12 time'>
                  <SelectDate　dispatch={this.dispatch}/>
								</div>
							</div>
							<div className='col-lg-12 bottom'>
              <Checkbox1 defaultValue={this.state.index} dispatch={this.dispatch}/>
							</div>
							<div className='col-lg-12 fice'>
                <div className=''>
                  <a href='#'>综合</a>
                  <a href='#'>最新</a>
                </div>
                <Checkbox2 dispatch={this.dispatch} style={{float:'left',width:'300px'}}/>
                <p style={{float:'right',lineHeight:'34px'}}><span>云图</span>　|　<span>列表</span></p>
							</div>
							<div className='col-lg-12 picture'>
                {this.state.rows.map(function(one,index){
                  return <Item  key={index} data={one}/>
                })}
							</div>
							<div style={{paddingLeft:'40%'}} className='col-lg-12'>
								<Pagination dispatch={this.dispatch} total={this.state.total} />
							</div>
						</div>
					</div>
				</div>
			)
	}
})
import Header from '../components/header/index.js'
import Footer from '../components/footer/footer.js'

ReactDOM.render(<Header/>,document.getElementsByClassName('g-hd')[0]);
ReactDOM.render(<BodyChoose/>,document.getElementsByClassName('g-bd')[0]);
ReactDOM.render(<Footer/>,document.getElementsByClassName('g-ft')[0]);
