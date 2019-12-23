// pages/report/report.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
        reasonType: "请选择原因",
        reportReasonArray: app.reportReasonArray,
        publishUserId:"",
        videoId:""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (params) {
      var me = this;
      var videoId = params.videoId;
      var publishUserId = params.publishUserId;

      me.setData({
        publishUserId: publishUserId,
        videoId: videoId
      });
  },
  changeMe:function(e) {
    var me = this;

    var index = e.detail.value;
    var reasonType = app.reportReasonArray[index];

    me.setData({
      reasonType: reasonType
    });
  },
  submitReport:function(e) {
    var me = this;

    var reasonIndex = e.detail.value.reasonIndex;
    var reasonContent = e.detail.value.reasonContent;

    var user = app.getGlobalUserInfo();
    var currentUserId = user.id;

    if (reasonIndex == null || reasonIndex == '' || reasonIndex == undefined) {
      wx.showToast({
        title: '选择举报理由',
        icon: "none"
      })
      return;
    }
    var serverUrl = app.serverUrl;
    var reasonType = this.data.reasonType;
    wx.request({
      url: serverUrl + '/user/reportUser',
      method: 'POST',
      data: {
        dealUserId:me.data.publishUserId,
        dealVideoId:me.data.videoId,
        title:me.data.reasonType,
        content: reasonContent,
        userId:currentUserId,
      },
      header: {
        'content-type': 'application/json', // 默认值
      }, // 设置请求的 header
      success: function(res){
        // success
        wx.showToast({
          title:res.data.data,
          duration:1000,
          icon:'success'
        });
        setTimeout(function () {
            wx.navigateBack({
              delta: 1 // 回退前 delta(默认为1) 页面
            })
        },2000)
      },
      fail: function() {
        // fail
      },
      complete: function() {
        // complete
      }
    })
  }
})