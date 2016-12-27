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

var Guide2=React.createClass({
    getInitialState(){
        return ({
            url:localStorage.getItem('url')||'http://',
            scope:JSON.parse(localStorage.getItem('scope'))||[],
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
      console.info(this.state.url)
        localStorage.setItem('url',this.state.url);
        localStorage.setItem('scope',JSON.stringify(this.state.scope));
        var self=this
        this.showLoading();
        var spiderId =localStorage.getItem('spiderId')||'';
        if (spiderId==='') {
          window.toastr.error('请先保存基本属性')
          return false;
        }
        if (this.state.scope==='') {
          window.toastr.error('请先选择范围')
          return false;
        }
        var data={
            "crawl": {
                "source": [{
                    "url": this.state.url,
                    "scope": {
                        "detail": [
                        this.state.scope.caption.map(function(one,index){
                            return {
                                capftion:one,
                                xpath:self.state.scope.xpath[index]
                            }
                        })
                        ]
                    }
                }]
            }
        }
        data={crawlscopejson:JSON.stringify(data)};
        $.ajax({
            url:window.basePath+'sp/cs/'+spiderId,
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
                window.toastr.success('成功')
            },
            error:function(){
                self.setState({
                    loading:false
                })
            }
        })
    },
    down(){
      PubSub.emit('GET_PAGE',{url:'/fhps?url='+encodeURIComponent(this.state.url)})
    },
    handleChangeUrl(e){
      var state = {};
      state[e.target.name] =  $.trim(e.target.value);
      this.setState(state)
    },
    getUrl(obj,index){
            // alert('getUrl');
        var url=this.state.url;
        if (obj.url) url[index]=obj.url;
        this.setState({url:url})
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
        PubSub.on('GET_SCOPE',function(data){
          console.info('接收到的',data);
            var scope={
                caption:data.caption,
                xpath:data.xpath
            }
            self.setState({
                scope:scope
            })
        })
        PubSub.on('GET_PAGE',function(data){
            var url=data.url;
            self.getPage(url);
        })
    },
    componentWillMount(){
        PubSub.removeAllListeners('GET_SCOPE');
        PubSub.removeAllListeners('GET_PAGE');
    },
    componentWillunMount(){
        PubSub.removeAllListeners('GET_SCOPE');
        PubSub.removeAllListeners('GET_PAGE');
    },
    render(){
        var self=this;
        return (
        <div>
            <div className='col-lg-12'>
                <div className={self.state.urlNow===self.state.url?'hveee clearfix w10 mgt10 pd2':'clearfix w10 mgt10 pd2'}>
                    <input style={{width:'60%'}} name='url' className='form-control pull-left' onChange={this.handleChangeUrl} value={this.state.url}/>
                    <div className='pull-left' style={{width:'10%'}}>
                       <button onClick={this.down} className='btn btn-success pull-left mgl5'><span className='glyphicon glyphicon-cloud-download'></span></button>
                    </div>
                </div>
            </div>
            <div className='col-lg-3 bdr1 mgt30' style={{height:$(window).height()-400,overflow:'scroll'}}>
                <div>
                    <div className='w10'>
                        <Scope scope={this.state.scope} urlNow={this.state.urlNow}/>
                    </div>
                </div>
            </div>
            <div className='col-lg-9 mgt30'>
                <iframe width='100%'  name='frame' id='frame' frameBorder='0' style={{height:($(window).height()-400),display:this.state.urlNow==''?'none':'block'}} className='w10'></iframe>
            </div>
            <div className='col-lg-12'>
                <button onClick={this.handleSubmit} style={{paddingLeft:'100px',paddingRight:'100px'}} className='btn btn-success mgl10 pull-right'  disabled={this.state.loading} type='submit'>{this.state.loading?'正在提交':'保存'}</button>
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
var Scope=React.createClass({
    getInitialState(){
        return({
            xpath:[],
            caption:[]
        })
    },
    componentDidMount(){
        PubSub.on('GET_XPATH',function(data){
            var xpath=this.state.xpath,caption=this.state.caption;
            xpath.push(data);
            caption.push('')
            modal('show');
            this.setState({xpath:xpath,caption:caption});
        }.bind(this))
    },
    componentWillMount(){
        PubSub.removeAllListeners('GET_XPATH');
    },
    componentWillunMount(){
        PubSub.removeAllListeners('GET_XPATH');
    },
    del(e,i){
      var xpath=this.state.xpath,caption=this.state.caption;
      function rmIndex(arr){
        if (i > -1)arr.splice(i, 1);
        return arr;
      }
      console.info('要发送的',{xpath:rmIndex(xpath),caption:rmIndex(caption)});
      PubSub.emit('GET_SCOPE',{xpath:rmIndex(xpath),caption:rmIndex(caption)})
      this.setState({xpath:rmIndex(xpath),caption:rmIndex(caption)})
    },
    render(){
        if (this.state.caption!==undefined) {
            return (
            <div>
            <ul>
                <li className='row'>
                    <div className='col-lg-3'>  名称</div>
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
        var state=this.state.caption;
        state[state.length-1]=name;
        PubSub.emit('GET_SCOPE',{xpath:this.state.xpath,caption:state})
        this.setState({caption:state});
    }
})
module.exports = Guide2
