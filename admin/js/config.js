var getserverUrl = function(){
    // return "http://localhost:8089"; // 本地环境
    return "http://47.102.223.176:8080/admin"; // 正式环境
    // return "http://192.168.0.103:8089"; //屋子里
    // return "http://10.30.20.211:8089"; //校园内网
}
var getResourceServerUrl = function(){
    // return "http://localhost:8081"; // 本地环境
    return "http://47.102.223.176:8080/video"; // 正式环境
    // return "http://192.168.0.103:8089"; //屋子里
    // return "http://10.30.20.211:8089"; //校园内网
}
var getUserInfo = function(){
    var userInfo = getMyCookie("userInfo");
}
