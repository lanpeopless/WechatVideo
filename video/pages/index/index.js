//index.js
//获取应用实例
const app = getApp();
const cameraContext = wx.createCameraContext(this);
var searchValue;
Page({
  data: {
    videoList:[],
    totalPage:1,//总页数
    page:1,// 当前页码
    videoListCount:0,//当前记录数
    records:1, //总记录数
    pageSize:10,// 当前页面大小
    screenWidth: 350,
    screenHeight: 350,
    pixelRatio:1,
    like: false,
    serverUrl: "",
    videoUrl: "",
    showFullScreenBtn:false,
    isCanPlay:true,
    isOpenCamera:false,
    searchValue:'',
    arrIndex:0,
    mineVideo: [] //从个人界面点击进入的视频展示页面
  },
  showVideoInfo:function(event){
    // 接受image传来的 data-id  ---- 微信特有的传参方式
    var videoId = event.currentTarget.dataset.id;
    var videoList = this.data.videoList;
    videoList.forEach(item => {
      if(item.id == videoId){
          item.check=true;
      }else {
          item.check=false;
      }
    });
    this.setData({
      videoList:videoList
    });
    
  },
  // onPullDownRefresh:function(){
  //   console.log('onPullDownRefresh');
  //   // 下拉刷新
  //   // wx.startPullDownRefresh();
  //   this.setData({
  //     videoList:[],
  //     page: 1,
  //     pageSize:10
  //   })
  //   this.getVideoByPage();
  //   wx.stopPullDownRefresh(); 
  // },
  // onReachBottom:function(){
  //   // 触底向后加载几个视频
  //   var that = this;
  //   var page = that.data.page;//当前页码
  //   var totalPage = that.data.totalPage;//总页数
  //   var records = that.data.records;//总记录数
  //   var videoListCount = that.data.videoListCount;//当前记录数
  //   var pageSize = that.data.pageSize;//当前页码大小
  //   if (page < totalPage && videoListCount < records) {
  //     // 如果当前页码小于总页码数且当前记录数小于总记录数则向后加载加载视频
  //     that.setData({
  //       page:page + 1
  //     })
  //     that.getVideoByPage();
  //   }else {
  //       wx.showToast({
  //         title: '没视频了喔~',
  //         icon: 'none',
  //         mask: true,
  //         duration: 1000
  //       });
  //     return;
  //   }
    
  // },
  onLoad:function(param){    
    cameraContext.stopRecord();
    var that = this;
    var screenWidth = wx.getSystemInfoSync().windowWidth;
    var screenHeight = wx.getSystemInfoSync().windowHeight;
    var pixelRatio = wx.getSystemInfoSync().pixelRatio;
    searchValue = param.searchValue;
    var arrIndex = param.arrindex;
    

    if (arrIndex == null || arrIndex == undefined || arrIndex == '' ){
      arrIndex=0;
    }
    var serverUrl = app.serverUrl;
    var videoUrl = app.videoUrl;
    that.setData({
      screenWidth: screenWidth,
      screenHeight: screenHeight,
      pixelRatio:pixelRatio,
    });
    if (searchValue == null || searchValue == undefined || searchValue == '' ){
      // 如果搜索关键词为空则执行查询所有视频（仅分页)
      if (param.mineVideo != null && param.mineVideo != undefined && param.mineVideo != ''){
        that.setData({
          videoList:JSON.parse(param.mineVideo),
          serverUrl:serverUrl,
          videoUrl:videoUrl,
          arrIndex:arrIndex
        })
      }else {
        that.getVideoByPage();
      }
    }else {
      // 如果搜索关键词不为空则执行按视频描述搜索视频（分页 & 视频描述）
      that.setData({
        videoList:[]
      });
      that.getVideoByDesc(searchValue);
    }
    
  },
  onHide:function(){
    this.setData({
      isCanPlay:true
    });
    this.videoController();
  },
  /**
   * 点击触发搜索事件
   */
  search:function(){
    console.log('this is search');
    wx.redirectTo({
      url: '../searchVideo/searchVideo'
    })
  },
  /**
   * 点击查看视频用户信息触发事件
   */
  intoUserInfo:function(param){
    // 通过 data-xxx 获取参数，xxx为全小写
    var userInfo = app.userInfo;
    var videoCreatorId = param.currentTarget.dataset.videocreatorid;
    // 先判断用户是否登录了
    // 如果登录了且视频创作者和用户一致就跳转到到个人中心查询个人信息（不带videoCreatorId）
    // 如果未登录或已经登录的用户和视频创作者不一致，那么就跳转到查询视频创作者的个人信息
    if (userInfo != null) {
      // 用户已登录
      if (userInfo.id == videoCreatorId){
        // 同一个用户，查询自己的个人信息
        wx.redirectTo({
          url: '/pages/mine/mine',
        })
      }else {
        wx.redirectTo({
          url: '/pages/mine/mine?videoCreatorId=' + videoCreatorId,
        })
      }
    }else{
      // 用户未登录
      wx.redirectTo({
        url: '/pages/mine/mine?videoCreatorId=' + videoCreatorId,
      })
    }

  },
  /**
   * 点击关注按钮触发事件
   */
  likeUser:function(param){
    console.log('this is likeUser');
    var that = this;
    var videoCreatorId = param.currentTarget.dataset.videocreatorid;
    var videoId = param.currentTarget.dataset.videoid;
    var serverUrl = app.serverUrl;
    var videoUrl = app.videoUrl;
    var userInfo = app.userInfo;
    if (userInfo != null) {
      if (userInfo.id != videoCreatorId) {
        wx.request({
          url: serverUrl + '/user/changeUserLikeStatus',
          data: {
            userId:videoCreatorId,
            fanId:userInfo.id,
            likeUserStatus:true
          },
          method: 'POST',
          header: {
            'content-type': 'application/json' // 默认值
          }, // 设置请求的 header
          success: function(res){
            console.log(res);
            var videoList = that.data.videoList;
             // 暂时赋值给页面新的改变，不做页面刷新，但是容易引起同步数据的问题
            for ( var i = 0; i < videoList.length; i++) {
              var id = videoList[i].id;
              if (id == videoId) {
                videoList[i].likeUser = true;
              }else {
                videoList[i].likeUser = false;                
              }
            }
            that.setData({
              videoList:videoList
            });
          }
        })
      }
    }else {
      wx.redirectTo({
        url: '/pages/userLogin/userLogin?next=index',
      })
    }
  },
  /**
   * 点击喜欢按钮触发事件
   */
  likeVideo:function(param){
    var that = this;
    var serverUrl = app.serverUrl;
    var videoUrl = app.videoUrl;
    var userInfo = app.userInfo;
    
    var videoCreatorId = param.currentTarget.dataset.videocreatorid;
    var likeStatusParam = param.currentTarget.dataset.likestatus;
    var likeStatus ;
    var videoId = param.currentTarget.dataset.videoid;
    if (likeStatusParam) {
      // 表示原来是喜欢，点击后变成不喜欢
      likeStatus = 'unlike';
    }else {
      // 表示原来是不喜欢，点击后变成喜欢
      likeStatus = 'like';
    }
    
    // if (false){
    if (userInfo != null){
      // 用户登录后才能进行点赞
      var userId = userInfo.id;
      wx.request({
        url: serverUrl + '/video/changeVideoLikeStatus',
        data: {
          userId: userId,
          videoId: videoId,
          videoCreatorId: videoCreatorId,
          likeStatus: likeStatus
        },
        method: 'POST',
        header: {
          'content-type': 'application/json' // 默认值
        }, // 设置请求的 header
        success: function(res){
          // 暂时赋值给页面新的改变，不做页面刷新，但是容易引起同步数据的问题
          var videoList = that.data.videoList;
          for ( var i = 0; i < videoList.length; i++) {
            var id = videoList[i].id;
            if (id == videoId) {
              videoList[i].likeVildeo = !likeStatusParam;
            }
          }
          that.setData({
            videoList:videoList
          });
        }
      })

    }else {
      wx.redirectTo({
        url: '/pages/userLogin/userLogin?next=index',
      })
    }
  },
  /**
   * 点击评论按钮触发事件
   */
  commentVideo:function(param){
    var videoId = param.currentTarget.dataset.videoid;
    var that = this;
    var userInfo = app.userInfo;
    if (userInfo != null){
      wx.navigateTo({
        url: '/pages/comment/comment?videoId=' + videoId,
      })
    }else {
      wx.redirectTo({
        url: '/pages/userLogin/userLogin?next=index',
      })
    }
  },
  /**
   * 点击视频信息按钮触发事件
   */
  searchVideoInfo:function(param){
    // console.log('this is shareVideo');
    var that = this;
    var videoPath = param.currentTarget.dataset.videopath;
    var videoCreatorId = param.currentTarget.dataset.videocreatorid;
    var videoId = param.currentTarget.dataset.videoid;
    var userInfo = app.userInfo;
    var serverUrl = app.serverUrl;
    var videoUrl = app.videoUrl;
    wx.showActionSheet({
      itemList: ['保存该视频','举报违规'],
      success(res) {
        var tapIndex = res.tapIndex;
        if (userInfo != null){
          if (tapIndex == 0) {
            // console.log('保存视频');
            wx.showLoading();
            wx.downloadFile({
              url: videoPath , // 仅为示例，并非真实的资源
              success(res) {
                // 只要服务器有响应数据，就会把响应内容写入文件并进入 success 回调，业务需要自行判断是否下载到了想要的内容
                if (res.statusCode === 200) {
                  console.log(res.tempFilePath);
                  wx.saveVideoToPhotosAlbum({
                    filePath: res.tempFilePath,
                    success(res) {
                      wx.hideLoading();
                      console.log(res.errMsg)
                    }
                  })
                }
              }
            })

          }else if (tapIndex == 1){
            wx.navigateTo({
              url: '/pages/report/report?videoId=' + videoId +'&publishUserId=' + videoCreatorId ,
            })
          }
        }else {
          wx.redirectTo({
            url: '/pages/userLogin/userLogin?next=index',
          })
        }
      },
    })
  },
  onShareAppMessage:function(param){
    console.log(param);
    
    var that = this;
    var videoId = param;
    var videoList = this.data.videoList;
    var videoListNew = new Array;
    var videoDesc;
    for ( var i = 0;i < videoList.length; i++){
      if (videoId == videoList[i].id){
        videoListNew.push(videoList[i]);
        videoDesc = videoList[i].videoDesc;
      }
    }
    return {
      title: videoDesc,
      path: "/pages/index/index?arrindex=0&mineVideo=" + JSON.stringify(videoList)
    }
    
  },
  /**
   * 点击分享按钮触发事件
   */
  shareVideo:function(){
    console.log('this is shareVideo');
  },
  /**
   * 视频滑动触发事件
   * @param {*} event 
   */
  swiperChange:function(event){
    var index = event.detail.current;
    var that = this;
    var page = that.data.page;//当前页码
    var pageSize = that.data.pageSize;//当前页码大小
    var totalPage = that.data.totalPage;//总页数
    var records = that.data.records;//总记录数
    var videoListCount = that.data.videoListCount;//当前记录数
    var videoList = that.data.videoList;//当前list
    videoList.forEach(item => {
      item.check=false;
    });
    this.setData({
      videoList:videoList
    });
    if (index+1==videoListCount){
        if (page < totalPage && videoListCount < records) {
          // 如果当前页码小于总页码数且当前记录数小于总记录数则向后加载加载视频
          that.setData({
            page:page + 1
          })
          that.getVideoByPage();
        } 
    }

    
  },
  /**
   * 加载视频的请求方法
   */
  getVideoByPage:function(){
    var that = this;
    const page = that.data.page;
    const pageSize = that.data.pageSize;
    const serverUrl = app.serverUrl;
    const videoUrl = app.videoUrl;
    var userInfo = app.userInfo;
    var currentUserId = null;
    if(userInfo!=null){
      currentUserId =  userInfo.id;
    }
    wx.showLoading({
      title: '请等待',
    });
    wx.request({
        url: serverUrl + '/video/selectAllVideo', 
        method: "POST",
        data: {
          page: page,
          pageSize: pageSize,
          currentUserId: currentUserId
        },
        header: {
          'content-type': 'application/json' // 默认值
        },
        success(res) {
          if (res.data.status == 200) {
            var videoList = res.data.data.rows;
            var videoListOld = that.data.videoList;
            var videoListNew = videoListOld.concat(videoList);
            var videoListCount = videoListNew.length;
            var page = res.data.data.page;
            var records = res.data.data.records;
            var total = res.data.data.total;
            if (videoListCount!=0) {
              for (var i = 0;i < videoList.length; i++) {
                videoList[i].check=false;
              }
              
              that.setData({
                videoList:videoListOld.concat(videoList),
                serverUrl:serverUrl,
                videoUrl:videoUrl,
                page:page,
                records:records,
                totalPage:total,
                videoListCount,videoListCount
              });
              wx.hideLoading();
              
              wx.showToast({
                title: '加载成功',
                icon: 'success',
                mask: true,
                duration: 1000
              });
            }else {
              wx.showToast({
                title: '视频库暂无视频',
                icon: 'none',
                mask: true,
                duration: 1000
              });
            }
          } else {
            wx.hideLoading();
            wx.showToast({
              title: '加载失败' + res.data.msg,
              icon: 'none',
              mask: true,
              duration: 2000
            })
          }
        }
    })
  },
  /**
   * 按视频描述查询视频
   */
  getVideoByDesc:function(value){
    var that = this;
    const page = that.data.page;
    const pageSize = that.data.pageSize;
    const serverUrl = app.serverUrl;
    const videoUrl = app.videoUrl;

    var userInfo = app.userInfo;
    var currentUserId = null;
    if(userInfo!=null){
      currentUserId =  userInfo.id;
    }
    var videoList = new Array();
    videoList.push({
      videoDesc:value
    })
    
    wx.showLoading({
      title: '请等待',
    });
    wx.request({
        url: serverUrl + '/video/selectVideoByCondition', 
        method: "POST",
        data: {
          page: page,
          pageSize: pageSize,
          videoList:videoList,
          currentUserId: currentUserId
        },
        header: {
          'content-type': 'application/json' // 默认值
        },
        success(res) {
          if (res.data.status == 200) {
            var videoList = res.data.data.rows;
            var videoListOld = that.data.videoList;
            var videoListNew = videoListOld.concat(videoList);
            var videoListCount = videoListNew.length;
            var page = res.data.data.page;
            var records = res.data.data.records;
            var total = res.data.data.total;
            if (videoListCount!=0) {
              for (var i = 0;i < videoList.length; i++) {
                videoList[i].check=false;
              }
              
              that.setData({
                videoList:videoListOld.concat(videoList),
                serverUrl:serverUrl,
                videoUrl: videoUrl,
                page:page,
                records:records,
                totalPage:total,
                videoListCount,videoListCount
              });
              wx.hideLoading();
              
              wx.showToast({
                title: '加载成功',
                icon: 'success',
                mask: true,
                duration: 1000
              });
            }else {
              wx.showToast({
                title: '视频库暂无视频',
                icon: 'none',
                mask: true,
                duration: 1000
              });
            }
          } else {
            wx.hideLoading();
            wx.showToast({
              title: '加载失败' + res.data.msg,
              icon: 'none',
              mask: true,
              duration: 2000
            })
          }
        }
    })
  },
  /**
   * 视频控制函数
   */
  videoController: function(e){
    var videoContext = wx.createVideoContext("index");
    var isCanPlay = this.data.isCanPlay;
    if (isCanPlay){
      videoContext.pause();
      isCanPlay = !isCanPlay;
    }else {
      videoContext.play();
      isCanPlay = !isCanPlay;
    }
    this.setData({
      isCanPlay:isCanPlay
    });
  },
  //事件处理函数
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
