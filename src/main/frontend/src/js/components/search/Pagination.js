// total:''
// dispatch({
// imit:'',offset:''
//})
//



//引入css
var React = require('react');
import { Pagination } from 'antd';

var API_BATHPATH=require('../../config').default;
var myPagination=React.createClass({
	getInitialState(){
		return ({
			options:[]
		})
	},
	onChange(pageNum){
		var limit=12;
		var offset=(pageNum-1)*limit
		this.props.dispatch({
			limit:12,
			offset:offset
		})
	},
	render(){
		var state=this.state;
		return(
			 <Pagination defaultPageSize={12} onChange={this.onChange} total={this.props.total} />
		)
	}
})

export default myPagination
