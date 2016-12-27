//引入css
require('bootstrap/dist/css/bootstrap.css')
require("../../css/common/global.css");
require("../../css/common/grid.css");
require("../../css/page/sub.css");
import { Button } from 'react-bootstrap';
var React = require('react');
var ReactDOM = require('react-dom');
window.$ = require('jquery/dist/jquery.js')
var $=window.$;
var API_BATHPATH=require('../config').default;
import KeyWords from '../components/sub/KeyWords.js'

class Subtip extends React.Component {
  state={
    time:5
  };
  componentDidMount(){
    var self=this;
    window.a=setInterval(function(){
      self.setState({
        time:self.state.time-1
      })
    },1000)
    window.b=setTimeout(function(){
      clearInterval(a);
      location.reload();
    },5000)
  }
  componentWillUnMount(){
    clearInterval(a);
    clearTimeout(b);
  }
  render(){
    return <div className='container'>
      <div className="page-header">
        <h2>订阅服务</h2>
        <span></span>
      </div>
      <div className='pro'>
      </div>
      <div className="row subread">
      </div>
      <div className='row style'>
        <p>订阅成功！</p>
        <p>订阅号：
        <span>{this.props.subId}</span></p>
        <p>当前订阅信息已提交</p>
        <p>{this.state.time}
        秒后返回＞＞点击<a href='./sub.html'>快度返回</a></p>
      </div>
      <div className='row'>
      </div>
    </div>
  }

}

var BodySub=React.createClass({
  getInitialState(){
    return  ({
      uContext:'',
      uRemark:'',
      keyWords:[]
    })
  },
  handleChange(e){
      // 更改关原文和备注
      this.setState({
        [e.target.name]:e.target.value
      })
  },
  handleSubmit(){
    // 提交表单　　处理keyWords
    var params=this.state;
    params.keyWords=this.state.keyWords.join(',');
    $.ajax({
      type:'post',
      url:API_BATHPATH+'su/a',
      data:JSON.stringify(params),
      contentType: "application/json",
      dataType: "json",
      success:function(data){
          if (data.status==='SUCCESS') {
            ReactDOM.render(<Subtip subId={data.data}/>,document.getElementsByClassName('g-bd')[0]);
          }else {
            alert(data.cause)
          }
      }
    })
  },
  handleGetKeywords(){
    var self=this;
    $.ajax({
      type:'post',
      url:API_BATHPATH+'su/c',
      data:JSON.stringify({subscriber:self.state.uContext}),
      contentType: "application/json",
      dataType: "json",
      success:function(data){
        if (data.status==='ERROR') {
          alert(data.cause)
          return false;
        }
        self.setState({
          keyWords:data.data
        })
      }
    })
  },
  dispatch:function(obj){
    this.setState(obj)
  },
  EnterPress(e){
    if(e.key == 'Enter'){
      this.handleGetKeywords()
    }
  },
	render(){
		return(
					<div className='container'>
						<div className="page-header">
						  <h2>订阅服务</h2>
              <span></span>
						</div>
						<div className='pro'>
						  <a><span>01</span>确定标签</a>
						  <a><span>02</span>提交申请</a>
						  <a><span>03</span>标签匹配</a>
						  <a><span>04</span>推送文章</a>
						  <a><span>05</span>随时阅览</a>
						  <p className='read'>随心所欲订阅政策文章，竭诚提供个性订阅服务。定制需求推送私有书房，随时随地阅览天下政策</p>
						</div>
						<div className="row subread">
              <div className="col-md-7 col-md-offset-2 paddqx">
                      <div style={{marginBottom:'0'}} className="form-group">
                          <input onKeyPress={this.EnterPress}  onChange={this.handleChange} name='uContext' value={this.state.uContext} type="text" className="form-control search_ip" placeholder="请输入定制内容..."/>
                          <Button onClick={this.handleGetKeywords} className='search_btns'>生成</Button>
                      </div>
                  <div className='tishi'>示例:2016至2017年，北京市政府颁布，关于税收征收类，政策文件。</div>
              </div>
              <div className="col-md-1 paddqx backg">
                <span>生成关键字段<br/>方便订阅</span>
              </div>
            </div>
            <div className='row'>
              <div className='col-md-8 col-md-offset-2 analysis'>
                <h4><strong>解析:</strong></h4>
                  <KeyWords dispatch={this.dispatch} keyWords={this.state.keyWords}/>
              </div>
            </div>
            <div className='row'>
              <div className='col-md-7 col-md-offset-2 textarea form-group'>
                  <textarea placeholder='备注可以让您的订阅更精确...' onChange={this.handleChange} name='uRemark' value={this.state.uRemark} className="form-control"></textarea>
                  <Button className='pull-right'>重置</Button>
                  <Button onClick={this.handleSubmit} bsStyle="primary" className='pull-right'>提交</Button>
              </div>
            </div>
					</div>
			)
	}
})

import Header from '../components/header'
import Footer from '../components/footer/footer.js'
ReactDOM.render(<Header/>,document.getElementsByClassName('g-hd')[0]);
ReactDOM.render(<BodySub/>,document.getElementsByClassName('g-bd')[0]);
ReactDOM.render(<Footer/>,document.getElementsByClassName('g-ft')[0]);
