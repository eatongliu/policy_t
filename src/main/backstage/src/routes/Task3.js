//引入css
var React = require('react');

window.$ = require('jquery/dist/jquery.js')
import 'antd/dist/antd.css';
var $ = window.$;
import {Table,Modal, Button,Alert,Input} from 'antd';
const confirm = Modal.confirm;
function getLocalTime(nS) {
    if(nS == null || nS == '') return '-';
    return new Date(parseInt(nS)).toLocaleDateString()
}
// 表头配置
const columns = [
    {
        title: '文件编号',
        dataIndex: 'pdId',
        key: 'pdId',
    },   {
          title: '文件名称',
          dataIndex: 'pdName',
          key: 'pdName',
      },   {
            title: '分类',
            dataIndex: 'topicClassify',
            key: 'topicClassify',
        },   {
              title: '颁布机构',
              dataIndex: 'pubOrg',
              key: 'pubOrg',
          },   {
                title: '具体单位',
                dataIndex: 'placed',
                key: 'placed',
            },   {
                  title: '成文日期',
                  dataIndex: 'createDate',
                  key: 'createDate',
              },   {
                    title: '成文年份',
                    dataIndex: 'createYear',
                    key: 'createYear',
                },   {
                      title: '发文字号',
                      dataIndex: 'issuedNum',
                      key: 'issuedNum',
                  },   {
                        title: '链接',
                        dataIndex: 'link',
                        key: 'link',
                    },   {
                          title: '是否有效',
                          dataIndex: 'isEffect',
                          key: 'isEffect',
                      },   {
                            title: '是否试点',
                            dataIndex: 'isPilot',
                            key: 'isPilot',
                        },   {
                              title: '是否发布',
                              dataIndex: 'isPub',
                              key: 'isPub',
                          },   {
                                title: '是否隐藏',
                                dataIndex: 'isHide',
                                key: 'isHide',
                          }, {
        title: '地区',
        dataIndex: 'province',
        key: 'province',
        render:(text,re)=>(
          re.province+re.city+re.county
        )
    },{
        title: 'ES索引 ',
        dataIndex: 'esIndex',
        key: 'esIndex',
    },{
        title: 'ES类型',
        dataIndex: 'esType',
        key: 'esType',
    },{
        title: 'ESID',
        dataIndex: 'esId',
        key: 'esId',
    },{
        title: '操作',
        key:'action',
        render: (text, record) => (
          <StartTask record={record}/>
        ),
    }
];

// 操作
class StartTask extends React.Component{
  constructor(props) {
    super(props);
  }
  handleOk=()=> {
      var self=this;
    confirm({
      title: '确定要停止吗?',
      content: '停止了需要重新虫管理页重新开启一个任务',
      onOk() {
        $.ajax({
            url:`${window.basePath}manager/status/${self.props.record.id}/stopping`,
            method: 'PUT'
        }).then((data) => {
            if (data.status==='SUCCESS') {
              window.location.reload()
            }
        });
      },
      onCancel() {},
  });
}
  render(){
    return <span>
      <a onClick={this.handleOk}></a>
    </span>
  }
}

var TaskList = React.createClass({
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
            url: window.basePath+'admin/policydoc?limit='+pageSize+'&offset='+pageSize*(page-1),
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
          <div style={{height:'30px'}}>
            <Input
              type="text"
              style={{ width: 120 ,float:'right'}}
              value={this.state.keyWord}
              onChange={this.handleChange}
              placeholder='搜索'
              onPressEnter={this.fetch.bind(this,this.state.pagination)}
            />
          </div>
          <Table className='mart' columns={columns} rowKey={record => record.esId} dataSource={this.state.data} pagination={this.state.pagination} loading={this.state.loading} onChange={this.handleTableChange}/>
        </div>
        );
    }
})

export default TaskList
