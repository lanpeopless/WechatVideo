package com.jonas.dto;

/**
 * @author cmj
 * @date 2019-03-27 23:12
 */
public class LikeVideoDTO {
    private String userId;
    private String videoId;
    private String videoCreatorId;
    private String likeStatus;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoCreatorId() {
        return videoCreatorId;
    }

    public void setVideoCreatorId(String videoCreatorId) {
        this.videoCreatorId = videoCreatorId;
    }

    public String getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
    }

    @Override
    public String toString() {
        return "LikeVideoDTO{" +
                "userId='" + userId + '\'' +
                ", videoId='" + videoId + '\'' +
                ", videoCreatorId='" + videoCreatorId + '\'' +
                ", likeStatus='" + likeStatus + '\'' +
                '}';
    }


}
