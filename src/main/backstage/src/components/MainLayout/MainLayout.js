
import React, { PropTypes } from 'react';
import { connect } from 'dva';
import './MainLayout.css';
import { Icon,BackTop,Row, Col,Spin } from 'antd';

import Breadcrumb from '../Public/Breadcrumb'
import AsideNav from '../Public/AsideNav'
import TopNav from '../Public/TopNav'

// 组件本身

class MainLayout extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      collapse: false
    };
    this.onCollapseChange=this.onCollapseChange.bind(this)
  }
  onCollapseChange() {
    this.setState({
      collapse: !this.state.collapse,
    })
  }
  componentWillUpdate(nextprops){
    if (nextprops.location.pathname!==this.props.location.pathname) {
      this.props.dispatch({
        type:'common/showLoading'
      })
    }
  }
  componentDidUpdate(nextprops){
    if(this.props.common.loading===true){
      this.props.dispatch({
        type:'common/hideLoading'
      })
    }
  }
  render() {
    const collapse = this.state.collapse;
    const common = this.props.common;
    const location = this.props.location;
    return (
      <div className={collapse ? "ant-layout-aside ant-layout-aside-collapse" : "ant-layout-aside"}>
        <aside className="ant-layout-sider">
          <div className="ant-layout-logo">
            <h3>{collapse?<Icon type="laptop" />:'政查查后台管理系统'}</h3>
          </div>
          <AsideNav current={location.pathname} collapse={this.state.collapse}/>
          <div className="ant-aside-action" onClick={this.onCollapseChange}>
            {collapse ? <Icon type="right" /> : <Icon type="left" />}
          </div>
        </aside>
        <div className="ant-layout-main">
          <div className="ant-layout-header">
            <Row type="flex" justify="space-between">
              <Col className="ant-layout-breadcrumb"  span={8}  order={1}>
                <Breadcrumb data={common.breadcrumb}/>
              </Col>
              <Col  span={8}  order={2}>
                <TopNav/>
              </Col>
            </Row>
          </div>
          <div className="ant-layout-container">
            <div className="ant-layout-content">
              <Spin size="large" spinning={common.loading}>
                {this.props.children||'页面丢了，点点左面的导航吧'}
              </Spin>
            </div>
          </div>
          <div className="ant-layout-footer">
            <p>gpdata.com © 2015　东华光普大数据版权所有</p>
          </div>
        </div>
        <BackTop visibilityHeight='100' />
      </div>
    );
  }
}
// 传入数据检测
MainLayout.propTypes = {};

// 监听属性，建立组件和数据的映射关系
function mapStateToProps({common}) {
  return {common};
}
// 关联 model
export default connect(mapStateToProps)(MainLayout);
