
function isEmpty(obj) {
  return obj === undefined || obj === null || ((obj instanceof String) && obj.trim().length === 0);
}

function isNotEmpty(obj) {
  return !isEmpty(obj);
}

/**
 * 深拷贝
 * @param source 
 * @param target 
 */
function deepCopy(source, target) {
  for (const key in source) {
    if (source.hasOwnProperty(key)) {
      if (source.length && source.length > 0) {
        target[key] = {};
        deepCopy(source[key], target[key]);
      } else {
        target[key] = source[key];
      }
    }
  }
}

function toString(value) {
  return isEmpty(value) ? '' : value.toString();
}

function toBoolean(value) {
  return value === '' || (value && value !== 'false');
}

/**
 * @param source 对象深度复制
 */
function copyObject(source) {
  if (!source) {
    return null;
  }
  const CircularJSON = require('circular-json');
  // const str = CircularJSON.stringify(source);
  // const str = JSON.stringify(source )
  let cache = [];
  const str = JSON.stringify(source, function (key, value) {
    if (typeof value === 'object' && value !== null) {
      if (cache.indexOf(value) !== -1) {
        // Circular reference found, discard key
        return;
      }
      // Store value in our collection
      cache.push(value);
    }
    return value;
  });
  cache = null; // Enable garbage collection
  CircularJSON.parse(str);
  return CircularJSON.parse(str);
}

/**
 * 过滤特殊字符
 * @param source 过滤原字符串
 */
function filterSpecialChar(source) {
  if (!source) {
    return '';
  }
  const sourceString = source.toString();
  let sourceStringCode = '';
  let index = 0;
  let sourceStringTemp = '';
  // 处理回车符和制表符
  for (let i = 0; i < sourceString.length; i++) {
    if (sourceString.charCodeAt(i) === 10 || sourceString.charCodeAt(i) === 9) {
      sourceStringCode = sourceStringCode + sourceString.substring(index, i);
      index = i + 1;
    }
  }
  sourceStringCode = sourceStringCode + sourceString.substring(index, sourceString.length);
  const temp = sourceStringCode.split('\\');
  if (temp.length === 0) {
    return sourceStringCode;
  }
  // 处理转义符
  for (let i = 0; i < temp.length; i++) {
    if (i === temp.length - 1) {
      sourceStringTemp = sourceStringTemp + temp[i];
    } else {
      sourceStringTemp = sourceStringTemp + temp[i] + '\\\\';
    }
  }
  return sourceStringTemp;
}

// 判断字符串字节长度 汉字及全角符号占3
function calcByteLength(str, num = 3) {
  if (str === null || str === undefined) {
    return 0;
  }
  let nstr = '';
  while (num > 0) {
    nstr += '0';
    num--;
  }
  return (str + '').replace(/[^\x00-\xff]/g, nstr).length;
}

/**
 * 长度超过length截取前length个字节
 * @params value
 * @params length
 * @params num  一个汉字占几个字节
 */
function cutString(value, length, num = 3) {
  if (value === null || value === undefined) {
    return '';
  }
  if (!(value instanceof String)) {
    console.error('cutString 只支持string类型,' + value + '不是string类型');
    return value;
  }
  if (value.length > length) {
    value = value.substring(0, length);
  }
  let l;
  while ((l = calcByteLength(value, num)) > length) {
    value = value.substring(0, value.length - Math.ceil((l - length) / num));
  }
  return value;
}

/**
 * 格式化日期
 * @params date
 * @params fmt
 */
function formatDate(date, fmt = 'yyyy-MM-dd') {
  const o = {
    'M+': date.getMonth() + 1, // 月份
    'd+': date.getDate(), // 日
    'h+': date.getHours(), // 小时
    'm+': date.getMinutes(), // 分
    's+': date.getSeconds(), // 秒
    'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
    'S': date.getMilliseconds() // 毫秒
  };
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
  }
  for (const k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
    }
  }
  return fmt;
}

/**
 * 日期+天数得到新日期
 * @param date 日期
 * @param days 天数
 */
function addDate(date, days) {
  const d = new Date(date);
  d.setDate(d.getDate() + days);
  const m = d.getMonth() + 1;
  return d.getFullYear() + '-' + m + '-' + d.getDate();
}

/**
 * 限制输入框字数
 */
function trimText(value, nlength) {
  if (value && nlength) {
    console.log('限制的字数' + nlength);
    const length = nlength;
    const oldV = value.trim();
    const newV = oldV.substr(0, nlength);
    if (oldV.length > newV.length) {
      // this.message.create('error', '填写超过长度限制,系统已自动删除多余字符');
      return newV;
    } else {
      return oldV;
    }
  }
}


