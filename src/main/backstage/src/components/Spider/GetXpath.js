//get xpath in iframe
$.fn.getQuery = function(options) {
    o = $.extend({
        type: 'xpath',
        highLight: true,
        fullPath: false,
        preferenceAttr: 'id',
        bgColor: 'yellow',
        border: 'yellow 1px solid',
        expansion: 3,
    }, options || {});
    if (o.highLight) {
        this.highLight(o);
    }
    var path = getPath(this, '');
    query = '/' + path;
    if (!o.fullPath) {
        query = '/' + query;
    }
    return query;
}

$.fn.highLight = function(options) {
    op = $.extend({
        bgColor: 'yellow', //背景色
        border: 'yellow 1px solid', //边框
        expansion: 3, //扩大边框
    }, options || {});
    $('body').append("<div id='abs-box' class='abs'> </div>");
    $('head').append("<style>.abs{position:absolute;zoom:1;pointer-events:none;z-index:999;}</style>");
    var div = $('#abs-box');
    if (div != this) {
        var pos = this.offset(),
            em = op.expansion;
        div.css({
            'left': pos.left - em - 1,
            'top': pos.top - em - 1,
            'width': this.innerWidth() + 2 * em,
            'height': this.innerHeight() + 2 * em
        });
        div.css({
            'background-color': op.bgColor,
            'border': op.border
        });
    }
}

function getPath(e, path) {
    var tn = e.get(0).tagName;
    if (isNullOrEmpty(e) || isNullOrEmpty(tn)) {
        return path;
    }
    var attr = getAttr(e);
    tn = tn.toLowerCase() + attr;
    path = isNullOrEmpty(path) ? tn : tn + "/" + path;
    var parentE = e.parent();
    if (isNullOrEmpty(parentE) || (!o.fullPath && attr.substring(0, 5) == '[@id=')) {
        return path;
    }
    return getPath(parentE, path);
}

function getAttr(e) {
    var tn = e.get(0).tagName;
    var id = e.attr('id'),
        clazz = e.attr('class');
    var hasId = !isNullOrEmpty(id),
        hasClazz = !isNullOrEmpty(clazz);
    id = "[@id='" + id + "']";
    clazz = "[@class='" + clazz + "']";
    if (hasId && hasClazz) {
        if (o.preferenceAttr.toLowerCase() == 'class') {
            return clazz;
        } else {
            return id;
        }
    } else if (hasId && !hasClazz) {
        return id;
    } else if (!hasId && hasClazz) {
        return clazz;
    } else {
        if (e.siblings(tn).size() > 0) {
            var i = e.prevAll(tn).size();
            if (o.type == 'xpath') {
                i++;
            }
            return '[' + i + ']';
        } else {
            return '';
        }
    }
}

function isNullOrEmpty(o) {
    return null == o || 'null' == o || '' == o || undefined == o;
}
String.prototype.replaceAll = function(regx, t) {
    return this.replace(new RegExp(regx, 'gm'), t);
};
//鼠标经过 
$("*").hover(
    function(e) {
        $(e.target).css({
            'border': '0.2px red solid',
            'cursor': 'default',
        });
    },
    function(e) {
        $(e.target).css({
            'border': '',
            'cursor': 'auto',
        });
        $(e.target).parents().css({
            'border': ''
        });
    }
)
$(document).click(function(e) {
    e = e || window.event;
    var target = e.target || e.srcElement;
    var path = $(target).getQuery({
        type: 'xpath',
        preferenceAttr: 'id',
        highLight: true,
        bgColor: 'rgba(255,255,0,.4)',
        border: 'green 1px solid',
        expansion: 2,
        fullPath: false //是否是全路径, 默认是false
    });
    parent.window.PubSub.emit('GET_XPATH', path);
    return false;
});
