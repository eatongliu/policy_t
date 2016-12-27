//加载模块css
require('./css/footer.css');
var logo=require('../../../img/logo.png');
 import { OverlayTrigger,tooltip3,tooltip2,tooltip1,Tooltip,ButtonToolbar } from 'react-bootstrap';

var React = require('react');

class Footer extends React.Component{
	render(){
	const tooltip3 = <Tooltip id="tooltip3"><strong>Holy guacamole!</strong> Check this info.</Tooltip>,
		tooltip2 = <Tooltip id="tooltip2"><strong>Holy guacamole!</strong> Check this info.</Tooltip>,
		tooltip1 = <Tooltip id="tooltip1"><strong>Holy guacamole!</strong> Check this info.</Tooltip>
		return <footer className='foot'>
		<div className='container foot_me'>
			<nav className='navbar navbar-default bor_top' role='navigation'>
			  <div className='container-fluid'>
			    <div className='navbar-header'>
			      <button type='button' className='navbar-toggle collapsed' data-toggle='collapse' data-target='#bs-example-navbar-collapse-1'>
			        <span className='sr-only'>Toggle navigation</span>
			        <span className='icon-bar'></span>
			        <span className='icon-bar'></span>
			        <span className='icon-bar'></span>
			      </button>
			      <a className='navbar-brand foot-bg' href='#'><img src={logo}/></a>
			    </div>
			    <div className='collapse navbar-collapse' id='bs-example-navbar-collapse-1'>
			      <ul className='nav navbar-nav'>
			        <li><a href='./index.html'>首页</a></li>
			        <li><a href='#'>个人中心</a></li>
			        <li><a href='./about.html'>关于我们</a></li>
			      </ul>
			      <ul className='nav navbar-nav navbar-right right_nav'>
			      	<li><ButtonToolbar>
			        <OverlayTrigger placement="top" overlay={tooltip1}>
				      <div className='wh'></div>
				    </OverlayTrigger>
    				</ButtonToolbar></li>
			        <li><ButtonToolbar>
			        <OverlayTrigger placement="top" overlay={tooltip2}>
				      <div className='wh'></div>
				    </OverlayTrigger>
    				</ButtonToolbar></li>
			        <li>
			        <ButtonToolbar>
			        <OverlayTrigger placement="top" overlay={tooltip3}>
				      <div className='wh'></div>
				    </OverlayTrigger>
    				</ButtonToolbar>
    				</li>
			      </ul>

			    </div>
			  </div>
			</nav>
			<div className='foot_top'>
				<p>京ICP备05004897号-1&nbsp;|&nbsp;<a href='javascript:void;'>常见问题</a>&nbsp;|&nbsp;<a href='javascript:void;'>网站地图</a></p>
				<p>名称&nbsp;政策数据中心　版权所有&nbsp;Copyright2016-2020&nbsp;<a href='javascript:void;'>京ICP备05004897号</a></p>
				<p>本站所刊登的信息，数据和各种专栏材料，未经授权禁止下载使用</p>
				<p><a href='javascript:void;'>东华光普大数据信息技术有限公司</a>　主办</p>
			</div>
		</div>
	</footer>
	}
}

module.exports= Footer