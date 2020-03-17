/* 公共request 方法 */
const app = getApp();
const utils = require('../utils/utils.js');

const requestUrl=({
   url,
   method = "POST",
   params,
   headers = null
})=>{
  wx.showLoading({
    title: '加载中',
  });
  let server = app.serverUrl;
  let userInfo = app.userInfo;
  let userToken = app.userToken;
  let header;
  if (headers == null) {
    if (userInfo != "" && userInfo !=null){
      header = { 
        'content-type': 'application/json', 
        'uid': userInfo.id,
        'token': app.userToken
      }
    }else{
      header = { 'content-type': 'application/json'}
    }
  } else {
    header = headers;
  }
  return new Promise(function (resolve, reject) {
    wx.request({
      url: server + url,
      method: method,
      data: params,
      header: header,
      success: (res) => {
        wx.hideLoading();
        if (res.data.code == 'error') {
            wx.showToast({
                title: res.data.message || '请求出错',
                icon: 'none',
                duration: 2000,
                mask: true
            })
        } else {
          // 检测有无token信息
          if (utils.isNotEmpty(res.header['token']) || utils.isNotEmpty(res.header['Token'])) {
            app.userToken = utils.isNotEmpty(res.header['token']) 
              ? res.header['token'] : res.header['Token'];
          }
        }
        return resolve(res.data);
      },
      fail: function (res) {
        wx.hideLoading();
        wx.showToast({
          title: res.data.msg || '请求出错',
          icon: 'none',
          duration: 2000,
          mask: true
        })
        return reject(res.data);
      },
      complete: function () {
        wx.hideLoading()
      }
    })
  }).catch((res) => { 
      console.error(res);
  })
}
/* 公共showTotast  loading 方法 */
module.exports = {
//   formatTime: formatTime,
  requestUrl: requestUrl
}