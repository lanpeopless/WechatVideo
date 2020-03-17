// 全局配置接口信息
const requestInfo = {
    user: {
        login: {
            "url": "/user/userAuth/login",
            "method": "POST",
        },
        register: {
            "url": "/user/userAuth/register",
            "method": "POST",
        },
        logout: {
            "url": "/user/userAuth/logout",
            "method": "GET",
        },
        queryUserInfoById: {
            "url": "/user/business/queryUserInfoById",
            "method": "POST",
        },
        getUserLikeStatus: {
            "url": "/user/business/getUserLikeStatus",
            "method": "POST",
        },
    },
    video: {
        getPersonalVideo: {
            "url": "/video/business/getPersonalVideo",
            "method": "POST",
        },
        selectVideoByCondition: {
            "url": "/video/business/selectVideoByCondition",
            "method": "POST",
        },
        selectHotWords: {
            "url": "/video/business/selectHotWords",
            "method": "GET",
        }
    }
}

module.exports = requestInfo;