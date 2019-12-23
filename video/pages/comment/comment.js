
// const model = require('../cityChoose/cityChoose.js')
// const config = require('../../utils/config.js')
// const util = require('../../utils/util.js')
const app = getApp()
var mydata = {
  end: 0,
  replyUserName: ""
}

Page({
  data: {
    inputBoxShow: true,
    isScroll: true,
    page:1,
    pageSize:10,
    videoId:'190317BYDTFGHNXP',
    list:[],
    comment:'',
    placeholder:'请输入评论',
  },
  onLoad: function(options) {
    var that = this;
    console.log(options);
    var serverUrl = app.serverUrl;
    var userId = app.userInfo.id;
    var videoId = options.videoId;
    //设置scroll的高度
    wx.getSystemInfo({
      success: function(res) {
        that.setData({
          scrollHeight: res.windowHeight,
          videoId:videoId,
          serverUrl:serverUrl,
          userId:userId
        });
      }
    });
    that.getPageInfo();
  },
  /**
   * 页面下拉刷新事件的处理函数
   */
  refresh: function() {
      this.setData({
        list: [],
        page:1
      })
      this.getPageInfo();
  },
  /**
   * 页面上拉触底事件的处理函数
   */
  bindDownLoad: function() {
    var that = this;
    var page = that.data.page;//当前页码
    var totalPage = that.data.totalPage;//总页数
    var records = that.data.records;//总记录数
    var listCount = that.data.listCount;//当前记录数
    var list = that.data.list;//当前记list
    var pageSize = that.data.pageSize;//当前页码大小
    if (page < totalPage && listCount < records) {
      // 如果当前页码小于总页码数且当前记录数小于总记录数则向后加载加载视频
      that.setData({
        page:page + 1
      })
      that.getPageInfo();
    } 
  },
  bindReply: function(e) {
    console.log(e);
    mydata.commentId = e.target.dataset.commentid;
    mydata.replyUserName = e.target.dataset.commentusername;
    this.setData({
      replyUserName: mydata.replyUserName,
      reply: true
    })
  },
  // 合并数组
  addArr(arr1, arr2) {
    for (var i = 0; i < arr2.length; i++) {
      arr1.push(arr2[i]);
    }
    return arr1;
  },
  deleteComment:function(e){
    console.log(e);
    var that = this;
    var commentId = e.target.dataset.commentid;
    var userInfo = app.userInfo;
    wx.showModal({
      title: '删除评论',
      content: '请确认是否删除该评论？',
      success: function (res) {
        if (res.confirm) {
          var serverUrl = app.serverUrl;
          if (userInfo != null) {
            wx.request({
              url: serverUrl + '/video/deleteCommentVideo',
              data: {
                commentId:commentId
              },
              method: 'POST', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
              header: {
                'content-type': 'application/json' // 默认值
              },  // 设置请求的 header
              success: function(res){
                // success
                wx.showToast({
                  title:res.data.data,
                  icon:'none',
                  duration:1000
                })
                that.setData({
                  list:[]
                })
                that.getPageInfo();
                
              }
            })
          }
        }
      }
    })
  },
  cancleReply: function(e) {
    this.setData({
      placeholder:'请输入评论',
      reply: false
    })
  },
  // 更新页面信息
  // 此处的回调函数在 传入新值之前执行 主要用来清除页面信息
  getPageInfo() {
    var that = this;
    var page = this.data.page;
    var pageSize = this.data.pageSize;
    var videoId = this.data.videoId;
    var videoList = new Array;
    videoList.push({
      id:videoId
    });
    var serverUrl = app.serverUrl;
    wx.request({
      url: serverUrl + '/video/getCommentVideo',
      data: {
        page:page,
        pageSize:pageSize,
        videoList:videoList
      },
      method: 'POST', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
      header: {
        'content-type': 'application/json' // 默认值
      }, // 设置请求的 header
      success: function(res){
        // success
        var list = res.data.data.rows;
        var page = res.data.data.page;
        var records = res.data.data.records;
        var total = res.data.data.total;
        for (var i = 0;i < list.length; i++) {
          var createTime = list[i].createTime;
          var date = new Date(createTime);
          list[i].createTime = date.toLocaleString();
        }
        var oldList = that.data.list;
        var newList = oldList.concat(list);
        var listCount = newList.length;
        
        that.setData({ 
          list:oldList.concat(list),
          page:page,
          records:records,
          totalPage:total,
          listCount:listCount
        })
      }
    })
  },
  replyComment:function(param){
    var fatherCommentId = param.currentTarget.dataset.fathercommentid;
    var toUserId = param.currentTarget.dataset.touserid;
    var toNickname = param.currentTarget.dataset.tonickname;
    this.setData({
      placeholder:'请输入回复内容 ',
      replyUserName:toNickname,
      reply:true,
      fatherCommentId:fatherCommentId,
      toUserId:toUserId,
      toNickname:toNickname
    })
  },
  /**
   * 提交评论
   * @param {*} e 
   */
  submitForm(e) {
    var reply = this.data.reply;
    var form = e.detail.value;
    var that = this;
    var userInfo = app.userInfo;
    if(form.comment == ""){
      wx.showToast({
        title:'请输入内容',
        icon:'none',
        duration:1000
      })
      return;
    }
    // 提交评论
    var serverUrl = app.serverUrl;
    if (userInfo != null) {
      if (reply) {
        var fatherCommentId = that.data.fatherCommentId;
        var toUserId = that.data.toUserId;
        var toNickname = that.data.toNickname;
        wx.request({
          url: serverUrl + '/video/commentVideo',
          data: {
            videoId:that.data.videoId,
            fromUserId:userInfo.id,
            comment:form.comment,
            fatherCommentId:fatherCommentId,
            toUserId:toUserId,
            toNickname:toNickname
          },
          method: 'POST', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: {
            'content-type': 'application/json' // 默认值
          },  // 设置请求的 header
          success: function(res){
            that.setData({
              list:[],
              comment:''
            })
            that.getPageInfo();
            that.cancleReply();
          }
        })
      } else {
        wx.request({
          url: serverUrl + '/video/commentVideo',
          data: {
            videoId:that.data.videoId,
            fromUserId:userInfo.id,
            comment:form.comment
          },
          method: 'POST', // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
          header: {
            'content-type': 'application/json' // 默认值
          },  // 设置请求的 header
          success: function(res){
            that.setData({
              list:[],
              comment:''
            })
            that.getPageInfo();
            // success
          }
        })
      }

    }else {
      wx.redirectTo({
        url: '/pages/userLogin/userLogin?next=index',
      })
    }
    // wx.request({
    //   url: config.insertComment,
    //   method: "POST",
    //   data: {
    //     sourceId: mydata.sourceId,
    //     comment: form.comment,
    //     userId: app.globalData.haulUserInfo.id,
    //     userName: app.globalData.haulUserInfo.userName,
    //     replyCommentId: mydata.commentId,
    //     replyUserName: mydata.replyUserName,
    //     userPhoto: app.globalData.haulUserInfo.userPhoto
    //   },
    //   header: {
    //     "content-type": "application/x-www-form-urlencoded;charset=utf-8",
    //     //token: app.globalData.token
    //   },
    //   success: res => {
    //     console.log(res)
    //     if (res.data.success) {
    //       wx.showToast({
    //         title: "回复成功"
    //       })
    //       that.refresh();
    //       mydata.commentId = "";
    //       mydata.replyUserName = "";
    //       this.setData({
    //         replyUserName: mydata.replyUserName,
    //         reply: false
    //       })
    //     } else {
    //       wx.showToast({
    //         title: '回复失败，请检查您的网络',
    //       })
    //     }
    //   }
    // })
  },
  /**
   * 提交回复
   */
  doReply:function(){

  }

})