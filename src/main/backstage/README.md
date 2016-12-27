# 常用命令

- ### 安装
 `npm install`
- ### 启动devserver
 `npm start`
- ### 打包发布
 `npm run build`
- ### 清除dist(shell)
 `npm run clean`
- ### 运行测试
 `npm run test`


# 项目结构
> ├── /mock/           # 数据mock的接口文件  
> ├── /src/            # 项目源码目录   
> │ ├── /components/   # 项目组件  
> │ ├── /routes/       # 路由组件（页面维度）    
> │ ├── /models/       # 数据模型   
> │ ├── /services/     # 数据接口   
> │ ├── /utils/        # 工具函数     
> │ ├── route.js       # 路由配置   
> │ ├── index.js       # 入口文件   
> │ ├── index.less      
> │ └── index.html         
> ├── package.json     # 项目信息     
> └── proxy.config.js  # 数据mock配置   

# 组建设计
- Container Component:监听数据，绑定modal，容器组件

      import React, { Component, PropTypes } from 'react';

      // dva 的 connect 方法可以将组件和数据关联在一起
      import { connect } from 'dva';

      // 组件本身
      const MyComponent = (props)=>{};
      MyComponent.propTypes = {};

      // 监听属性，建立组件和数据的映射关系
      function mapStateToProps(state) {
        return {...state.data};
      }

      // 关联 model
      export defaultconnect(mapStateToProps)(MyComponent;`
- Presentational Component:无状态组件,展示组件,props传递

      import React, { Component, PropTypes } from 'react';

      // 组件本身
      // 所需要的数据通过 Container Component 通过 props 传递下来
      const MyComponent = (props)=>{}
      MyComponent.propTypes = {};

      // 并不会监听数据
      export default MyComponent;

# 组件写法

    // １. es6 的写法（生命周期）
    class App extends React.Component({});
    // ２. stateless 的写法（推荐写法）
    const App = (props) => ({});
