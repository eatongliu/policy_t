import React from 'react';
import { Menu,Icon } from 'antd';
import { Link } from 'dva/router'
import {getUrlParent} from '../../utils/stringUtil';

const SubMenu=Menu.SubMenu;

const AsideNav=({collapse,current})=>{
    return (
      <Menu
        mode={!collapse?"inline":"vertical"}
        theme="dark"
        defaultSelectedKeys={[current]}
        defaultOpenKeys={[getUrlParent(current)]}
        selectedKeys={[current]}>
        <SubMenu key="sys"
          title={<span><Icon type="setting" /> {!collapse && <span>系统管理</span>}</span>}>
          <Menu.Item key="/sys/1"><Link to="/sys/1">数据统计</Link></Menu.Item>
          <Menu.Item key="/sys/2"><Link to="/sys/2">权限分配</Link></Menu.Item>
          <Menu.Item key="/sys/3"><Link to="/sys/3">站点配置</Link></Menu.Item>
          <Menu.Item key="/sys/4"><Link to="/sys/4">系统消息</Link></Menu.Item>
        </SubMenu>

        <SubMenu key="user" title={<span><Icon type="user" />{!collapse && <span>用户管理</span>}</span>}>
          <Menu.Item key="/user/1"><Link to="/user/1">用户列表</Link></Menu.Item>
          <Menu.Item key="/user/2"><Link to="/user/2">消息列表</Link></Menu.Item>
        </SubMenu>

        <SubMenu key="task" title={<span><Icon type="calendar" />{!collapse && <span>任务管理</span>}</span>}>
          {/* <Menu.Item key="/task/1"><Link to="/task/1">创建任务</Link></Menu.Item> */}
          <Menu.Item key="/task/2"><Link to="/task/2">任务列表</Link></Menu.Item>
          <Menu.Item key="/task/3"><Link to="/task/3">文件列表</Link></Menu.Item>
        </SubMenu>

        <SubMenu key="sub" title={<span><Icon type="area-chart" />{!collapse && <span>订阅服务</span>}</span>}>
          <Menu.Item key="/sub/1"><Link to="/sub/1">订阅申请</Link></Menu.Item>
          <Menu.Item key="/sub/2"><Link to="/sub/2">订阅列表</Link></Menu.Item>
          <Menu.Item key="/sub/3"><Link to="/sub/3">推送详情</Link></Menu.Item>
        </SubMenu>

        <SubMenu key="fina" title={<span><Icon type="pay-circle-o" />{!collapse && <span>财务管理</span>}</span>}>
          <Menu.Item key="/fina/1"><Link to="/fina/1">平台收支</Link></Menu.Item>
          <Menu.Item key="/fina/2"><Link to="/fina/2">统计报表</Link></Menu.Item>
        </SubMenu>
      </Menu>
    )
}

export default AsideNav