function getAgent() {
  const userAgent = navigator.userAgent;
  if (userAgent.indexOf('Windows') > -1) {
    if (userAgent.indexOf('Windows NT 10.0') > -1) {// Windows 10
      return checkBrowser(userAgent, 'win', '10.0'); // 判断浏览器
    } else if (userAgent.indexOf('Windows NT 6.4') > -1) {// Windows 10
      return checkBrowser(userAgent, 'win', '10'); // 判断浏览器
    } else if (userAgent.indexOf('Windows NT 6.3') > -1) {// Windows 8
      return checkBrowser(userAgent, 'win', '8.1'); // 判断浏览器
    } else if (userAgent.indexOf('Windows NT 6.2') > -1) {// Windows 8
      return checkBrowser(userAgent, 'win', '8'); // 判断浏览器
    } else if (userAgent.indexOf('Windows NT 6.1') > -1) {// Windows 7
      return checkBrowser(userAgent, 'win', '7');
    } else if (userAgent.indexOf('Windows NT 6.0') > -1) {// Windows Vista
      return checkBrowser(userAgent, 'win', 'Vista');
    } else if (userAgent.indexOf('Windows NT 5.2') > -1) {// Windows XP x64 Edition
      return checkBrowser(userAgent, 'win', 'XP');
    } else if (userAgent.indexOf('Windows NT 5.1') > -1) {// Windows XP
      return checkBrowser(userAgent, 'win', 'XP');
    }
  } else if (userAgent.indexOf('Mac OS X') > -1) {
    if (userAgent.indexOf('iPhone') > -1) {
      return checkBrowser(userAgent, 'Mac', 'iPhone');
    } else if (userAgent.indexOf('iPad') > -1) {
      return checkBrowser(userAgent, 'Mac', 'iPad');
    } else {
      return checkBrowser(userAgent, 'Mac', 'else');
    }
  } else if (userAgent.indexOf('Android') > -1) {
    return checkBrowser(userAgent, 'Android', '');
  }
}

function checkBrowser(userAgent, os, osversion) {
  let temp, bversion;
  if (userAgent.indexOf('Chrome') > -1) {
    if (userAgent.indexOf('OPR') > -1) {
      temp = userAgent.substring(userAgent.indexOf('OPR/') + 4);
      // 拿到User Agent String "Chrome/" 之后的字符串,结果形如"24.0.1295.0 Safari/537.15"或"24.0.1295.0"
      bversion = temp.substring(0, 2);
      return UserAgent('opera', bversion, os, osversion);
    } else {
      temp = userAgent.substring(userAgent.indexOf('Chrome/') + 7);
      // 拿到User Agent String "Chrome/" 之后的字符串,结果形如"24.0.1295.0 Safari/537.15"或"24.0.1295.0"
      bversion = temp.substring(0, 2);
      return UserAgent('Chrome', bversion, os, osversion);
    }
  } else if (userAgent.indexOf('Firefox') > -1) {
    temp = userAgent.substring(userAgent.indexOf('Firefox/') + 8);
    // 拿到User; Agent; String; 'Firefox/'; 之后的字符串, 结果形如; '16.0.1 Gecko/20121011'; 或; '16.0.1';
    bversion = temp.substring(0, 2);
    return UserAgent('Firefox', bversion, os, osversion);
  } else if (userAgent.indexOf('MSIE') > -1) {
    if (userAgent.indexOf('MSIE 10.0') > -1) {// IE 10
      return UserAgent('IE', '10', os, osversion);
    } else if (userAgent.indexOf('MSIE 9.0') > -1) {// IE 9
      return UserAgent('IE', '9', os, osversion);
    } else if (userAgent.indexOf('MSIE 8.0') > -1) {// IE 8
      return UserAgent('IE', '8', os, osversion);
    } else if (userAgent.indexOf('MSIE 7.0') > -1) {// IE 7
      return UserAgent('IE', '7', os, osversion);
    } else if (userAgent.indexOf('MSIE 6.0') > -1) {// IE 6
      return UserAgent('IE', '6', os, osversion);
    }
  } else if (userAgent.indexOf('Edge') > -1) { // IE Edge
    return UserAgent('IE', 'Edge', os, osversion);
  } else if (userAgent.indexOf('Trident/7.0') > -1) { // IE 11
    return UserAgent('IE', '11', os, osversion);
  } else if (userAgent.indexOf('Version') > -1 && userAgent.indexOf('Safari') > -1) {
    temp = userAgent.substring(userAgent.indexOf('Safari/') + 7);
    bversion = temp.substring(0, 2);
    return UserAgent('Safari', bversion, os, osversion);
  }
}

function UserAgent(browser, browserversion, os, osversion) {
  return {
    os: os,
    osv: osversion,
    br: browser,
    brv: browserversion
  };
}

/**
 * 获取当天0点时间
 */
function setTimeByDay(time) {
  if (time) {
    time = time.getTime();
    return time - time % (24 * 3600000) - 8 * 3600000;
  }
  return null;
}

/**
 * 全角转半角
 * @param fileName 文件名
 */
