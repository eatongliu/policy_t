//引入css
var React = require('react');

window.$ = require('jquery/dist/jquery.js')
import 'antd/dist/antd.css';
var $ = window.$;
import {Table,Modal,Input} from 'antd';
var API_BATHPATH = require('../../config').default;
var getLocalTime=require('../Utils.js').getLocalTime
const confirm = Modal.confirm;
// 表头配置
const columns = [
    {
        title: '序号',
        dataIndex: 'favorId',
        key: 'favorId'
    }, {
        title: '收藏日期',
        dataIndex: 'createDate',
        key: 'createDate',
        sorter: true,
        render: date => getLocalTime(date)
    },{
        title: '政策文章',
        dataIndex: 'pdName',
        key: 'pdName'
    },
    {
        title: '主题',
        dataIndex: 'topicClassify',
        key: 'topicClassify'
    },
    {
        title: '文章类型',
        dataIndex: 'index_',
        key: 'index_',
        render:(te,record)=>{
          var ch;
          switch (record.index_) {
            case 'zcfg':
              ch='政策法规'
              break;
              case 'zcwj':
                ch='政策文件'
                break;
                case 'zcjd':
                  ch='政策解读'
                  break;
                  case 'jyta':
                    ch='建议提案'
                    break;
            default:ch=record.index_
          }
          return ch
        }
    },{
        title: '操作',
        key:'action',
        render: (text, record) => (
          <span>
            <a href={"./article.html?pdId="+record.pdId}>查看</a>
            <span className="ant-divider" />
            <a onClick={((record)=>{
            confirm({
              title: '确定取消收藏?',
              content: '',
              onOk() {
                $.ajax({
                    url: API_BATHPATH + 'user/fa/d/'+record.favorId,
                    method: 'delete'
                }).then((data) => {
                    if (data.status==='SUCCESS') {
                        window.location.reload()
                    }
                });
              },
              onCancel() {}
            });
          }).bind(null,record)}>删除</a>
          </span>
        )
    }
];
var User5 = React.createClass({
    getInitialState() {
        return {
          data: [],
          pagination: {
            page:1,
            pageSize:10
          },
          pdName:'',
          loading: false};
    },
    handleTableChange(pagination, filters, sorter) {
        const pager = this.state.pagination;
        pager.current = pagination.current
        this.setState({pagination: pager});
        this.fetch({
            pageSize: pagination.pageSize,
            page: pagination.current,
            sortField: sorter.field,
            sortOrder: sorter.order,
            ...filters
        });
    },
    componentDidMount() {this.fetch();},
    fetch(params = this.state.pagination) {
        const {pageSize,page}=params
        console.log(params)
        this.setState({loading: true});
        $.ajax({
            url: API_BATHPATH + 'user/fa/ls/'+pageSize*(page-1)+'/'+pageSize+'?pdName='+this.state.pdName,
            method: 'get'
        }).then((data) => {
            const pagination = this.state.pagination;
            pagination.total = data.data.total;
            this.setState({loading: false, data: data.data.rows, pagination});
        });
    },
    handleChange(e){
      this.setState({
        pdName:e.target.value
      })
    },
    render() {
        return (
        <div>
          <h3 className='pat'>政策收藏</h3>
          <div style={{height:'30px'}}>
            <Input
              type="text"
              style={{ width: 120 ,float:'right'}}
              value={this.state.pdName}
              onChange={this.handleChange}
              placeholder='搜索政策文章标题'
              onPressEnter={this.fetch.bind(this,this.state.pagination)}
            />
          </div>
          <Table className='mart' columns={columns} rowKey={record => record.favorId} dataSource={this.state.data} pagination={this.state.pagination} loading={this.state.loading} onChange={this.handleTableChange}/>
        </div>
        );
    }
})

export default User5
