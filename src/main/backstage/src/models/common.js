// const navListmap={
//   ''
// }

export default {

  namespace: 'common',

  state: {
    breadcrumb:[
      {
        name:'首页',
        path:'/'
      }
    ],
    loading:false
  },

  subscriptions: {
    //根据location 初始化导航和面包
    setup({ dispatch, history }) {
      history.listen(location => {
        // if (location.pathname === '/users') {
          dispatch({
            type: 'changeBreadcrumb',
            payload:{
              breadcrumb:[
                {
                  name:'首页',
                  path:'/'
                },
                {
                  name:'数据统计',
                  path:location.pathname
                }
              ]
            }
          });
        // }
      });
    },
  },

  //处理副作用　异步请求yield put({type:'reducersname',payload:''})
  //call调用一个函数　put dispatch一个action  select 访问其他modal
  effects: {
    // *fetchRemote({ payload }, { select,call, put }) {
    // },
  },
  //修改state
  reducers: {
    changeBreadcrumb(state,action){
      return {...state,...action.payload}
    },
    showLoading(state) {
      return { ...state, loading: true };
    },
    hideLoading(state) {
      return { ...state, loading: false };
    },
    // fetch(state, action) {
    //   return { ...state, ...action.payload };
    // },
  },

}
