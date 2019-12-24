package com.jonas.dto;

import com.jonas.pojo.UserInfo;

/**
 * 此DTO可用来接收前台传过来的JSON参数，无特殊含义
 * @author cmj
 * @date 2019-03-28 20:13
 */
public class UserDTO {
    private UserInfo userInfo;
    private Integer page;
    private Integer pageSize;


    private String userId;
    private String fanId;
    private boolean likeUserStatus;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFanId() {
        return fanId;
    }

    public void setFanId(String fanId) {
        this.fanId = fanId;
    }

    public boolean isLikeUserStatus() {
        return likeUserStatus;
    }

    public void setLikeUserStatus(boolean likeUserStatus) {
        this.likeUserStatus = likeUserStatus;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userInfo=" + userInfo +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", userId='" + userId + '\'' +
                ", fanId='" + fanId + '\'' +
                ", likeUserStatus=" + likeUserStatus +
                '}';
    }
}
