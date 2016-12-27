import React from 'react';
import { Menu,Icon,Input } from 'antd';
const SubMenu=Menu.SubMenu;
const Search = Input.Search;

const AsideNav=({collapse,current})=>{
    return (
      <Menu style={{display:'flex',justifyContent: "flex-end"}} onClick={() => {return false}}
        mode="horizontal">
        <Menu.Item key="search">
          <Search  placeholder="请输入关键字" onSearch={value => console.log(value)} />
        </Menu.Item>
        <Menu.Item key="front">
          <a href="http://www.gpdata.cn" target="_blank" rel="noopener noreferrer"><Icon type="home" />去往前台</a>
        </Menu.Item>
        <SubMenu title={<span><Icon type="user" />用户中心</span>}>
            <Menu.Item key="logout">退出</Menu.Item>
        </SubMenu>
      </Menu>
    )
}

export default AsideNav
