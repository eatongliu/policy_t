export default {

  namespace: 'task1',

  state: {
    current:0,
    steps: [{
      title: '基本属性',
      // description:'设置名称等信息',
      content: 'First-content',
    }, {
      title: '爬取来源',
      // description:'配置爬去来源',
      content: 'Second-content',
    }, {
      title: '爬取策略',
      // description:'配置爬取策略等信息',
      content: 'Last-content',
    },{
      title: '解析策略',
      // description:'配置数据解析策略',
      content: 'Last-content',
    }]
  },

  subscriptions: {
  },

  //处理副作用　异步请求yield put({type:'reducersname',payload:''})
  //call调用一个函数　put dispatch一个action  select 访问其他modal
  effects: {
    // *fetchRemote({ payload }, { select,call, put }) {
    // },
  },
  //修改state
  reducers: {
    showLoading(state) {
      return { ...state, loading: true };
    },
    hideLoading(state) {
      return { ...state, loading: false };
    },
    next(state) {
      return { ...state,current:state.current+1}
    },
    prev(state) {
      return { ...state,current:state.current-1}
    }
    // fetch(state, action) {
    //   return { ...state, ...action.payload };
    // },
  },

}
