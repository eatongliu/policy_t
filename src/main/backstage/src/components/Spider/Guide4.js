var React = require('react');
import PubSub from './PubSub';
window.PubSub=PubSub;
var $=window.$;

function modal(status){
  if (status==='show') {
    $('.modal').removeClass('fade');
    $('.modal').css('display','block');
  }
  if (status==='hide') {
    $('.modal').addClass('fade');
    $('.modal').css('display','none');
  }
}
var Guide4=React.createClass({
    contextTypes: {
        router: React.PropTypes.object
    },
    getInitialState(){
        return ({
            url:localStorage.getItem('url1')||'http://',
            url2:localStorage.getItem('url2')||'http://',
            field:JSON.parse(localStorage.getItem('field'))||[],
            urlNow:'',
            loading:false
        })
    },
    showLoading(){
        this.setState({
            loading:true
        })
    },
    handleSubmit(){
      localStorage.setItem('url1',this.state.url)
      localStorage.setItem('url2',this.state.url2)
      localStorage.setItem('field',JSON.stringify(this.state.field))

      console.log(this.state.field)
      if(!this.state.field.caption){
          window.toastr.error('还未选择字段')
          return false
      }
        var self=this
        var spiderId =localStorage.getItem('spiderId')||'';
        if (spiderId==='') {
          window.toastr.error('请先保存基本属性')
          return false;
        }
        var data={
                    'parse': {
                        'page': [{
                            'parseUrl':this.state.url2,
                            'field': [
                            this.state.field.caption.map(function(one,index){
                                return {
                                    caption:one,
                                    name:self.state.field.name[index],
                                    xpath:self.state.field.xpath[index]
                                }
                            })
                            ]
                        }]
                    }
                }
        this.showLoading();
        data={parsejson:JSON.stringify(data)};
        $.ajax({
            url:window.basePath+'sp/zip/'+spiderId,
            type:'post',
            data:data,
            success:function(data){
              self.setState({
                  loading:false
              })
                if(data.status==='ERROR'){
                    window.toastr.error(data.cause)

                    return false
                }
                if(data.status==='SUCCESS'){

                window.toastr.error('是否前往任务中心调度您的爬虫</br><span class="btn btn-default btn-sm text-center">确定</span>','发布成功',{
                    progressBar:true,
                    preventDuplicate:true,
                    closeButton:true,
                    timeOut:8000,
                    onclick:function(){
                        self.context.router.push('task/2');
                        return false
                    },
                    onHidden:function(){
                        self.context.router.push('task/2');
                    }
                });
                localStorage.clear()

              }
            },
            error:function(){
                self.setState({
                    loading:false
                })
            }
        })
    },
    down(){
      PubSub.emit('GET_PAGE',{url:'/fhps?url='+this.state.url})
    },
    handleChangeUrl(e){
      var state = {};
      state[e.target.name] =  $.trim(e.target.value);
      this.setState(state)
    },
    getPage(url){
        window.toastr.warning('loading···','页面分析中请稍后',{
            closeHtml:'',
            timeOut:80000000,
            extendedTimeOut:8000000
        });
        var iframe = document.getElementById('frame');
        iframe.src = url ;
        // 插入js
        // iframe加载完毕
        if (iframe.attachEvent){
            iframe.attachEvent('onload', function(){loadFrame()});
        } else {
            iframe.onload = function(){loadFrame()};
        }
        function loadFrame(){
            try{
                var frame=iframe.contentWindow.document;
                var jq=document.createElement('script');
                jq.setAttribute('src','http://lib.sinaapp.com/js/jquery/1.9.1/jquery-1.9.1.min.js');
                  frame.body.appendChild(jq);
                  // jq加载完毕
                  jq.onload=function(){
                    var script=document.createElement('script');
                    script.setAttribute('src','/admin/GetXpath.js');
                    frame.body.appendChild(script);
                  }
                  frame.close();
                  window.toastr.clear();
                  window.toastr.success('准备就绪，点击页面板块，添加抓取范围')
            }
            catch(err){
              window.toastr.clear();
              window.toastr.error('页面已经加载，但初始化失败，请重试')
            }
        }
        this.setState({urlNow:url})
    },
    componentDidMount(){
        var self=this;
        PubSub.on('GET_FIELD',function(data){
          console.info('接收到的',data);
            var field={
                caption:data.caption,
                xpath:data.xpath,
                name:data.name
            }
            self.setState({
                field:field
            })
        })
        PubSub.on('GET_PAGE',function(data){
            var url=data.url;
            self.getPage(url);
        })
    },
    componentWillMount(){
        PubSub.removeAllListeners('GET_FIELD');
        PubSub.removeAllListeners('GET_PAGE');
    },
    componentWillunMount(){
        PubSub.removeAllListeners('GET_FIELD');
        PubSub.removeAllListeners('GET_PAGE');
    },
    render(){
        var self=this;
        return (
        <div>
            <div className='col-lg-12'>
                <div className={self.state.urlNow===self.state.url?'hveee clearfix w10 mgt10 pd2':'clearfix w10 mgt10 pd2'}>
                    <div style={{width:'60%'}} className='input-group pull-left'>
                        <div className='input-group-addon'>示例url</div>
                        <input name='url' className='form-control' onChange={this.handleChangeUrl} value={this.state.url}/>
                    </div>

                    <div className='pull-left' style={{width:'10%'}}>
                       <button onClick={this.down} className='btn btn-success pull-left mgl5'><span className='glyphicon glyphicon-cloud-download'></span></button>
                    </div>
                    <div className='input-group pull-left mgt10'>
                        <div className='input-group-addon'>url正则</div>
                    <input style={{width:'60%'}} name='url2' className='form-control ' onChange={this.handleChangeUrl} value={this.state.url2}/>
                    </div>
                </div>
            </div>
            <div className='col-lg-3 bdr1 mgt30' style={{height:$(window).height()-400,overflow:'scroll'}}>
                <div>
                    <div className='w10'>
                        <Field field={this.state.field} urlNow={this.state.urlNow}/>
                    </div>
                </div>
            </div>
            <div className='col-lg-9 mgt30'>
                <iframe width='100%' name='frame' id='frame' frameBorder='0' style={{height:($(window).height()-240)/3*2,display:this.state.urlNow==''?'none':'block'}} className='w10'></iframe>
            </div>
            <div className='col-lg-12'>
                <button onClick={this.handleSubmit} style={{paddingLeft:'100px',paddingRight:'100px'}} className='btn btn-success mgl10 pull-right'  disabled={this.state.loading} type='submit'>{this.state.loading?'正在提交':'完 成'}</button>
            </div>
        </div>)
    }
})

