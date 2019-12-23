const app = getApp()

Page({
    data: {
      bgmList: [],
      serverUrl: "",
      videoParams: {},
      poster: '',
      name: '',
      author: '',
      src: '',
    
    },

    onLoad: function (params) {
      this.setData({
        videoParams:params
      })
      var serverUrl = app.serverUrl;
      var userInfo = app.getGlobalUserInfo();
      console.log(userInfo);
      
      var that = this;
      wx.showLoading({
        title: '请等待',
      });
      wx.request({
        url: serverUrl + '/bgm/queryAllBgmList', 
        method: "POST",
        header: {
          'content-type': 'application/json', // 默认值
          // 将用户的id和token放到header中用于安全认证
          'userId': userInfo.id,
          'userToken': userInfo.userToken
        },
        success(res) {
          if (res.data.status == 200) {
            wx.hideLoading();
            that.setData({
              bgmList : res.data.data,
              serverUrl: serverUrl
            })
          } else if(res.data.status == 502){
            wx.showToast({
                title: res.data.msg ,
                icon: 'none',
                duration: 2000
            });
            app.userInfo = null;
            wx.removeStorageSync('userInfo');
            setTimeout(function () {
                wx.redirectTo({
                  url: '/pages/userLogin/userLogin',
                })
            }, 1000)
          } else {
            wx.hideLoading();
            wx.showToast({
              title: 'BGM查询失败,' + res.data.msg,
              icon: 'none',
              mask: true,
              duration: 2000
            })
          }
        }
      })
    },
    upload:function (params) {
      var that = this;
      var bgmId = params.detail.value.bgmId;
      var desc = params.detail.value.desc;
      var videoParams = this.data.videoParams;
      var serverUrl = app.serverUrl;
      var tempVideoUrl = videoParams.tempVideoUrl;
      var tempCoverUrl = videoParams.tempCoverUrl;
      var duartion = videoParams.duration;
      var tempHeight = videoParams.tempHeight;
      var tempWidth = videoParams.tempWidth;
      // 获取本地缓存
      var userInfo = app.getGlobalUserInfo();
      app.userInfo = userInfo;
      
        wx.showLoading({
          title: '上传中....',
        });
        wx.uploadFile({
          url: serverUrl + '/video/uploadVideo', // 接口地址
          filePath: tempVideoUrl,
          formData:{
            userId: userInfo.id,
            bgmId: bgmId,
            desc: desc,
            videoSeconds: duartion,
            videoHeight: tempHeight,
            videoWidth: tempWidth
          },
          name: 'files',
          header: {
            'content-type': 'application/json', // 默认值
            // 将用户的id和token放到header中用于安全认证
            'userId': userInfo.id,
            'userToken': userInfo.userToken
          },
          success(res1) {
            if (res1.data.status == 200){
              const data = JSON.parse(res1.data);
              console.log(res1);
              wx.hideLoading();
              wx.showToast({
                title: '上传视频成功',
                icon: 'success',
                mask: true,
                duration: 2000
              });
              setTimeout(function () {
                wx.navigateBack({
                  delta: 1, // 回退前 delta(默认为1) 页面
                })
              }, 1000);
            } else if(res1.data.status == 502) {
                wx.showToast({
                    title: res.data.msg ,
                    icon: 'none',
                    duration: 2000
                });
                app.userInfo = null;
                wx.removeStorageSync('userInfo');
                setTimeout(function () {
                    wx.redirectTo({
                      url: '/pages/userLogin/userLogin',
                    })
                }, 1000)
              }

            },fail(res4){
                wx.hideLoading();
                wx.showToast({
                  title: '上传视频失败',
                  icon: 'none',
                  mask: true,
                  duration: 2000
                });
            }
        })
      
    }
})

