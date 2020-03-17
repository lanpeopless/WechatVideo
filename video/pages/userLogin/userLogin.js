const http = require('../../config/httpRequest.js');
const utils = require('../../utils/utils.js');
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
  onLoad: function(param) {
    var nextUrl = param.next;
    this.setData({
      nextUrl:nextUrl
    })
  },
  doLogin: function(e) {
    var that = this;
    var formObject = e.detail.value;
    var username = formObject.username;
    var password = formObject.password;
    if (utils.isEmpty(username) || utils.isEmpty(password)) {
      //提醒用户用户名或密码不为空
      wx.showToast({
        title: '用户名或密码不为空',
        icon: 'none',
        mask: true,
        duration: 2000
      })
    } else {
      // 向后台发起请求，请求登录
      var serverUrl = app.serverUrl;
      http.requestUrl({
        url: app.requestInfo.user.login.url,
        method: app.requestInfo.user.login.method,
        params: {
          username: username,
          password: password
        }
      }).then(res => {
        if (res.code == 'ok') {
          app.userInfo = res.data;
          wx.hideLoading();
          // 添加本地缓存
          app.setGlobalUserInfo(res.data);
          wx.showToast({
            title: '登录成功',
            icon: 'success',
            mask: true,
            duration: 2000
          }),
          setTimeout(function () {
            if (that.data.nextUrl == 'camera'){
              wx.redirectTo({
                url: '/pages/camera/camera',
              })
            } else if (that.data.nextUrl == 'mine') {
              wx.redirectTo({
                url: '/pages/mine/mine',
              })
            } else {
              wx.redirectTo({
                url: '/pages/index/index',
              })
            }
          }, 1000)
          
        } else {
          wx.hideLoading();
          wx.showToast({
            title: '登录失败,' + res.message,
            icon: 'none',
            mask: true,
            duration: 2000
          })
        }
      })
    }
  },  //事件处理函数
  toIndex: function(e) {
    wx.redirectTo({
      url: '/pages/index/index',
    })
  },
  toCamera: function(e){
    if (app.userInfo != null){
      wx.redirectTo({
        url: '/pages/camera/camera',
      })
    } else {
      wx.redirectTo({
        url: '/pages/userLogin/userLogin?next=camera',
      })
    }
  },
  toLogin: function(e){
    wx.redirectTo({
      url: '/pages/userLogin/userLogin',
    })
  },
  toRegister: function (e) {
    wx.redirectTo({
      url: '/pages/userRegist/regist',
    })
  },
  toMine: function (e) {
    if (app.userInfo != null){
      wx.redirectTo({
        url: '/pages/mine/mine',
      })
    } else {
      wx.redirectTo({
        url: '/pages/userLogin/userLogin?next=mine',
      })
    }
  }
})