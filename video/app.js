//app.js
App({
  // 需要打开内网穿透的配置
  // serverUrl:"http://wxtestproject.free.idcfengye.com",// 内网穿透
  // serverUrl:"http://10.30.20.211:8081",// 学校内网
  // serverUrl:"http://192.168.43.239:8081",// 手机网络
  // serverUrl:"http://192.168.0.101:8081",// 屋子里
  serverUrl:"http://localhost:8081",// 本机测试地址
  // serverUrl:"http://47.102.223.176:8080/video",// 正式环境无域名地址
  // serverUrl: "http://www.totoros.com.cn",// 正式环境域名地址

  videoUrl:"http://www.totoros.com.cn/source",
  
  userInfo:null,

  setGlobalUserInfo:function(user){
    wx.setStorageSync('userInfo', user);
  },
  getGlobalUserInfo:function(){
    return wx.getStorageSync('userInfo');
  },
  reportReasonArray:[
    "政治敏感",
    "涉嫌诈骗",
    "诱导分享",
    "辱骂谩骂",
    "广告垃圾",
    "引人不适",
    "过于暴力",
    "色情低俗",
    "违法违纪",
    "其它原因"
  ]
})