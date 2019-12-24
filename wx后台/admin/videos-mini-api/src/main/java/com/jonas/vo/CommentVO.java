package com.jonas.vo;

import java.util.Date;

/**
 * @author cmj
 * @date 2019-03-31 22:42
 */
public class CommentVO {
    private String id;

    private String videoId;

    private String fromUserId;

    private Date createTime;

    private String comment;

    private String faceImage;

    private String nickname;

    private String toUserId;

    private String fatherCommentId;

    private String toNickname;

    private String fatherComment;

    public String getFatherComment() {
        return fatherComment;
    }

    public void setFatherComment(String fatherComment) {
        this.fatherComment = fatherComment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getFatherCommentId() {
        return fatherCommentId;
    }

    public void setFatherCommentId(String fatherCommentId) {
        this.fatherCommentId = fatherCommentId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToNickname() {
        return toNickname;
    }

    public void setToNickname(String toNickname) {
        this.toNickname = toNickname;
    }

    @Override
    public String toString() {
        return "CommentVO{" +
                "id='" + id + '\'' +
                ", videoId='" + videoId + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", createTime=" + createTime +
                ", comment='" + comment + '\'' +
                ", faceImage='" + faceImage + '\'' +
                ", nickname='" + nickname + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", fatherCommentId='" + fatherCommentId + '\'' +
                ", toNickname='" + toNickname + '\'' +
                ", fatherComment='" + fatherComment + '\'' +
                '}';
    }
}
