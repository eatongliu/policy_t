import React, { PropTypes } from 'react';
import { Router, Route, IndexRoute, Link,Redirect } from 'dva/router';
import MainLayout from './components/MainLayout/MainLayout';


// 系统管理
const Sys1 = (location, callback) => {
  require.ensure([], require => {callback(null,
    require('./routes/Sys1'))}, 'Sys1')
};
const Sys2 = (location, callback) => {
  require.ensure([], require => {callback(null,
    require('./routes/Sys2'))}, 'Sys2')
};
const Sys3 = (location, callback) => {
  require.ensure([], require => {callback(null,
    require('./routes/Sys3'))}, 'Sys3')
};
// const Sys4 = (location, callback) => {
//   require.ensure([], require => {callback(null,
//     require('./routes/Sys4'))}, 'Sys4')
// };

// 用户管理
// const User1 = (location, callback) => {
//   require.ensure([], require => {callback(null,
//     require('./routes/User1'))}, 'User1')
// };
// const User２ = (location, callback) => {
//   require.ensure([], require => {callback(null,
//     require('./routes/User２'))}, 'User２')
// };

// 任务管理
// const Task1 = (location, callback) => {
//   require.ensure([], require => {callback(null,
//     require('./routes/Task1'))}, 'Task1')
// };
const Task2 = (location, callback) => {
  require.ensure([], require => {callback(null,
    require('./routes/Task2'))}, 'Task2')
};
const Task3 = (location, callback) => {
  require.ensure([], require => {callback(null,
    require('./routes/Task3'))}, 'Task3')
};

// 订阅服务
const Sub1 = (location, callback) => {
  require.ensure([], require => {callback(null,
    require('./routes/Sub1'))}, 'Sub1')
};
const Sub2 = (location, callback) => {
  require.ensure([], require => {callback(null,
    require('./routes/Sub2'))}, 'Sub2')
};
const Sub3 = (location, callback) => {
  require.ensure([], require => {callback(null,
    require('./routes/Sub3'))}, 'Sub3')
};

// 财务
// const Fina1 = (location, callback) => {
//   require.ensure([], require => {callback(null,
//     require('./routes/Fina1'))}, 'Fina1')
// };
// const Fina２ = (location, callback) => {
//   require.ensure([], require => {callback(null,
//     require('./routes/Fina２'))}, 'Fina２')
// };



export default function({ history }) {
  return (
    <Router history={history}>
      <Redirect from="/" to="sys/1" />
      <Route path="/" component={MainLayout}>
        {/* <IndexRoute getComponent={Sys1}/> */}
        <Route path="sys/1" getComponent={Sys1} />
        <Route path="sys/2" getComponent={Sys2} />
        <Route path="sys/3" getComponent={Sys3} />
        {/* <Route path="sys/4" getComponent={Sys4} />

        <Route path="user/1" getComponent={User1} />
        <Route path="user/2" getComponent={User2} /> */}

        <Route path="task/2" getComponent={Task2} />
        <Route path="task/3" getComponent={Task3} />

        <Route path="sub/1" getComponent={Sub1} />
        <Route path="sub/2" getComponent={Sub2} />
        <Route path="sub/3" getComponent={Sub3} />

        {/* <Route path="fina/1" getComponent={Fina1} />
        <Route path="fina/2" getComponent={Fina2} /> */}
      </Route>
    </Router>
  );
};
