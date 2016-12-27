//引入css
require('bootstrap/dist/css/bootstrap.css')
require("../../css/common/global.css");
require("../../css/common/grid.css");

require("../../css/page/about.css");
 import { Tabs,Tab } from 'react-bootstrap';
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
var BodyAbout=React.createClass({
	render(){
		return(
			<div className='container'>
				<div style={{margin:'24px 0 0'}} className="page-header">
					<h2>关于我们</h2>
					<span></span>
				</div>
				<div className='head-nav'>
					<ul>
						<li><img src={require("../../img/icon_home.png")}/></li>
						<li>关于我们</li>
						<li>公司介绍</li>
					</ul>
				</div>
				<div className='row user'>
					<div className='col-lg-12'>
						<Tabs defaultActiveKey={1} id="uncontrolled-tab-example">
						   <Tab eventKey={1} title="公司介绍">
						   		<div className='tab_one'>
						   			<img src={require("../../img/img_smalll-bannner.png")}/>
						   			<p>政策通，是一家以“大数据”为核心、“大数据＋互联网＋政策法规”为发展战略，业务覆盖政策法规、建议提案、政策文件、政策解读等政策方面的全网公开数据的整合搜索。

东华光普大数据技术有限公司，是以大数据思维及技术为支撑推动中国创新发展，落实国家大数据战略，以实现“十三五”大数据规划的全面实现为使命，提供大数据资源与技术创新应用、大数据交易、数据资产评估、大数据金融的综合运营商和解决方案提供商。

东华光普是中国大数据交易标准化的开拓者。东华光普遵循“开放、规范、安全、可控”的原则，打造标准化大数据交易平台，同时支持个人和机构用户的实时在线交易，提供完整的数据交易、结算、交付、安全保障等服务。同时拥有国内最大API接口，数据交易便捷性和数量级在业内名列前茅。

政策通在上述领域和行业的锐意进取和协同发展，东华光普“大数据＋互联网＋政策法规”的多产业发展模式初显，为政策通的战略发展奠定了坚实的基础。</p>
						   		</div>
						   </Tab>
						   <Tab eventKey={2} title="联系我们">
						   		<div className='tab_one'>
						   			<img src={require("../../img/img_smalll-map.png")}/>
						   			<p>地址：北京市 海淀区 知春路 紫金数码园 3号楼 东华合创大厦806室<br/>
电话：+86-（010）6257 8032<br/>
传真：+86-（010）6266 2299<br/>
联系人:刘经理 咨询电话: 138 2222 3333<br/>
业务邮箱：hu#gpdata.com（请把#更换成@）<br/>
技术邮箱：tech#gpdata.com（请把#更换成@）</p>
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
  <BodyAbout/>,
  document.getElementsByClassName('g-bd')[0]
);