var React = require('react');
import {Form} from './Form'
var $=window.$;
var Guide1=React.createClass({
    _onSubmit:Form._onSubmit,
    hideLoading:Form._hideLoading,
    _onChange:Form._onChange,
    _formGroupClass: Form._formGroupClass,
    _reSet:Form._reSet,
    contextTypes: {
        router: React.PropTypes.object
    },
    getInitialState(){
        return({
          taskName:localStorage.getItem('taskName')||'',
          spiderId:localStorage.getItem('spiderId')||'',
          remark:localStorage.getItem('remark')||'',
          loading:false,
          errors:{}
    })},
    _create(){
        var data={
            'taskName':this.state.taskName,
            'spiderId':this.state.spiderId,
            'remark':this.state.remark
          }
          localStorage.setItem('taskName', this.state.taskName);
          localStorage.setItem('spiderId', this.state.spiderId);
          localStorage.setItem('remark', this.state.remark);
          data=JSON.stringify(data);
        return $.ajax({
          url:window.basePath+ 'sp/ch',
          type: 'POST',
          contentType:'application/json',
          data: data,
          beforeSend: function () {
            this.setState({loading: true});
          }.bind(this)
        })
    },
    _validate: function () {
        var errors = {}
        if(this.state.taskName == '') {
          errors.taskName = '先取个名字吧';
        }
        if(this.state.spiderId == '') {
          errors.spiderId = '请点击生成标识';
        }
        return errors;
    },
    _onSuccess: function (data) {
        var self=this;
        if(data.error==='ERROR'||data.data===null){
          window.toastr.error(data.cause);
        }else {
          window.toastr.success('已保存');
          this.hideLoading()
        }
    },
    _onError: function () {
        this.hideLoading()
        var message = '网络错误,请重试';
        window.toastr.error(message);

    },
    getSpiderId(){
      var self=this;
      $.get(window.basePath+'pinyin?word='+encodeURIComponent(self.state.taskName),function(data){
        self.setState({
          spiderId:data.data+'_'+new Date().getTime()
        })
      })
    },
    render(){
        return(
          <div>
          <form className='mgt10' onSubmit={this._onSubmit}>
            <div className={this._formGroupClass(this.state.errors.taskName)}>
                <label>任务名称</label>
                <input defaultValue={this.state.taskName} name='taskName' type='text' onChange={this._onChange} className='form-control' placeholder='请输入任务名称' />
                <span className='help-block'>{this.state.errors.taskName}</span>
                <a className='btn btn-success' onClick={this.getSpiderId}>生成任务标识</a>
            </div>
            <div className={this._formGroupClass(this.state.errors.spiderId)}>
                <label>任务标识  ( 请先输入名称后点击生成标识 )</label>
                <input readOnly='true' name='spiderId' type='text' value={this.state.spiderId} className='form-control'/>
                <span className='help-block'>{this.state.errors.spiderId}</span>
            </div>
            <div className={this._formGroupClass(this.state.errors.remark)}>
                <label>备注信息</label>
                <textarea defaultValue={this.state.remark} rows='6' name='remark' onChange={this._onChange} className='form-control' placeholder='请输入备注' />
                <span className='help-block'>{this.state.errors.remark}</span>
            </div>
            <button style={{paddingLeft:'100px',paddingRight:'100px'}} className='btn btn-success mgl10 pull-right'  disabled={this.state.loading} type='submit'>{this.state.loading?'正在提交':'　保　存　'}</button>
          </form>
          </div>
          )
    }
})
module.exports = Guide1
