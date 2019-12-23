

const app = getApp()

Page({
  data: {
    faceUrl: "http://47.102.223.176:8080/images/noneface.png",
    creatorId:'',
    faceImageArr:[],
    fansCounts:0,
    followCounts:0,
    receiveLikeCounts:0,
    nickname:'',
    isMine:true,
    likeUserStatus:false,

    videoSelClass: "video-info",
    isSelectedWork: "video-info-selected",
    isSelectedLike: "",
    isSelectedFollow: "",

    myWorkFalg: false,
    myLikesFalg: true,
    myFollowFalg: true,

    currentUserId: '',
    videoList:[],
    totalPage:1,//总页数
    page:1,// 当前页码
    videoListCount:0,//当前记录数
    records:1, //总记录数
    pageSize:10,// 当前页面大小
    screenWidth: 350,
    screenHeight: 350,
    pixelRatio:1,
  },
  onLoad: function (params) {
    var screenWidth = wx.getSystemInfoSync().windowWidth;
    var screenHeight = wx.getSystemInfoSync().windowHeight;
    var pixelRatio = wx.getSystemInfoSync().pixelRatio;
    this.setData({
      screenWidth: screenWidth,
      screenHeight: screenHeight,
      pixelRatio:pixelRatio
    });
    var userInfo = app.userInfo;
    var videoCreatorId = params.videoCreatorId;
    if (videoCreatorId == undefined ) {
      this.getUserInfo();
      this.setData({
        currentUserId:app.userInfo.id
      })
    }else {
      this.setData({
        creatorId:videoCreatorId,
        currentUserId:videoCreatorId
      })
      this.getCreatorInfo();
      if (userInfo != null) {
        this.getUserLikeStatus();
      }
    }
    this.doSelectWork();
  },
  showVideo:function(params){
    var arrindex = params.currentTarget.dataset.arrindex;
    var that = this;
    var serverUrl = app.serverUrl;
    var videoList = this.data.videoList;
    wx.navigateTo({
      url: "/pages/index/index?arrindex=" + arrindex + "&mineVideo=" + JSON.stringify(videoList),
      success: function(res){
        // success
      }
    })
  },
  previewImage: function(){
    var faceUrl = this.data.faceUrl;
    var faceImageArr = this.data.faceImageArr;
    faceImageArr.push(faceUrl);
    wx.previewImage({
      urls: faceImageArr
    })
  },
  changeUserLikeStatus:function(){
    var likeUserStatus = this.data.likeUserStatus;
    var creatorId = this.data.creatorId;
    var that = this;
    var serverUrl = app.serverUrl;
    var userInfo = app.userInfo;
    var fansCounts = that.data.fansCounts;
    if (userInfo != null) {
      wx.request({
        url: serverUrl + '/user/changeUserLikeStatus',
        data: {
          userId:creatorId,
          fanId:userInfo.id,
          likeUserStatus:!likeUserStatus
        },
        method: 'POST',
        header: {
          'content-type': 'application/json' // 默认值
        }, // 设置请求的 header
        success: function(res){
          console.log(res);
          that.setData({
            likeUserStatus:!likeUserStatus
          });
          if (!likeUserStatus) {
            that.setData({
              fansCounts:fansCounts+1
            })
          }else {
            that.setData({
              fansCounts:fansCounts-1
            })
          }
          that.getUserLikeStatus();
        }
      })
    }else {
      wx.redirectTo({
        url: '/pages/userLogin/userLogin?next=mine',
      })
    }
  },
  logout: function (params){
    var user = app.getGlobalUserInfo();
    
    // 向后台发起请求，请求登录
        var serverUrl = app.serverUrl;
        wx.showLoading({
          title: '请等待',
        });
        wx.request({
          url: serverUrl + '/logout',
          method: "POST",
          data: {
            userId: user.id
          },
          header: {
            'content-type': 'application/json' // 默认值
          },
          success(res) {
            
            if (res.data.status == 200) {
              wx.hideLoading();
              app.userInfo = null;
              wx.removeStorageSync('userInfo');
              wx.showToast({
                title: '注销成功',
                icon: 'success',
                mask: true,
                duration: 2000
              }),
                setTimeout(function () {
                  wx.redirectTo({
                    url: '/pages/userLogin/userLogin',
                  })
                }, 1000)

            } else {
              wx.hideLoading();
              wx.showToast({
                title: '注销失败,' + res.data.msg,
                icon: 'none',
                mask: true,
                duration: 2000
              })
            }
          }
        })
  },
  getUserInfo: function (params){
    console.log(params);
    var that = this;
    var user = app.getGlobalUserInfo();
    app.userInfo = user;
    
    // 向后台发起请求，请求登录
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '数据加载中',
    });
    wx.request({
      url: serverUrl + '/user/queryUserInfoById',
      method: "POST",
      data: {
        id: user.id
      },
      header: {
        'content-type': 'application/json', // 默认值
        // 将用户的id和token放到header中用于安全认证
        'userId': user.id,
        'userToken': user.userToken
      },
      success(res) {
        if (res.data.status == 200) {
          wx.hideLoading();
          var userInfo = res.data.data;
          var token = userInfo.userToken;
          app.userInfo = userInfo;
          app.userInfo.userToken = token;
          wx.showToast({
            title: '查询成功',
            icon: 'success',
            mask: true,
            duration: 2000
          })
          var faceUrl;
          if (userInfo.faceImage==null){
            faceUrl = 'http://47.102.223.176:8080/images/noneface.png';
          }else{
            faceUrl = serverUrl + userInfo.faceImage;
          }
          that.setData({
            faceUrl: faceUrl,
            fansCounts: userInfo.fansCounts,
            followCounts: userInfo.followCounts,
            receiveLikeCounts: userInfo.receiveLikeCounts,
            nickname: userInfo.nickname,
            isMine:true
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
            title: '查询失败,' + res.data.msg,
            icon: 'none',
            mask: true,
            duration: 2000
          })
        }
      }
    })
    
  },
  /**
   * 获取视频创作者的信息
   * @param {*} params 
   */
  getCreatorInfo: function (params){
    var that = this;
    var creatorId = this.data.creatorId;
    // 向后台发起请求，请求登录
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '数据加载中',
    });
    wx.request({
      url: serverUrl + '/user/queryUserInfoById',
      method: "POST",
      data: {
        id: creatorId
      },
      header: {
        'content-type': 'application/json' // 默认值
        // 将用户的id和token放到header中用于安全认证
      },
      success(res) {
        if (res.data.status == 200) {
          wx.hideLoading();
          var creatorInfo = res.data.data;
          var token = creatorInfo.userToken;
          wx.showToast({
            title: '查询成功',
            icon: 'success',
            mask: true,
            duration: 2000
          })
          var faceUrl;
          if (creatorInfo.faceImage==null){
            faceUrl = 'http://47.102.223.176:8080/images/noneface.png';
          }else{
            faceUrl = serverUrl + creatorInfo.faceImage;
          }
          that.setData({
            faceUrl: faceUrl,
            creatorId: creatorInfo.id,
            fansCounts: creatorInfo.fansCounts,
            followCounts: creatorInfo.followCounts,
            receiveLikeCounts: creatorInfo.receiveLikeCounts,
            nickname: creatorInfo.nickname,
            isMine:false
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
            title: '查询失败,' + res.data.msg,
            icon: 'none',
            mask: true,
            duration: 2000
          })
        }
      }
    })
    
  },
  getUserLikeStatus: function(){
     // likeUserStatus:creatorInfo.likeUserStatus,
    var that = this;
    var creatorId = this.data.creatorId;
    var serverUrl = app.serverUrl;
    var userInfo = app.userInfo;
    console.log(userInfo);
    
    wx.request({
      url: serverUrl + '/user/getUserLikeStatus',
      data: {
        userId:creatorId,
        fanId:userInfo.id
      },
      method: 'POST',
      header: {
        'content-type': 'application/json' // 默认值
      }, // 设置请求的 header
      success: function(res){
        // success
        var likeUserStatus = res.data.data.likeUserStatus;
        that.setData({
          likeUserStatus:likeUserStatus
        });
        
      },
      fail: function() {
        // fail
      },
      complete: function() {
        // complete
      }
    })
  },
  changeFace: function (params){
    var that = this;
    wx.chooseImage({
      count: 1,
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success(res) {
        // tempFilePath可以作为img标签的src属性显示图片
        const tempFilePaths = res.tempFilePaths;
        var serverUrl = app.serverUrl;
        wx.showLoading({
          title: '上传中....',
        });
        var userInfo = app.getGlobalUserInfo();
        app.userInfo = userInfo;
        wx.uploadFile({
          url: serverUrl + '/user/uploadFaceImage?userId='+userInfo.id, // 接口地址
          filePath: tempFilePaths[0],
          name: 'file',
          header: {
            'content-type': 'application/json', // 默认值
            // 将用户的id和token放到header中用于安全认证
            'userId': userInfo.id,
            'userToken': userInfo.userToken
          },
          success(res) {
            if (res.data.status == 200) {
              const imageUrl = res.data;
              wx.hideLoading();
              wx.showToast({
                title: '上传头像成功',
                icon: 'success',
                mask: true,
                duration: 2000
              });
              that.getUserInfo();
            } else if (res.data.status == 502) {
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
          }
        })
      }
    })
  },
  uploadVideo:function(){
    wx.chooseVideo({
      sourceType: ['album', 'camera'],
      maxDuration: 30,
      compressed:true,
      camera: 'back',
      success(res) {
        var tempVideoUrl = res.tempFilePath;
        var tempCoverUrl = res.thumbTempFilePath;
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
  },
  /**
   * tab页切换--我的作品
   */
  doSelectWork: function () {
    this.setData({
      isSelectedWork: "video-info-selected",
      isSelectedLike: "video-info-unselected",
      isSelectedFollow: "video-info-unselected",

      myWorkFalg: true,
      myLikesFalg: false,
      myFollowFalg: false,

      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followVideoList: [],
      followVideoPage: 1,
      followVideoTotal: 1
    });

    this.getVideoList('work');
  },
  /**
   * tab页切换--我的喜欢
   */
  doSelectLike: function () {
    this.setData({
      isSelectedWork: "video-info-unselected",
      isSelectedLike: "video-info-selected",
      isSelectedFollow: "video-info-unselected",

      myWorkFalg: false,
      myLikesFalg: true,
      myFollowFalg: false,

      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followVideoList: [],
      followVideoPage: 1,
      followVideoTotal: 1
    });

    this.getVideoList('like');
  },
  /**
   * tab页切换--我的关注
   */
  doSelectFollow: function () {
    this.setData({
      isSelectedWork: "video-info-unselected",
      isSelectedLike: "video-info-unselected",
      isSelectedFollow: "video-info-selected",

      myWorkFalg: false,
      myLikesFalg: false,
      myFollowFalg: true,

      myVideoList: [],
      myVideoPage: 1,
      myVideoTotal: 1,

      likeVideoList: [],
      likeVideoPage: 1,
      likeVideoTotal: 1,

      followVideoList: [],
      followVideoPage: 1,
      followVideoTotal: 1
    });

    this.getVideoList('follow');
  },
  /**
   * 获取个人信息展示页面的视频列表
   * @param {*} param 传入三个tab页的状态值分别为 work like follow
   */
  getVideoList: function(param){
    var that = this;
    var currentUserId = this.data.currentUserId;
    var serverUrl = app.serverUrl;
    this.data
    if (param != undefined){
      wx.request({
        url: serverUrl + '/video/getPersonalVideo',
        method: "POST",
        data: {
          page: that.data.page,
          pageSize: that.data.pageSize,
          currentUserId: currentUserId,
          status: param
        },
        header: {
          'content-type': 'application/json' // 默认值
        },
        success: function(res){
          var videoList = res.data.data.rows;
          var videoListOld = that.data.videoList;
          var videoListNew = videoListOld.concat(videoList);
          var videoListCount = videoListNew.length;
          var page = res.data.data.page;
          var records = res.data.data.records;
          var total = res.data.data.total;
          that.setData({
            videoList:videoList,
            serverUrl:serverUrl,
            page:page,
            records:records,
            totalPage:total,
            videoListCount,videoListCount
          })
          console.log(that.data.videoList);
          
        }
      })
    }
    
  },
  onReachBottom: function(param){
    // 触底向后加载几个视频
    var that = this;
    var page = that.data.page;//当前页码
    var totalPage = that.data.totalPage;//总页数
    var records = that.data.records;//总记录数
    var videoListCount = that.data.videoListCount;//当前记录数
    var pageSize = that.data.pageSize;//当前页码大小
    if (page < totalPage && videoListCount < records) {
      // 如果当前页码小于总页码数且当前记录数小于总记录数则向后加载加载视频
      that.setData({
        page:page + 1
      })
      that.getVideoList();
    }else {
        wx.showToast({
          title: '没视频了喔~',
          icon: 'none',
          mask: true,
          duration: 1000
        });
      return;
    }
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
