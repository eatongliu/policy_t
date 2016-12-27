//引入css
var React = require('react');

window.$ = require('jquery/dist/jquery.js')
import 'antd/dist/antd.css';
var $ = window.$;
import {Table,Modal,Alert,Input} from 'antd';
var API_BATHPATH = require('../../config').default;
var getLocalTime=require('../Utils.js').getLocalTime
// 表头配置
const columns = [
    {
        title: '序号',
        dataIndex: 'sid',
        key: 'sid'
    }, {
        title: '订阅号',
        dataIndex: 'sid',
        key: 'sid2'
    }, {
        title: '订阅日期',
        dataIndex: 'createDate',
        key: 'createDate',
        sorter: true,
        render: date => getLocalTime(date)
        // render: date => `${name.first} ${name.last}`
    }, {
        title: '字段集',
        dataIndex: 'keyWords',
        key: 'keyWords'
    }, {
        title: '需求',
        dataIndex: 'uRemark',
        key: 'uRemark'
    }, {
        title: '状态',
        dataIndex: 'status',
        key: 'status',
        filters: [
            {
                text: '待审核',
                key:'key1',
                value: '待审核'
            }, {
                text: '未通过',
                key:'key2',
                value: '未通过'
            },{
                text: '已订阅',
                key:'key3',
                value: '已订阅'
            }
        ]
    }, {
        title: '操作',
        key:'action',
        render: (text, record) => (
          <DeleteText record={record}/>
        )
    }
];

// 操作
class DeleteText extends React.Component{
  constructor(props) {
    super(props);
    this.state={
      visible:false,
      passwd:''
    }
  }
  showModal=()=>{
    this.setState({
      visible:true
    })
  }
  handleCancel=()=> {
    this.setState({
      visible: false
    });
  };
  handleChange=(e)=>{
    this.setState({
      passwd:e.target.value
    })
  }
  handleOk=()=> {
    var self=this;
    this.setState({
      ModalText: '验证密码中．．．',
      confirmLoading: true
    });
    $.ajax({
      url:API_BATHPATH + 'user/judgepwd?password='+this.state.passwd,
      method:'post',
      contentType:'application/json'
    }).then((data)=>{
      if (data.status==='ERROR') {
        this.setState({
          confirmLoading: false,
          ModalText:data.cause
        })
        return false
      }
      //删除
      this.setState({
        ModalText:'验证通过，即将删除'
      });
      $.ajax({
          url: API_BATHPATH + 'su/d/'+self.props.record.sid,
          method: 'delete'
      }).then((data) => {
          if (data.status==='SUCCESS') {
            self.setState({
              visible: false,
              confirmLoading: false,
              ModalText:'删除成功'
            },function(){
              window.location.reload()
            });
          }
      });

    })
  };

  render(){
    return <span>
      <a href={"#store?sid="+this.props.record.sid}>查看</a>
      <span className="ant-divider" />
      <a onClick={this.showModal}>删除</a>
        <Modal title={"删除编号为"+this.props.record.sid+"的服务"}
                visible={this.state.visible}
                onOk={this.handleOk}
                confirmLoading={this.state.confirmLoading}
                onCancel={this.handleCancel}>
          <Alert
            message="注意"
            description={"您将要删除您订阅的推送服务，删除意味着您将不收到我们的推送，并且不可恢复.请谨慎操作．\n "}
            type="warning"
            showIcon
          />
          <Input type='password' value={this.state.passwd} onChange={this.handleChange} placeholder="请输入您的用户密码确认删除" />
          <p>{this.state.ModalText}</p>
        </Modal>
    </span>
  }
}

var User4 = React.createClass({
    getInitialState() {
        return {
          data: [],
          pagination: {
            page:1,
            pageSize:10
          },
          keyWord:'',
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
        this.setState({loading: true});
        $.ajax({
            url: API_BATHPATH + 'su/ls/'+pageSize*(page-1)+'/'+pageSize+'?keyWord='+this.state.keyWord,
            method: 'get'
        }).then((data) => {
            const pagination = this.state.pagination;
            pagination.total = data.data.total;
            this.setState({loading: false, data: data.data.rows, pagination});
        });
    },
    handleChange(e){
      this.setState({
        keyWord:e.target.value
      })
    },
    render() {
        return (
        <div>
          <h3 className='pat'>订阅服务</h3>
          <div style={{height:'30px'}}>
            <Input
              type="text"
              style={{ width: 120 ,float:'right'}}
              value={this.state.keyWord}
              onChange={this.handleChange}
              placeholder='搜索关键词'
              onPressEnter={this.fetch.bind(this,this.state.pagination)}
            />
          </div>
          <Table className='mart' columns={columns} rowKey={record => record.sid} dataSource={this.state.data} pagination={this.state.pagination} loading={this.state.loading} onChange={this.handleTableChange}/>
        </div>
        );
    }
})

export default User4
