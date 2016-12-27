//引入css
var React = require('react');

window.$ = require('jquery/dist/jquery.js')
import 'antd/dist/antd.css';
var $ = window.$;
import {Table,Modal, Button,Alert,Input} from 'antd';
var API_BATHPATH = require('../../config').default;
var getLocalTime=require('../Utils.js').getLocalTime
// 表头配置
const columns = [
  {
      title: '时间',
      dataIndex: 'qTime',
      key: 'qTime',
      sorter: true,
      render: date => getLocalTime(date)
  }, {
        title: '内容',
        dataIndex: 'answer',
        key: 'answer',
    },{
        title: '类型',
        dataIndex: 'qType',
        key: 'qType',
    },
    {
        title: '操作',
        key:'action',
        render: (text, record) => (
          <DeleteText record={record}/>
        ),
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
      visible: false,
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
      confirmLoading: true,
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
          url: API_BATHPATH + 'user/msg/d/'+self.props.record.id,
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
        <a onClick={this.showModal}>删除</a>
        <Modal title={"删除"}
                visible={this.state.visible}
                onOk={this.handleOk}
                confirmLoading={this.state.confirmLoading}
                onCancel={this.handleCancel}>
          <Alert
            message="注意"
            description={"您将要删除这条消息，删除不可恢复.请谨慎操作．\n 消息内容："+this.props.record.answer}
            type="warning"
            showIcon
          />
          <Input type='password' value={this.state.passwd} onChange={this.handleChange} placeholder="请输入您的用户密码确认删除" />
          <p>{this.state.ModalText}</p>
        </Modal>
    </span>
  }
}

var User６ = React.createClass({
    getInitialState() {
        return {
          data: [],
          pagination: {
            page:1,
            pageSize:10
          },
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
            url: API_BATHPATH + 'user/msg/ls',
            //pageSize*(page-1)+'/'+pageSize+'?sid=',
            method: 'get'
        }).then((data) => {
            const pagination = this.state.pagination;
            pagination.total = data.data.total;
            this.setState({loading: false, data: data.data.rows, pagination});
        });
    },
    render() {
        return (
        <div>
          <h3 className='pat'>消息提醒</h3>
          <Table className='mart' columns={columns} rowKey={record => record.id} dataSource={this.state.data} pagination={this.state.pagination} loading={this.state.loading} onChange={this.handleTableChange}/>
        </div>
        );
    }
})

export default User６
