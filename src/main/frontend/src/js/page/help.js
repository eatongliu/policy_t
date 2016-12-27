//引入css
require('bootstrap/dist/css/bootstrap.css')
require("../../css/common/global.css");
require("../../css/common/grid.css");

require("../../css/page/help.css");
 import { Tabs,Tab,Accordion,Panel } from 'react-bootstrap';
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
var BodyHelp=React.createClass({
	render(){
		return(
			<div className='container'>
				<div style={{margin:'24px 0 0'}} className="page-header">
					<h2>遇到问题</h2>
					<span></span>
				</div>
				<div className='head-nav'>
					<ul>
						<li><img src={require("../../img/icon_home.png")}/></li>
						<li>遇到问题</li>
						<li></li>
					</ul>
				</div>
				<div className='row user'>
					<div className='col-lg-12'>
						<Tabs defaultActiveKey={1} id="uncontrolled-tab-example">
						   <Tab eventKey={1} title="网页搜索特色功能">
						   		<div className='tab_one'>
						   			<Accordion>
									    <Panel header="搜索时出现的推荐政策是啥玩意？" eventKey="1">
									      Anim
									    </Panel>
									    <Panel header="我可以使用搜狗输入法么" eventKey="2">
									      不可以，只能用QQ输入法
									    </Panel>
									    <Panel header="什么是政策法规的相关政策" eventKey="3">
									      Anim
									    </Panel>
									 </Accordion>
						   		</div>
						   </Tab>
						   <Tab eventKey={2} title="常见搜索问题">
						   		<div className='tab_one'>
						   			<Accordion>
									    <Panel header="搜索时出现的推荐政策是啥玩意？" eventKey="1">
									      Anim
									    </Panel>
									    <Panel header="我可以使用搜狗输入法么" eventKey="2">
									      不可以，只能用QQ输入法
									    </Panel>
									    <Panel header="什么是政策法规的相关政策" eventKey="3">
									      Anim
									    </Panel>
									 </Accordion>
						   		</div>
						   </Tab>
						   <Tab eventKey={3} title="政策通搜索技巧">
						   		<div className='tab_one'>
						   			<Accordion>
									    <Panel header="搜索时出现的推荐政策是啥玩意？" eventKey="1">
									      Anim
									    </Panel>
									    <Panel header="我可以使用搜狗输入法么" eventKey="2">
									      不可以，只能用QQ输入法
									    </Panel>
									    <Panel header="什么是政策法规的相关政策" eventKey="3">
									      Anim
									    </Panel>
									 </Accordion>
						   		</div>
						   </Tab>
						</Tabs>
					</div>
				</div>
			</div>
			)
	}
})
ReactDOM.render(
  <BodyHelp/>,
  document.getElementsByClassName('g-bd')[0]
);