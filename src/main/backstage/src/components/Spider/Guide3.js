var React = require('react');
import {Form} from './Form'
var $=window.$;
var Guide3=React.createClass({
	_onSubmit:Form._onSubmit,
	hideLoading:Form._hideLoading,
	_onChange:Form._onChange,
	_formGroupClass: Form._formGroupClass,
	_reSet:Form._reSet,
	getInitialState(){
	    return({
	      depth:localStorage.getItem('depth')||'1',
	      loading:false,
	      errors:{}
	})},
	_create(){
		localStorage.setItem('depth',this.state.depth);
	    var data={
	        'depth':this.state.depth
	      },
	      data=JSON.stringify(data);
					var spiderId =localStorage.getItem('spiderId')||'';
					if (spiderId==='') {
						alert('请先保存基本属性')
						return false;
					}
	      return $.ajax({
	        url:window.basePath+ 'sp/ch/'+spiderId,
	        type: 'PUT',
	        contentType:'application/json',
	        data: data,
	        beforeSend: function () {
	          this.setState({loading: true});
	        }.bind(this)
	      })
	},
	_validate: function () {
	    var errors = {}
	    if(this.state.depth == '') {
	      errors.depth = '请设置采集深度';
	    }
	    return errors;
	},
	_onSuccess: function (data) {
		self.setState({
			loading:false
		})
	    var self=this;
	    if(data.status==='ERROR'){
	      window.toastr.error(data.cause);
	    }else if (data.status==='SUCCESS'){
				alert('已保存')
	    }else{
	    	return false
	    }
	},
	_onError: function () {

	    var message = '网络错误,请重试';
	    window.toastr.error(message);
			self.setState({
				loading:false
			})
	},
  	render(){
  	  return(
      <form onSubmit={this._onSubmit}>
      	<div className={this._formGroupClass(this.state.errors.depth)}>
      	    <label>爬取深度</label>
      	    <select defaultValue={this.state.depth} name='depth' onChange={this._onChange} className='form-control'>
      	    	<option value='1'>1</option>
      	    	<option value='2'>2</option>
      	    	<option value='3'>3</option>
      	    	<option value='4'>4</option>
      	    	<option value='5'>5</option>
      	    	<option value='6'>6</option>
      	    	<option value='7'>7</option>
      	    	<option value='8'>8</option>
      	    	<option value='9'>9</option>
      	    	<option value='10'>10</option>
      	    </select>
      	    <span className='help-block'>{this.state.errors.depth}</span>
      	</div>
      	<div className={this._formGroupClass(this.state.errors.ip)}>
      	    <label>是否启用IP代理</label>
      	    <select defaultValue='1' name='ip' onChange={this._onChange} className='form-control'>
      	    	<option value='0'>不启用</option>
      	    	<option value='1'>启用</option>
      	    </select>
      	    <span className='help-block'>{this.state.errors.ip}</span>
      	</div>
      	<button style={{paddingLeft:'100px',paddingRight:'100px'}} className='btn btn-success mgl10 pull-right'  disabled={this.state.loading} type='submit'>{this.state.loading?'正在提交':'保存'}</button>
      </form>
      )
  	}
})
module.exports = Guide3
