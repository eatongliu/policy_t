module.exports = {
  parseQueryString:function(url){
    var obj = {};
    var keyvalue = [];
    var key = "",
        value = "";
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
    for (var i in paraString) {
        keyvalue = paraString[i].split("=");
        key = keyvalue[0];
        value = keyvalue[1];
        if (obj[key]) {
          obj[key] = obj[key]+','+value;
        }else {
          obj[key] = value;
        }
    }
    return obj;
  },
  getLocalTime:function(nS) {
      if(nS == null || nS == '') return '-';
      return new Date(parseInt(nS)).toLocaleDateString()
  }

}
