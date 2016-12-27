//引入css
var React = require('react');
 import { Tabs,Tab,Button } from 'react-bootstrap';
export default React.createClass({
	  // getInitialState:function(){
	  // 	return({
	  // 		txt:'',
	  // 		pswd1:'',
	  // 		pswd:''
	  // 	})
	  // },
	  // handleChange(e){
	  //     this.setState({
	        
	  //     })
  	//   },
	  // handleSubmit(){
   //  // 提交表单
	  //   $.ajax({
	  //     type:'post',
	  //     url:API_BATHPATH+'user/revisepwd',
	  //     contentType: "application/json",
	  //     dataType: "json",
	  //     success:function(data){
	  //         alert("修改成功")
	  //     }
	  //   })
	  // },
	render(){
		return(
			<div className='col-lg-12 user2'>
        		<h3 className='pat'>修改密码</h3>
        		<div className='col-lg-offset-3 col-lg-6'>
        			<p className='padd'>验证方式：</p>
        			<Tabs defaultActiveKey={1} id="uncontrolled-tab-example">
						   <Tab eventKey={1} title="原密码">
						   		<div className='tab_one'>
						   			<form className="form-horizontal" role="form">
									  <div className="form-group">
									    	<label className="col-sm-3 control-label">原密码</label>
									    	<div className="col-sm-9">
									      		<input name='txt' onChange={this.handleChange} type="text" className="form-control" id="inputEmail3" placeholder="请输入原密码..."/>
									    	</div>
									  </div>
									  <div className="form-group">
									    	<label className="col-sm-3 control-label">新密码</label>
									    	<div className="col-sm-9">
									    	  	<input name='pswd'  onChange={this.handleChange} type="password" className="form-control" id="inputPassword3" placeholder="请输入新密码..."/>
									    	</div>
									  </div>
									  <div className="form-group">
									    	<label htmlFor="inputPassword3" className="col-sm-3 control-label">确认密码</label>
									    	<div className="col-sm-9">
									    	  	<input name='pswd1' onChange={this.handleChange} type="password" className="form-control" id="inputPassword3" placeholder="确认新密码..."/>
									    	</div>
									  </div>
									  
									  <div className="form-group">
									  <Button onClick={this.handleSubmit} bsStyle="primary" className='pull-right'>提交</Button>
									  </div>
									</form>
						   		</div>
						   </Tab>
						   <Tab eventKey={2} title="手机号">
						   		<div className='tab_one'>
						   			<form className="form-horizontal" role="form">
									  <div className="form-group">
									    	<label className="col-sm-3 control-label">手机号</label>
									    	<div className="col-sm-9">
									      		<input type="email" className="form-control" id="inputEmail3" placeholder="请输入手机号..."/>
									    	</div>
									  </div>
									  <div className="form-group">
									    	<label className="col-sm-3 control-label">新密码</label>
									    	<div className="col-sm-9">
									    	  	<input type="password" className="form-control" id="inputPassword3" placeholder="请输入新密码..."/>
									    	</div>
									  </div>
									  <div className="form-group">
									    	<label htmlFor="inputPassword3" className="col-sm-3 control-label">确认密码</label>
									    	<div className="col-sm-9">
									    	  	<input type="password" className="form-control" id="inputPassword3" placeholder="确认新密码..."/>
									    	</div>
									  </div>
									  <div className="form-group">
									  <Button bsStyle="primary" className='pull-right'>提交</Button>
									  </div>
									</form>
						   		</div>
						   </Tab>
					</Tabs>
        		</div>
			</div>
		)
	}
})
