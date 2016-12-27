//引入css
var React = require('react');

window.$ = require('jquery/dist/jquery.js')
var $=window.$;

var API_BATHPATH=require('../../config').default;
var User1=React.createClass({
	getInitialState(){
		return ({
			headPic:'',
			nickName:'',
			lastTime:'',
			lastSite:'',

			userName:'2334243',
			sex:'',
			qq:'',
			weixin:'',
			email:''
		})
	},
	componentDidMount:function(){
		var self=this;
    console.log(API_BATHPATH)
		$.get(API_BATHPATH+'user/showself',function(data){
			self.setState(data.data)
		})
	},
	render(){
		var state=this.state;
		return(
			<div style={{paddingTop:'10px',paddingLeft:'30px'}}>
				<div style={{paddingBottom:'20px',borderBottom:'1px solid #e2e2e2',display:'flex',align:'flex-start'}}>
					<div className='userLogo' style={{marginRight:'26px',height:'120px',width:'120px'}}>
						<img href/>
					</div>
					<div style={{flex:'1'}}>
						<h2>{state.nickName}</h2>
						<div>
							<p>上次登录时间　：{state.lastTime}</p>
							<p>上次登录地点　：{state.lastSite}　（安全登录）</p>
						</div>
					</div>
				</div>
        <div className='userinfo' style={{paddingTop:'30px'}}>
					<p>帐号信息：{state.userName}</p>
					<p>性别：{state.sex}</p>
					<p>关联ＱＱ：{state.qq}</p>
					<p>关联微信：{state.weixin}</p>
					<p>绑定邮箱：{state.email}</p>
				</div>
			</div>
		)
	}
})

export default User1
