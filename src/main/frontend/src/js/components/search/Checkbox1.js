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


var Checkbox1=React.createClass({
	getInitialState(){
		return ({
			all:false,
			zcfg: this.props.defaultValue.indexOf('zcfg')>-1,
			zcwj: this.props.defaultValue.indexOf('zcwj')>-1,
			zcjd: this.props.defaultValue.indexOf('zcjd')>-1,
			jyta: this.props.defaultValue.indexOf('jyta')>-1
		})
	},
	handleChange(e){
		var self=this;
		this.setState({
			[e.target.name]:e.target.checked,
			all:false
		},function(){
			var index='';
			if (this.state.zcfg) index+='zcfg,'
			if (this.state.zcwj) index+='zcwj,'
			if (this.state.zcjd) index+='zcjd,'
			if (this.state.jyta) index+='jyta'
			console.log(index)
			if (index==='') {
				index='zcfg,zcwj,zcjd,jyta'
				self.setState({
					all:true
				})
			}
			self.props.dispatch({
				index:index
			})
		})
	},
	selectAll(e){
		var self=this;
		if (e.target.checked) {
			this.setState({
				all:true,
				zcfg: false,
				zcwj: false,
				zcjd: false,
				jyta: false
			},function(){
				self.props.dispatch({
					index:'zcfg,zcwj,zcjd,jyta'
				})
			})
		}
	},
	render(){
		return(
			<div>
				<label className="radio-inline" style={{paddingLeft:'0'}}>
					<input checked={this.state.all} onChange={this.selectAll} type="checkbox" name="index" value="zcfg,zcwj,zcjd,jyta"/><span></span>
					不限
				</label>
				<label className="radio-inline">
					<input checked={this.state.zcfg} onChange={this.handleChange} type="checkbox" name="zcfg"  value="zcfg"/><span></span>
					政策法规
				</label>
				<label className="radio-inline">
					<input checked={this.state.zcwj} onChange={this.handleChange} type="checkbox" name="zcwj"  value="zcwj"/><span></span>
					政策文件
				</label>
				<label className="radio-inline">
					<input checked={this.state.zcjd} onChange={this.handleChange} type="checkbox" name="zcjd"  value="zcjd"/><span></span>
					政策解读
				</label>
				<label className="radio-inline">
					<input checked={this.state.jyta} onChange={this.handleChange} type="checkbox" name="jyta" value="jyta"/><span></span>
					建议提案
				</label>
			</div>
		)
	}
})

export default Checkbox1
