//props
//
//
//dispatch({
//	topicClassify:value
//})
//defaultValue:''



//引入css
var React = require('react');

window.$ = require('jquery/dist/jquery.js')
var $=window.$;
import { Cascader } from 'antd';

var API_BATHPATH=require('../../config').default;
var SelectClass=React.createClass({
	getInitialState(){
		return ({
			options:[]
		})
	},
	componentDidMount:function(){
		var self=this
			$.get(API_BATHPATH+'zt/s',function(data){
				var list=data.data.data1.map(function(one){
					return {
						value:one.policyclassificationName,
						label:one.policyclassificationName,
						children:data.data[one.policyclassificationId].map(function(one2){
							return {
								value:one2.policyclassificationName,
								label:one2.policyclassificationName
							}
						})
					}
				})
			self.setState({
				options:list
			})
		})
	},
	onChange(value){
		this.props.dispatch({
			topicClassify:value[1]||value[0]
		})
	},
	render(){
		var state=this.state;
		return(
				<Cascader size="large" placeholder='请选择数据' style={{ width: '95%' }} options={this.state.options} onChange={this.onChange} />
		)
	}
})

export default SelectClass
