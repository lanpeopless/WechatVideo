// pages/camera/camera.js
const cameraContext = wx.createCameraContext(this);
var ctx;
var timer;
Page({

  /**
   * 页面的初始数据
   */
  data: {
      screenWidth: 500,
      screenHeight: 500,
      pixelRatio:1,
      cameraStatus:true,
      src:'',
      videoSrc:'',
      videoIsEnd:false,
      secondNum:0,
      takeVideoImage:true,
  },
  onReady:function(){
    console.log('onReady');
    var that = this;
    var screenWidth = wx.getSystemInfoSync().windowWidth;
    var screenHeight = wx.getSystemInfoSync().windowHeight;
    var pixelRatio = wx.getSystemInfoSync().pixelRatio;
    that.setData({
      screenWidth: screenWidth,
      screenHeight: screenHeight,
      pixelRatio:pixelRatio
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log('onLoad');

    var that = this;
    var screenWidth = wx.getSystemInfoSync().windowWidth;
    var screenHeight = wx.getSystemInfoSync().windowHeight;
    var pixelRatio = wx.getSystemInfoSync().pixelRatio;
    that.setData({
      screenWidth: screenWidth,
      screenHeight: screenHeight,
      pixelRatio:pixelRatio
    });
    console.log(this);
    
    this.ctx = wx.createCameraContext()
  },
  /**
   * 切换前后置摄像头
   * @param {*} event 
   */
  cameraChange:function(event){
    var cameraStatus = this.data.cameraStatus;
    this.setData({
      cameraStatus:!cameraStatus
    })
  },
  takePhoto:function(event){
    wx.chooseImage({
      count: 9, // 最多可以选择的图片张数，默认9
      sizeType: ['original', 'compressed'], // original 原图，compressed 压缩图，默认二者都有
      sourceType: ['camera','album'], // album 从相册选图，camera 使用相机，默认二者都有
      success: function(res){
        // success
        console.log(res);
        
      }
    })
  },
  takeVideo:function(event){
    var videoIsEnd = this.data.videoIsEnd;
    wx.chooseVideo({
      sourceType: ['camera'], // album 从相册选视频，camera 使用相机拍摄
      maxDuration: 30, // 拍摄视频最长拍摄时间，单位秒。最长支持60秒
      camera: ['back', 'front'],
      success: function(res){
        // success
        console.log(res);
        var tempVideoUrl = res.tempFilePath;
        var tempCoverUrl = res.tempFilePath;
        var duartion = res.duration;
        var tempHeight = res.height;
        var tempWidth = res.width;
        if (duartion > 31) {
            wx.showToast({
              title: '上传视频大于30秒',
              icon: 'none',
              mask: true,
              duration: 2000
            });
        }else if (duartion < 1){
            wx.showToast({
              title: '视频过短',
              icon: 'none',
              mask: true,
              duration: 2000
            });
        }else {
          //打开选择BGM的页面
          wx.navigateTo({
            url: '../chooseBgm/chooseBgm?duration='+duartion
            + '&tempHeight='+tempHeight
            + '&tempWidth='+tempWidth
            + '&tempVideoUrl='+tempVideoUrl
            + '&tempCoverUrl='+tempCoverUrl
          })
        }
        
      }
    })
    // if (!videoIsEnd) {
    //   // 如果是videoIsEnd是false
    //   // 则表示还未开始录像，调用开始录像函数
    //   this.setData({
    //     videoIsEnd:true
    //   });
    //   this.startRecord();
    // }else {
    //   // 如果是videoIsEnd是true
    //   // 则表示还已经开始录像，调用结束录像函数
    //   this.setData({
    //     videoIsEnd:false
    //   })
    //   this.stopRecord();
    // }
  },
  changeImage:function(){
    var takeVideoImage = this.data.takeVideoImage;
    this.setData({
      takeVideoImage:!takeVideoImage
    });
  },
  // 开始录像
  startRecord:function() {
    var that = this;
    this.ctx.startRecord({
      success: (res) => {
        console.log('startRecord');
      }
    })
  },
  //结束录像
  stopRecord:function() {
    var that = this;
    console.log('stopRecord');
    this.ctx.stopRecord({
      success: (res) => {
        console.log(res);
        that.countUp(false);
        
        var tempCoverUrl = res.tempThumbPath;
        var tempVideoUrl = res.tempVideoPath;
        var tempHeight = that.data.screenHeight-200;
        var tempWidth = that.data.screenWidth;
        var duartion = 30;
        this.setData({
          src: tempCoverUrl,
          videoSrc: tempVideoUrl
        })
        wx.navigateTo({
          url: '../chooseBgm/chooseBgm?duration='+duartion
          + '&tempHeight='+tempHeight
          + '&tempWidth='+tempWidth
          + '&tempVideoUrl='+tempVideoUrl
          + '&tempCoverUrl='+tempCoverUrl
        })
      }
    })
  },
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
        url: '/pages/userLogin/userLogin',
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
        url: '/pages/userLogin/userLogin',
      })
    }
  }
})