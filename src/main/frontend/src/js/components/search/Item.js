import ReactEcharts from 'echarts-for-react';
require('echarts-wordcloud');
var React = require('react');

var Item = React.createClass({
    render() {
			var data=this.props.data
        return (
            <div className="col-lg-4 col-md-6 col-xs-12 boxshaw">
                <div className='bor-box'>
                    <a href={'./article.html?pdId='+data.pdId}><Echart1 optionData={data.tags}/></a>
                    <p style={{
                        textAlign: 'center',
                        margin: '5px 0 0 0'
                    }}><a href={'./article.html?pdId='+data.pdId}>{data.pdName}</a></p>
                    <p style={{
                        margin: '0'
                    }}>
                        <a href={'./article.html?pdId='+data.pdId} className='one_a'>{data.createDate}</a>
                        <a className='two_a'>0</a>
                        <a className='three_a'>0</a>
                    </p>
                </div>
            </div>
        )
    }
})
var Echart1 = React.createClass({
    getOption() {
				var optionData=this.props.optionData&&this.props.optionData.map(function(one,index){
					return {name:one.tag,value:one.weight}
				})
        var option = {
            series: [
                {
                    type: 'wordCloud',
                    gridSize: 2,
                    sizeRange: [
                        12, 50
                    ],
                    rotationRange: [
                        -90, 90
                    ],
                    shape: 'pentagon',
                    width: 600,
                    height: 400,
                    textStyle: {
                        normal: {
                            color: function() {
                                return 'rgb(' + [
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160),
                                    Math.round(Math.random() * 160)
                                ].join(',') + ')';
                            }
                        },
                        emphasis: {
                            shadowBlur: 10,
                            shadowColor: '#333'
                        }
                    },
                    data: optionData
                }
            ]
        };
        return option
    },
    onChartReady: function(chart) {
        chart.hideLoading();
    },
    render() {
        return (
            <div className='' style={{
                width: '100%'
            }}>
                <ReactEcharts option={this.getOption()} onChartReady={this.onChartReady} showLoading={false} style={{
                    width: '100%',height:'240px',cursor:'pointer'
                }}/>
            </div>
        )
    }
})

export default Item
