//引入css
require('bootstrap/dist/css/bootstrap.css')
require("../../css/common/global.css");
require("../../css/common/grid.css");
require("../../css/page/article.css");
import 'antd/dist/antd.css';
import { BackTop } from 'antd';
var React = require('react');
var ReactDOM = require('react-dom');
var parseQueryString=require('../components/Utils.js').parseQueryString
window.$ = require('jquery/dist/jquery.js')
var $=window.$;
var API_BATHPATH=require('../config').default;


var BodyArticle=React.createClass({
  getInitialState(){
    return ({
      fontSize:12,
      article:{}
    })
  },
  componentDidMount(){
    var pdId=parseQueryString(location.href)['pdId']
    var self=this;
    $.get(API_BATHPATH+'list/q/'+pdId,function(data){
      self.setState({
        article:data.data.rows[0]
      })
      document.oncontextmenu=function(){
        return false;
      };
      document.getElementsByTagName('body')[0].oncopy=function(){return false}
    })
  },
  handleChangefont(size){
    this.setState({
      fontSize:size
    })
  },
	render(){
    var article=this.state.article;
		return(
				<div>
					<div className='container article'>
						<div className='ad'><img style={{width:'100%'}} src={require('../../img/gg.png')}/></div>
						<p className='nav_top'>
							<strong>当前位置：</strong>
							<a>经济>税收</a>
							<a>北京</a>
							<a>市政府文件</a>
							&nbsp;&nbsp;>>&nbsp;&nbsp;
							<span>经济</span>
						</p>
						<div className='row title'>
							<div className='col-lg-6 col-xs-12'>
								<p><span>主题分类</span><a href='#'>{article.topicClassify}</a></p>
								<p><span>发文机构</span><a href='#'>{article.pubOrg}</a></p>
								<p><span style={{letterSpacing:'5px'}}>有效性</span><a>{article.isEffect?'现行有效':'无效'}</a></p>
							</div>
							<div className='col-lg-6 col-sxs-12'>
								<p><span>成文日期</span><a href='#'>{article.createDate}</a></p>
								<p><span>发文字号</span><a href='#'>{article.issuedNum}</a></p>
								<p><span>原文链接</span><a href={article.linkAddress}>{article.link}</a></p>
							</div>
						</div>
						<div className='row content'>
							<div className='col-lg-9 col-xs-9'>
								<div className='top text-right'>
									<span><a>326666</a></span>
									<span><a>326666</a></span>
									<span className='font'>字号：
                    				<a onClick={this.handleChangefont.bind(this,12)} style={{color:this.state.fontSize===12&&'#FA5353',fontSize:'24px'}}>A</a>
                    				<a onClick={this.handleChangefont.bind(this,14)} style={{color:this.state.fontSize===14&&'#FA5353',fontSize:'30px'}}>A</a>
                    				<a onClick={this.handleChangefont.bind(this,18)} style={{color:this.state.fontSize===18&&'#FA5353',fontSize:'36px'}}>A</a>
                  					</span>
									<span><a onClick={window.print}>打印全文</a></span>
									<span><a>326666</a></span>
								</div>
								<div className=''>
									<h2 style={{marginBottom:'20px'}} className='text-center'>{article.pdName}</h2>
                  					<p style={{fontSize:this.state.fontSize}} dangerouslySetInnerHTML={{__html:article.content}}></p>
								</div>
								<div className='fenxiang'>
									<ul>
										<li>分享到:</li>
										<li></li>
										<li></li>
										<li></li>
									</ul>
								</div>
							</div>
							<div className='col-lg-3 col-xs-3 bg'>
								<div className='right_content'>
									<p className='like'>猜你喜欢</p>
									<div className='pad'>
										<a href='#'>[政策解读]</a>
										<p>北京市政府熟手保证你好</p>
										<p><span>2016-08-11</span>
										<span className='pull-right'>3.2万</span>
										<span className='pull-right'>3.2万</span></p>
									</div>
								</div>
								<div className='right_content'>
									<p className='like'>相关政策</p>
									<div className='pad'>
										<a href='#'>[政策解读]</a>
										<p>北京市政府熟手保证你好</p>
										<p><span>2016-08-11</span>
										<span className='pull-right'>3.2万</span>
										<span className='pull-right'>3.2万</span></p>
									</div>
								</div>
							</div>
						</div>
						<div>
						    <BackTop />
						</div>
					</div>
				</div>
			)
	}
})

import Header from '../components/header'
import Footer from '../components/footer/footer.js'
ReactDOM.render(<Header/>,document.getElementsByClassName('g-hd')[0]);
ReactDOM.render(<Footer/>,document.getElementsByClassName('g-ft')[0]);
ReactDOM.render(<BodyArticle/>,document.getElementsByClassName('g-bd')[0]);
