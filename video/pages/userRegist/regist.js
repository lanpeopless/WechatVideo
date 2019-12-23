const app = getApp()

Page({
  data: {

  },
  doRegist: function (e) {
    var formObject = e.detail.value;
    var username = formObject.username;
    var password = formObject.password;
    if (username.length == 0 || password.length == 0) {
      //提醒用户用户名或密码不为空
      wx.showToast({
        title: '用户名或密码不为空',
        icon: 'none',
        mask: true,
        duration: 2000
      })
    } else {
      // 向后台发起请求，注册用户信息
      var serverUrl = app.serverUrl;
      wx.showLoading({
        title: '请等待',
      });
      wx.request({
        url: serverUrl + '/register',
        method: "POST",
        data: {
          username: username,
          password: password
        },
        header: {
          'content-type': 'application/json' // 默认值
        },
        success(res) {
          if (res.data.status == 200) {
            wx.hideLoading();
            app.userInfo = res.data.data;
            // 添加本地缓存
            app.setGlobalUserInfo(res.data.data);
            wx.showToast({
              title: '注册成功',
              icon: 'success',
              mask: true,
              duration: 2000
            }),
              setTimeout(function () {
                wx.redirectTo({
                  url: '/pages/mine/mine',
                })
              }, 1000)
          
          } else {
            wx.hideLoading();
            wx.showToast({
              title: '注册失败,' + res.data.msg,
              icon: 'none',
              mask: true,
              duration: 2000
            })
          }
        }
      })
    }

  },    //事件处理函数
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