function full2half(fileName) {
  // 如: ＼／：＊？＜＞｜     \/:*?<>|
  fileName = fileName
    .replace(/＼/g, '\\')
    .replace(/／/g, '/')
    .replace(/：/g, ':')
    .replace(/＊/g, '*')
    .replace(/？/g, '?')
    .replace(/＜/g, '<')
    .replace(/＞/g, '>')
    .replace(/｜/g, '|')
    .replace(/－/g, '-')
    .replace(/　/g, ' ');
  return fileName;
}

/**
 * 半角转全角
 * @param fileName 文件名
 */
function half2full(fileName) {
  // 如: ＼／：＊？＜＞｜     \/:*?<>|
  fileName = fileName
    .replace(/\\/g, '＼')
    .replace(/\//g, '／')
    .replace(/\:/g, '：')
    .replace(/\*/g, '＊')
    .replace(/\?/g, '？')
    .replace(/\</g, '＜')
    .replace(/\>/g, '＞')
    .replace(/\|/g, '｜')
    .replace(/\-/g, '－')
    .replace(/ /g, '　');
  return fileName;
}

/**
 * 获取到当前浏览器版本
 */
function getBrowserInfo() {
  var Sys = {};
  var ua = navigator.userAgent.toLowerCase();
  var re = /(msie|firefox|chrome|opera|version).*?([\d.]+)/;
  var m = ua.match(re);
  if (m && m.length > 0) {
    Sys.browser = m[1].replace(/version/, "'safari");
    Sys.ver = m[2];
  }
  return Sys;
}

function decode(val, key) {
  let e = str2UTF8(val);
  let keyByte = str2UTF8(key);
  let dee = e;
  for (let i = 0; i < e.length; i++) {
    for (let j = 0; j < keyByte.length; j++) {
      e[i] = dee[i] ^ keyByte[j];
    }
  }
  return byteToString(e);
};

function str2UTF8(str) {
  var bytes = new Array();
  var len, c;
  len = str.length;
  for (var i = 0; i < len; i++) {
    c = str.charCodeAt(i);
    if (c >= 0x010000 && c <= 0x10FFFF) {
      bytes.push(((c >> 18) & 0x07) | 0xF0);
      bytes.push(((c >> 12) & 0x3F) | 0x80);
      bytes.push(((c >> 6) & 0x3F) | 0x80);
      bytes.push((c & 0x3F) | 0x80);
    } else if (c >= 0x000800 && c <= 0x00FFFF) {
      bytes.push(((c >> 12) & 0x0F) | 0xE0);
      bytes.push(((c >> 6) & 0x3F) | 0x80);
      bytes.push((c & 0x3F) | 0x80);
    } else if (c >= 0x000080 && c <= 0x0007FF) {
      bytes.push(((c >> 6) & 0x1F) | 0xC0);
      bytes.push((c & 0x3F) | 0x80);
    } else {
      bytes.push(c & 0xFF);
    }
  }
  return bytes;
}

function byteToString(arr) {
  if (typeof arr === 'string') {
    return arr;
  }
  var str = '',
    _arr = arr;
  for (var i = 0; i < _arr.length; i++) {
    var one = _arr[i].toString(2),
      v = one.match(/^1+?(?=0)/);
    if (v && one.length == 8) {
      var bytesLength = v[0].length;
      var store = _arr[i].toString(2).slice(7 - bytesLength);
      for (var st = 1; st < bytesLength; st++) {
        store += _arr[st + i].toString(2).slice(2);
      }
      str += String.fromCharCode(parseInt(store, 2));
      i += bytesLength - 1;
    } else {
      str += String.fromCharCode(_arr[i]);
    }
  }
  return str;
}

/**
 * 生成uuid
 */
function uuid() {
  const s = [];
  const hexDigits = '0123456789abcdef';
  for (let i = 0; i < 36; i++) {
      s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
  }
  s[14] = '4'; // bits 12-15 of the time_hi_and_version field to 0010
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
  s[8] = s[13] = s[18] = s[23] = '-';

  const uuid = s.join('');
  return uuid;
}

module.exports = {
  deepCopy: deepCopy,
  isEmpty:isEmpty,
  isNotEmpty:isNotEmpty,
  toString:toString,
  toBoolean:toBoolean,
  copyObject:copyObject,
  filterSpecialChar:filterSpecialChar,
  calcByteLength:calcByteLength,
  cutString:cutString,
  formatDate:formatDate,
  trimText:trimText,
  getAgent:getAgent,
  checkBrowser:checkBrowser,
  UserAgent:UserAgent,
  setTimeByDay:setTimeByDay,
  full2half:full2half,
  half2full:half2full,
  getBrowserInfo:getBrowserInfo,
  decode:decode,
  str2UTF8:str2UTF8,
  byteToString:byteToString,
  uuid:uuid
};

