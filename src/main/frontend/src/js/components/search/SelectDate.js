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


var SelectDate=React.createClass({
	getInitialState(){
		return ({
			startTime: null,
			endTime: null
		})
	},


		disabledStartDate:function(startTime) {
			const endTime = this.state.endTime;
			if (!startTime || !endTime) {
				return false;
			}
			return startTime.valueOf() > endTime.valueOf();
		},

		disabledEndDate:function(endTime) {
			const startTime = this.state.startTime;
			if (!endTime || !startTime) {
				return false;
			}
			return endTime.valueOf() <= startTime.valueOf();
		},


	onChange(field,value){
		console.log(value.format('YYYY'));

		this.setState({
			 [field]:value,
		})
		this.props.dispatch({
			[field]:value.format('YYYY')
		})
	},
	onStartChange: function(value){
		this.onChange('startTime', value);
	},
	onEndChange: function(value){
		this.onChange('endTime', value);
	},
	render(){
		const { startTime, endTime, endOpen } = this.state;
		return(<div>
			<DatePicker
				disabledDate={this.disabledStartDate}
				showTime
				format="YYYY-MM-DD"
				value={startTime}
				placeholder="起始时间"
				onChange={this.onStartChange}
				onOpenChange={this.handleStartOpenChange}
			/>
			<DatePicker
				disabledDate={this.disabledEndDate}
				showTime
				format="YYYY-MM-DD"
				value={endTime}
				placeholder="结束时间"
				onChange={this.onEndChange}
				onOpenChange={this.handleEndOpenChange}
			/>
			</div>
		)
	}
})

export default SelectDate
