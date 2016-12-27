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
import { DatePicker } from 'antd';


var Checkbox2=React.createClass({
	getInitialState(){
		return ({
			none: true,
			isEffect: false,
			isPilot: false
		})
	},
	handleChange1(e){
		var self=this;
		this.setState({
			none:false,
			isEffect:e.target.checked
		},function(){
			self.props.dispatch({
				isEffect:Number(self.state.isEffect)
			})
			if (!self.state.isEffect&&!self.state.isPilot) {
				self.setState({
					none:true
				})
			}
		})
	},
	handleChange2(e){
		var self=this;
		this.setState({
			none:false,
			isPilot:e.target.checked
		},function(){
			self.props.dispatch({
				isPilot:Number(self.state.isPilot)
			})
			if (!self.state.isEffect&&!self.state.isPilot) {
				self.setState({
					none:true
				})
			}
		})
	},
	handleNone(e){
		var self=this;
		if (e.target.checked) {
			this.setState({
				none:true,
				isEffect:false,
				isPilot:false
			},function(){
				self.props.dispatch({
					isEffect:0,
					isPilot:0
				})
			})
		}else{
			this.setState({
				none:false
			})
		}
	},
	render(){
		return(
			<div>
				<label className="radio-inline">
					<input onChange={this.handleNone} value='0' checked={this.state.none} name='none' className="radioclass" type="checkbox"/><span></span>
					不限
				</label>
				<label className="radio-inline">
					<input onChange={this.handleChange1} value='0' checked={this.state.isEffect} name='isEffect' type="checkbox"/><span></span>
					现行有效
				</label>
				<label className="radio-inline">
					<input onChange={this.handleChange2} value='0' checked={this.state.isPilot} name='isPilot' type="checkbox"/><span></span>
					试点
				</label>
			</div>
		)
	}
})

export default Checkbox2
