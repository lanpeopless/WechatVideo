package com.jonas.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户对象",description = "这是用户对象")

public class UserInfo {

    private String id;
    @ApiModelProperty(value = "用户名",name = "username",required = true)
    private String username;
    @ApiModelProperty(value = "密码",name = "password",required = true)
    private String password;

    private String faceImage;

    private String nickname;

    private String fansCounts;

    private String followCounts;

    private String receiveLikeCounts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFansCounts() {
        return fansCounts;
    }

    public void setFansCounts(String fansCounts) {
        this.fansCounts = fansCounts;
    }

    public String getFollowCounts() {
        return followCounts;
    }

    public void setFollowCounts(String followCounts) {
        this.followCounts = followCounts;
    }

    public String getReceiveLikeCounts() {
        return receiveLikeCounts;
    }

    public void setReceiveLikeCounts(String receiveLikeCounts) {
        this.receiveLikeCounts = receiveLikeCounts;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", faceImage='" + faceImage + '\'' +
                ", nickname='" + nickname + '\'' +
                ", fansCounts=" + fansCounts +
                ", followCounts=" + followCounts +
                ", receiveLikeCounts=" + receiveLikeCounts +
                '}';
    }
}