var ModalchangeName=React.createClass({
    handleRename(e){
        this.setState({caption:e.target.value})
    },
    getInitialState:()=>({caption:''}),
    handleConfirm(){
        modal('hide');
        this.props.getScopname(this.state.caption)
        // 清除caption
        this.setState({caption:''})
    },
    render(){
        return (
            <div className='modal fade' id='ModalchangeName' role='dialog' >
              <div className='modal-dialog modal-sm'>
                 <div className='modal-content'>
                    <div className='modal-header'>
                      <button type='button' className='close' data-dismiss='modal'>
                       <span aria-hidden='true'>&times;</span>
                      </button>
                      <h4 className='modal-title'>填写名称</h4>
                    </div>
                    <div className='modal-body'>
                        <input onChange={this.handleRename} className='form-control' value={this.state.caption}/>
                        <a className='btn btn-success  mgt10' onClick={this.handleConfirm}>保存</a>
                    </div>
                 </div>
              </div>
            </div>
    )}
})

var Field=React.createClass({
    getInitialState(){
        return({
            xpath:[],
            caption:[],
            name:[]
        })
    },
    componentDidMount(){
        PubSub.on('GET_XPATH',function(data){
            var xpath=this.state.xpath,caption=this.state.caption,name=this.state.name
            xpath.push(data);
            caption.push('');
            name.push('');
            modal('show');
            this.setState({xpath:xpath,caption:caption,name:name});
        }.bind(this))
    },
    componentWillMount(){
        PubSub.removeAllListeners('GET_XPATH');
    },
    componentWillunMount(){
        PubSub.removeAllListeners('GET_XPATH');
    },
    del(e,i){
      var xpath=this.state.xpath,caption=this.state.caption,name=this.state.name;
      function rmIndex(arr){
        if (i > -1)arr.splice(i, 1);
        return arr;
      }
      PubSub.emit('GET_FIELD',{xpath:rmIndex(xpath),caption:rmIndex(caption),name:rmIndex(caption)})
      this.setState({xpath:rmIndex(xpath),caption:rmIndex(caption),name:rmIndex(name)})
    },
    render(){
        if (this.state.caption!==undefined) {
            return (
            <div>
            <ul>
                <li className='row'>
                    <div className='col-lg-3'>名称</div>
                    <div className='col-lg-6'>Xpath</div>
                    <div className='col-lg-3' >操作</div>
                </li>
                {this.state.xpath.map(function(a,i){
                    return <li key={i} className='pd2 row bdb1'>
                        <div className='col-lg-3 lh24'>
                            <p>{this.state.caption[i]}</p>
                        </div>
                        <div className='col-lg-6 lh24' title={a} >
                            {a.length<30?a:a.slice(0,30)+'...'}
                        </div>
                        <div className='col-lg-3 lh24' >
                            <button  onClick={this.del.bind(null,a,i)} className='btn btn-xs btn-danger'>删除</button>
                        </div>
                    </li>
                }.bind(this))}
            </ul>
            <ModalchangeName getScopname={this.getScopname}/>
            </div>
            )
        }return (<div></div>)
    },
    getScopname(name){
        var self=this;
        $.get(window.basePath+'pinyin/'+name,function(data){
            var state=self.state.caption;
            state[state.length-1]=name;
            var state2=self.state.name;
            state2[state.length-1]=data.data;
            PubSub.emit('GET_FIELD',{xpath:self.state.xpath,caption:state,name:state2})
            self.setState({caption:state,name:state2});
        })
    }
})
module.exports = Guide4
