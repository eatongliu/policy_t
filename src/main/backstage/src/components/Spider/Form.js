var $=window.$;
var Form={
_onSubmit:function(e){
    e.preventDefault();
    var errors = this._validate();
    if(Object.keys(errors).length != 0) {
      this.setState({
        errors: errors
      });
      return;
    }
    var xhr = this._create();
    xhr.done(this._onSuccess)
    .fail(this._onError)
  },
_hideLoading:function(){
  this.setState({loading: false});
  },
_onChange:function(e){
  var state = {};
  state[e.target.name] =  $.trim(e.target.value);
  
  var errors=this.state.errors;
  var info='';
  if (state[e.target.name]=='') {
      info='请填写此项'
  }
  errors[e.target.name]=info
  state.errors=errors;

  this.setState(state);
},
_formGroupClass:function (field) {
  var className = 'form-group col-lg-12';
  if(field) {
    className += ' has-error'
  }
  return className;
},
_reSet(){
  this.refs.form.reset()
  this.setState(this.getInitialState());
},
_onError: function () {
  var message = '网络错误,请重试';
  window.toastr.error(message);
  this.setState({loading:false});
},
charset:['UTF-8',
    'armscii8 -- ARMSCII-8 Armenian',
    'ascii -- US ASCII',
    'big5 -- Big5 Traditional Chinese',
    'binary -- Binary pseudo charset',
    'cp1250 -- Windows Central European',
    'cp1256 -- Windows Arabic',
    'cp1257 -- Windows Baltic',
    'cp850 -- DOS West European',
    'cp852 -- DOS Central European',
    'cp866 -- DOS Russian',
    'cp932 -- SJIS for Windows Japanese',
    'dec8 -- DEC West European',
    'eucjpms -- UJIS for Windows Japanese',
    'euckr -- EUC-KR Korean',
    'gb2312 -- GB2312 Simplified Chinese',
    'gbk -- GBK Simplified Chinese',
    'geostd8 -- GEOSTD8 Georgian',
    'greek -- ISO 8859-7 Greek',
    'hebrew -- ISO 8859-8 Hebrew',
    'hp8 -- HP West European',
    'keybcs2 -- DOS Kamenicky Czech-Slovak',
    'koi8r -- KOI8-R Relcom Russian',
    'koi8u -- KOI8-U Ukrainian',
    'latin1 -- cp1252 West European',
    'latin2 -- ISO 8859-2 Central European',
    'latin5 -- ISO 8859-9 Turkish',
    'latin7 -- ISO 8859-13 Baltic',
    'macce -- Mac Central European',
    'macroman -- Mac West European',
    'sjis -- Shift-JIS Japanese',
    'swe7 -- 7bit Swedish',
    'tis620 -- TIS620 Thai',
    'ucs2 -- UCS-2 Unicode',
    'ujis -- EUC-JP Japanese',
    'utf16 -- UTF-16 Unicode',
    'utf32 -- UTF-32 Unicode',
    'utf8 -- UTF-8 Unicode',
    'utf8mb4 -- UTF-8 Unicode'
  ]
}
export  {Form}

