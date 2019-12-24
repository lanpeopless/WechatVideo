package com.jonas.dto;

/**
 * 此DTO可用来接收前台传过来的JSON参数，无特殊含义
 * @author cmj
 * @date 2019-03-28 20:13
 */
public class UserDTO {
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

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId='" + userId + '\'' +
                ", fanId='" + fanId + '\'' +
                ", likeUserStatus=" + likeUserStatus +
                '}';
    }
}
