//引入css
require('bootstrap/dist/css/bootstrap.css')
require("../../css/common/global.css");
require("../../css/common/grid.css");
require("../../css/page/user.css");
window.$ = require('jquery/dist/jquery.js')
 import { PanelGroup,Panel } from 'react-bootstrap';
var React = require('react');
var ReactDOM = require('react-dom');
import Header from '../components/header'
import Footer from '../components/footer/footer.js'
ReactDOM.render(
  <Header/>,
  document.getElementsByClassName('g-hd')[0]
);
ReactDOM.render(
  <Footer/>,
  document.getElementsByClassName('g-ft')[0]
);


import  User1 from '../components/user/User1'
import  User2 from '../components/user/User2'
import  User3 from '../components/user/User3'
import  User4 from '../components/user/User4'
import  User5 from '../components/user/User5'
import  User6 from '../components/user/User6'
var BodyUser=React.createClass({
  getInitialState(){
      return ({
        hash:location.hash||'#info'
      })
  },
  componentDidMount(){
      window.onhashchange=function(){
        this.setState({
          hash:location.hash
        })
      }.bind(this)
  },
	render(){
		var children=<User1/>;
		switch(this.state.hash){
			case '#info':
			children=<User1/>
      break;
			case '#pswd':
			children=<User2/>
      break;
			case '#msg':
			children=<User3/>
      break;
			case '#sub':
			children=<User4/>
      break;
			case '#col':
			children=<User5/>
      break;
			case '#store':
			children=<User6/>
      break;
		}
		return(
			<div className='container'>
				<div style={{margin:'24px 0 0'}} className="page-header">
					<h2>个人中心</h2>
					<span></span>
				</div>
				<div className='head-nav'>
					<ul>
						<li><img src={require("../../img/icon_home.png")}/></li>
						<li>个人中心</li>
						<li>个人资料</li>
					</ul>
				</div>
				<div className='row user'>
					<div className='col-lg-2 bjt'>
  						<PanelGroup   defaultActiveKey='1' accordion={false}>
  						  <Panel collapsible={false} header="账号管理" eventKey="1">
  						  	<p><a className={this.state.hash==='#info'?'active':'c555'} href='#info'>个人资料</a></p>
  						  	<p><a className={this.state.hash==='#pswd'?'active':'c555'} href='#pswd'>密码管理</a></p>
  						  </Panel>
  						  <Panel collapsible={false} header="服务管理" eventKey="1">
  						  	<p><a className={this.state.hash==='#msg'?'active':'c555'} href='#msg'>消息提醒</a></p>
  						  	<p><a className={this.state.hash==='#sub'?'active':'c555'} href='#sub'>订阅服务</a></p>
  						  </Panel>
  						  <Panel collapsible={false} header="书房管理" eventKey="1">
  						  	<p><a className={this.state.hash==='#col'?'active':'c555'} href='#col'>政策收藏</a></p>
  						  	<p><a className={this.state.hash==='#store'?'active':'c555'} href='#store'>订阅书房</a></p>
  						  </Panel>
  						</PanelGroup>
					</div>
					<div className='col-lg-10'>
              {children}
					</div>
				</div>
			</div>
		)
	}
})
ReactDOM.render(
  <BodyUser/>,
  document.getElementsByClassName('g-bd')[0]
);
