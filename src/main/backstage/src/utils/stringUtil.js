/**
 * 字符串操作工具包.
 *
 * @param  {ostring}        　　需要操作的字符串
 * @return {nstring}           返回的字符串
 */
export function getUrlParent(ostring) {
  var nstring='';
  if(ostring[0]==='/'){
    nstring=ostring.split('/')[1];
  }else {
    console.warning('url格式无法解析')
  }
  return nstring;
}
