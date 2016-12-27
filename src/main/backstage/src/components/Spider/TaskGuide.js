var React = require('react');
import Guide1 from './Guide1'
import Guide2 from './Guide2'
import Guide3 from './Guide3'
import Guide4 from './Guide4'

var Guidecontent=React.createClass({
	render(){
		var spiderId=this.props.spiderId
		console.log(spiderId)
		var guide;
		switch(this.props.id){
			case 0:guide=<Guide1/>;
			break;
			case 1:guide=<Guide2 spiderId={spiderId}/>;
			break;
			case 2:guide=<Guide3 spiderId={spiderId}/>;
			break;
			case 3:guide=<Guide4 spiderId={spiderId}/>;
			break;
		}
		return (
				<div  className='mgt10'>
				{guide}
				</div>
			)
	}
})

var TaskGuide=React.createClass({
	render(){
		var id=this.props.id
		var spiderId=this.props.spiderId
		return (
			<div  className="panel panel-default">
			<div className="panel-body">
				<Guidecontent spiderId={spiderId} id={id}/>
			</div>
			</div>
			)
	}
})
module.exports = TaskGuide
