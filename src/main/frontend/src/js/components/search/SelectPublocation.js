//props
//
//defaultValue:''
//
//dispatch({
//  "pubOrg":"颁布机构"
//  "placed":"具体单位",
//  "province":"省",
//  "city":"市",
//  "county":"县",
//})



//引入css
var React = require('react');
window.$ = require('jquery/dist/jquery.js')
var $=window.$;
import { Cascader,Select } from 'antd';
const Option = Select.Option;

var API_BATHPATH=require('../../config').default;


var SelectPublocation=React.createClass({
	getInitialState(){
		return ({
			//数据
			Datapub:{data1:[{},{}]},
			placed:'',

			Dataprovince:[],
			Datacity:[],
			DataCounty:[],

			//出口
			pubOrgId:'',//需要查询
			province:'',//需要查询
			city:'',//需要查询
			county:''
		})
	},
	componentDidMount:function(){
		var self=this
		// 部门
			$.get(API_BATHPATH+'bb/s',function(data){
				self.setState({
					Datapub:data.data
				},function(){
					//省
					$.get(API_BATHPATH+'ad/p',function(data){
						self.setState({Dataprovince:data.data})
					})
				})
		})
	},
	onChange2(value){
		//单位
		var self=this;
		this.setState({
			placed:value
		},function(){
			self.props.dispatch({
				placed:value
			})
		})
	},
	onChange3(value){
		//省
		//加载市
		this.getcity(value)
		// 设置省，还原市县
		this.setState({
			province:value,
			city:'',
			county:''
		})
		var self=this
			var province=''
			this.state.Dataprovince.map(function(one,index){
				if (one.codeid==value) {
					province=one.cityName
				}
			})
			self.props.dispatch({
				province:province,
				city:'',
				county:''
			})
	},
	onChange4(value){
		//城市
		//加载县
		this.getcounty(value)

		//查询名称
		var cityName=''
		this.state.Datacity.map(function(one){
			if (one.codeid==value) {
				cityName=one.cityName
			}
		})
		//设置市　还原县
		var self=this;
		this.setState({
			city:value,
			county:''
		},function(){
			self.props.dispatch({
				city:cityName,
				county:''
			})
		})
	},
	onChange5(value){
		// 县城
		this.setState({
			county:value
		})
		this.props.dispatch({
			county:value
		})
	},
	getcity(codeid){
		if (this.state.pubOrgId<8) {
			return false
		}
		var self=this;
		$.get(API_BATHPATH+'ad/c/'+codeid,function(data){
			var data=data.data
			self.setState({
				Datacity:data
			})
		})
	},
	getcounty(codeid){
		if (this.state.pubOrgId<13) {
			return false
		}
		var self =this;
		$.get(API_BATHPATH+'ad/c/'+codeid,function(data){
			var data=data.data
			self.setState({
				DataCounty:data
			})
		})
	},
	handlePubOrg(value){
		var self=this;
		var state=this.state;
		this.setState({
			pubOrgId:value,
			placed:'',
			province:'',
			city:'',
			county:''
		},function(){
			// 查询名称
			var pubOrg=''
			self.state.Datapub.data1.map(function(one){
				if (value==one.regionId) {
					pubOrg=one.regionName
				}
			})
			console.log(pubOrg)
			self.props.dispatch({
				pubOrg:pubOrg,
				placed:'',
				province:'',
				city:'',
				county:''
			})
		})
	},
	render(){
		var Datapub=this.state.Datapub
		var Dataprovince=this.state.Dataprovince
		var pubOrgId=this.state.pubOrgId
		return(
			<div className='row'>

				<Select classsName='pull-left' size="large" placeholder="请选择发布部门" style={{ width: 120 }} onChange={this.handlePubOrg}>
          {Datapub&&Datapub.data1&&Datapub.data1.map((one,index) => <Option value={one.regionId+''} key={index}>{one.regionName}</Option>)}
        </Select>

				{pubOrgId!==''&&	<Select showSearch size="large" classsName='pull-left' placeholder="请选择具体部门"  value={this.state.placed}  style={{ width: 100 }} onChange={this.onChange2}>
				  {pubOrgId&&Datapub[pubOrgId].map((one,index) => <Option value={one.regionName} key={'placeds'+index}>{one.regionName}</Option>)}
				</Select>}

				{pubOrgId>2&&	<Select size="large" classsName='pull-left' placeholder="请选择省"  value={this.state.province+''}  style={{ width: 100 }} onChange={this.onChange3}>
					{Dataprovince&&Dataprovince.map((one,index) => <Option value={one.codeid+''} key={'province'+index}>{one.cityName}</Option>)}
				</Select>}

				{pubOrgId>7&&this.state.province&&<Select size="large" classsName='pull-left'　placeholder="请选择市"  value={this.state.city+''}  style={{ width: 100 }} onChange={this.onChange4}>
					{Dataprovince&&this.state.Datacity.map((one,index) => <Option value={one.codeid+''} key={'province'+index}>{one.cityName}</Option>)}
				</Select>}

				{pubOrgId>12&&this.state.province&&this.state.city&&<Select showSearch size="large" classsName='pull-left'　placeholder="请选择县"  value={this.state.county}  style={{ width: 100 }} onChange={this.onChange5}>
					{Dataprovince&&this.state.DataCounty.map((one,index) => <Option value={one.cityName} key={'cityName'+index}>{one.cityName}</Option>)}
				</Select>}

			</div>
		)
	}
})

export default SelectPublocation
